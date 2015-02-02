FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += " \
	lighttpd-module-cgi \
	lighttpd-module-auth \
"

SRC_URI += "file://lighttpd-user.conf"
SRC_URI += "file://lighttpd.users"

do_configure_append() {
	sed -i -e "s:@USER@:${USER}:" ${WORKDIR}/lighttpd-user.conf
	sed -i -e "s:@USER@:${USER}:" ${WORKDIR}/lighttpd.users
}

do_install_append() {
	mv ${D}${sysconfdir}/lighttpd.conf ${D}${sysconfdir}/lighttpd-old.conf
	install -m 0755 ${WORKDIR}/lighttpd-user.conf ${D}${sysconfdir}/lighttpd.conf
	install -m 0755 ${WORKDIR}/lighttpd.users ${D}${sysconfdir}/lighttpd.users
}
