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

SRC_URI = "svn://svn.transmissionbt.com/Transmission/;protocol=svn;module=trunk"

S = "${WORKDIR}/trunk"

SRC_URI =+ "${@base_contains('DISTRO_FEATURES', 'systemd', '', 'file://transmission-daemon', d)}"
SRC_URI =+ "${@base_contains('DISTRO_FEATURES', 'busybox_send_email', 'file://send-email-transmission.sh', '', d)}"

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
	chown root:root ${D}${sysconfdir}/init.d/transmission-daemon
}

do_install_systemd() {
	install -d ${D}${nonarch_base_libdir}/systemd/system
	install -m 0644 ${S}/daemon/transmission-daemon.service ${D}${nonarch_base_libdir}/systemd/system
}

do_install_send_mail_script() {
	install -m 0755 ${WORKDIR}/send-email-transmission.sh ${D}${bindir}/send-email-transmission.sh
}

do_install_append() {
	${@base_contains('DISTRO_FEATURES', 'systemd', 'do_install_systemd', 'do_install_initd', d)}
	${@bb.utils.contains('DISTRO_FEATURES', 'busybox_send_email', 'do_install_send_mail_script', '', d)}
}

pkg_postinst_${PN}() {
	if [ -n "$D" ]; then
		exit 1
	else
		sysctl -w net.core.rmem_max=4194304
		sysctl -w net.core.wmem_max=1048576
		if ${@bb.utils.contains("DISTRO_FEATURES", "busybox_send_email", "true", "false", d)}; then
			sed -i -e 's@"script-torrent-done-enabled": false,@"script-torrent-done-enabled": true,@'  ${localstatedir}/lib/transmission-daemon/.config/settings.json
			sed -i -e 's@"script-torrent-done-filename": "",@"script-torrent-done-filename": "/usr/bin/send-email-transmission.sh",@'  ${localstatedir}/lib/transmission-daemon/.config/settings.json
		fi
	fi
}
 
PACKAGES += "${PN}-gui ${PN}-client ${PN}-web"

FILES_${PN}-client = "${bindir}/transmission-remote ${bindir}/transmission-cli ${bindir}/transmission-create ${bindir}/transmission-show ${bindir}/transmission-edit"
FILES_${PN}-gui += "${bindir}/transmission-gtk ${datadir}/icons ${datadir}/applications ${datadir}/pixmaps"
FILES_${PN}-web = "${datadir}/transmission/web"

FILES_${PN} = "${bindir}/transmission-daemon ${sysconfdir} ${@bb.utils.contains('DISTRO_FEATURES', 'busybox_send_email', '${bindir}/send-email-transmission.sh', '', d)}"

SYSTEMD_SERVICE_${PN} = "transmission-daemon.service"
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system transmission"
USERADD_PARAM_${PN} = "--home ${localstatedir}/lib/transmission-daemon --create-home \
                       --gid transmission \
                       --shell ${base_bindir}/false \
                       --system \
                       transmission"


INITSCRIPT_NAME = "transmission-daemon"
INITSCRIPT_PARAMS = "defaults 80"

