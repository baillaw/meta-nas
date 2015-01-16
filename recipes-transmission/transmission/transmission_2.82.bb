DESCRIPTION = "Transmission is a BitTorrent client w/ a built-in Ajax-Powered Webif GUI."
SECTION = "network"
HOMEPAGE = "www.transmissionbt.com/"

DEPENDS = "libevent gnutls openssl libtool intltool-native curl"

RDEPENDS_${PN}-web = "${PN} lighttpd"
RDEPENDS_${PN} = "${@base_contains('DISTRO_FEATURES', 'systemd', 'systemd', 'start-stop-daemon', d)}"

LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=7ee657ac1dce0e7353033fc06c8087d2"

REQUIRED_DISTRO_FEATURES = " libc-locale-code "

SRC_URI = "http://download.transmissionbt.com/files/transmission-${PV}.tar.xz \
		   file://transmission-daemon \
			"
SRC_URI[md5sum] = "a5ef870c0410b12d10449c2d36fa4661"
SRC_URI[sha256sum] = "3996651087df67a85f1e1b4a92b1b518ddefdd84c654b8df6fbccb0b91f03522"

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
do_install_append() {
	${@base_contains('DISTRO_FEATURES', 'systemd', 'do_install_systemd', 'do_install_initd', d)}
}

PACKAGES += "${PN}-gui ${PN}-client ${PN}-web"

FILES_${PN}-client = "${bindir}/transmission-remote ${bindir}/transmission-cli ${bindir}/transmission-create ${bindir}/transmission-show ${bindir}/transmission-edit"
FILES_${PN}-gui += "${bindir}/transmission-gtk ${datadir}/icons ${datadir}/applications ${datadir}/pixmaps"
FILES_${PN}-web = "${datadir}/transmission/web"

FILES_${PN} = "${bindir}/transmission-daemon ${sysconfdir}"

SYSTEMD_SERVICE_${PN} = "transmission-daemon.service"
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system transmission"
USERADD_PARAM_${PN} = "--home ${localstatedir}/lib/transmission-daemon --create-home \
                       --gid transmission \
                       --shell ${base_bindir}/false \
                       --system \
                       transmission"


pkg_postinst_${PN}-web () {
#!/bin/sh -e
mv /www/pages /www/pages_bkp
ln -s ${datadir}/transmission/web /www/pages
}


INITSCRIPT_NAME = "transmission-daemon"
INITSCRIPT_PARAMS = "defaults 80"

