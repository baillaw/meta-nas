FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += " \
	lighttpd-module-cgi \
"

SRC_URI += "file://lighttpd-user.conf"

do_install_append() {
	mv ${D}${sysconfdir}/lighttpd.conf ${D}${sysconfdir}/lighttpd-old.conf
	install -m 0755 ${WORKDIR}/lighttpd-user.conf ${D}${sysconfdir}/lighttpd.conf
}
