DESCRIPTION = "Transmission is a BitTorrent client w/ a built-in Ajax-Powered Webif GUI."
SECTION = "network"
HOMEPAGE = "www.transmissionbt.com/"

DEPENDS = "libevent gnutls openssl libtool intltool-native curl"

RDEPENDS_${PN}-web = "${PN}"
RDEPENDS_${PN} = "${@base_contains('DISTRO_FEATURES', 'systemd', 'systemd', 'start-stop-daemon', d)}"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=7ee657ac1dce0e7353033fc06c8087d2"

REQUIRED_DISTRO_FEATURES = " libc-locale-code "
# Upstream names releases after SVN revs
PV = "r${SRCREV}"

SRC_URI = "svn://svn.transmissionbt.com/Transmission/;protocol=svn;module=trunk \
          file://settings.json"

S = "${WORKDIR}/trunk"

SRC_URI =+ "${@base_contains('DISTRO_FEATURES', 'systemd', '', 'file://transmission-daemon', d)}"
SRC_URI =+ "${@base_contains('DISTRO_FEATURES', 'busybox_send_email', 'file://send-email-transmission.sh', '', d)}"
HOME_DIR_TRANSMISSION ??= "${localstatedir}/lib/transmission-daemon"
DOWNLOAD_DIR_TRANSMISSION ??= "${HOME_DIR_TRANSMISSION}/Downloads"
inherit autotools gettext useradd update-rc.d  systemd distro_features_check

PACKAGECONFIG = "${@base_contains('DISTRO_FEATURES', 'x11', 'gtk', '', d)} \
                 ${@base_contains('DISTRO_FEATURES','systemd','systemd','',d)}"

PACKAGECONFIG[gtk] = " --with-gtk,--without-gtk,gtk+3,"
PACKAGECONFIG[systemd] = "--with-systemd-daemon,--without-systemd-daemon,systemd,"

# Configure aborts with:
# config.status: error: po/Makefile.in.in was not created by intltoolize.
B = "${S}"
do_configure_prepend() {
	sed -i /AM_GLIB_GNU_GETTEXT/d ${S}/configure.ac
	cd ${S}
	./update-version-h.sh
	intltoolize --copy --force --automake
}

do_install_initd() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0744 ${WORKDIR}/transmission-daemon ${D}${sysconfdir}/init.d/
	sed -i -e 's@#TRANSMISSION_ARGS=""@TRANSMISSION_ARGS=" -g ${HOME_DIR_TRANSMISSION}"@'  ${D}${sysconfdir}/init.d/transmission-daemon
	chown root:root ${D}${sysconfdir}/init.d/transmission-daemon
}

do_install_systemd() {
	install -d ${D}${nonarch_base_libdir}/systemd/system
	install -m 0644 ${S}/daemon/transmission-daemon.service ${D}${nonarch_base_libdir}/systemd/system
	sed -i -e 's@/usr/bin/transmission-daemon -f --log-error@/usr/bin/transmission-daemon -f --log-error -g ${HOME_DIR_TRANSMISSION}@' ${D}${nonarch_base_libdir}/systemd/system/transmission-daemon.service

}

do_install_send_mail_script() {
	install -m 0755 ${WORKDIR}/send-email-transmission.sh ${D}${bindir}/send-email-transmission.sh
	sed -i -e 's@"script-torrent-done-enabled": false,@"script-torrent-done-enabled": true,@'  ${D}${HOME_DIR_TRANSMISSION}/settings.json
	sed -i -e 's@"script-torrent-done-filename": "",@"script-torrent-done-filename": "/usr/bin/send-email-transmission.sh",@'  ${D}${HOME_DIR_TRANSMISSION}/settings.json
}

do_set_download_dir() {
	sed -i -e 's@"download-dir": "", @"download-dir": "${DOWNLOAD_DIR_TRANSMISSION}",@'   ${D}${HOME_DIR_TRANSMISSION}/settings.json
	sed -i -e 's@"incomplete-dir": "", @"incomplete-dir": "${DOWNLOAD_DIR_TRANSMISSION}",@'   ${D}${HOME_DIR_TRANSMISSION}/settings.json
}

do_set_whitelist() {
	if [ -z ${IP_ALLOWED_TRANSMISSION+x} ]; then
	#${@bb.warn('transmission : IP_ALLOWED_TRANSMISSION is not set so rpc-whitelist-enabled will be disabled' )}
		sed -i -e 's@"rpc-whitelist-enabled": true,@"rpc-whitelist-enabled": false,@'   ${D}${HOME_DIR_TRANSMISSION}/settings.json;
	else 
		echo "var is set to '$var'"; 
	fi
}

do_set_user_pass() {
	sed -i -e 's|"rpc-username": "@USER@",|"rpc-username": "${USER_FOR_AUTH}",|'   ${D}${HOME_DIR_TRANSMISSION}/settings.json
	sed -i -e 's|"rpc-password": "@PASS@",|"rpc-password": "${PASS_FOR_AUTH}",|'   ${D}${HOME_DIR_TRANSMISSION}/settings.json
}

do_install_append() {

	install -d ${D}${HOME_DIR_TRANSMISSION}
	install -d ${D}${DOWNLOAD_DIR_TRANSMISSION}
	install -m 0644 ${WORKDIR}/settings.json ${D}${HOME_DIR_TRANSMISSION}/settings.json
	do_set_download_dir
	do_set_user_pass
	do_set_whitelist
	${@base_contains('DISTRO_FEATURES', 'systemd', 'do_install_systemd', 'do_install_initd', d)}
	${@base_contains('DISTRO_FEATURES', 'busybox_send_email', 'do_install_send_mail_script', '', d)}
	chown -R transmission:transmission ${D}${HOME_DIR_TRANSMISSION}
	chown -R transmission:transmission ${D}${DOWNLOAD_DIR_TRANSMISSION}

}

pkg_postinst_${PN}() {
	if [ -n "$D" ]; then
		exit 1
	else
		sysctl -w net.core.rmem_max=4194304
		sysctl -w net.core.wmem_max=1048576
	fi
}
 
PACKAGES += "${PN}-gui ${PN}-client ${PN}-web"

FILES_${PN}-client = "${bindir}/transmission-remote ${bindir}/transmission-cli ${bindir}/transmission-create ${bindir}/transmission-show ${bindir}/transmission-edit"
FILES_${PN}-gui += "${bindir}/transmission-gtk ${datadir}/icons ${datadir}/applications ${datadir}/pixmaps"
FILES_${PN}-web = "${datadir}/transmission/web"

FILES_${PN} = "${bindir}/transmission-daemon ${sysconfdir} ${DOWNLOAD_DIR_TRANSMISSION} ${HOME_DIR_TRANSMISSION}/settings.json ${@bb.utils.contains('DISTRO_FEATURES', 'busybox_send_email', '${bindir}/send-email-transmission.sh', '', d)}"

SYSTEMD_SERVICE_${PN} = "transmission-daemon.service"
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system transmission"
USERADD_PARAM_${PN} = "--home ${HOME_DIR_TRANSMISSION} --create-home \
                       --gid transmission \
                       --shell ${base_bindir}/false \
                       --system \
                       transmission"


INITSCRIPT_NAME = "transmission-daemon"
INITSCRIPT_PARAMS = "defaults 80"

