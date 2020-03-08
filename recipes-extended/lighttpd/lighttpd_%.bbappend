FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'meta-nas_ligthttpd_update_conf', 'lighttpd-module-cgi lighttpd-module-auth', '', d)}"

SRC_URI += "file://lighttpd-user.conf"
SRC_URI += "file://lighttpd.users"


do_metanas_ligthttpd_update_conf() {
	sed -i -e "s:@USER@:${USER_FOR_AUTH}:" ${WORKDIR}/lighttpd-user.conf
	sed -i -e "s:@USER@:${USER_FOR_AUTH}:" ${WORKDIR}/lighttpd.users
	sed -i -e "s:@PASS@:${PASS_FOR_AUTH}:" ${WORKDIR}/lighttpd.users
	mv ${D}${sysconfdir}/lighttpd/lighttpd.conf ${D}${sysconfdir}/lighttpd/lighttpd-old.conf
	install -m 0755 ${WORKDIR}/lighttpd-user.conf ${D}${sysconfdir}/lighttpd/lighttpd.conf
	install -m 0755 ${WORKDIR}/lighttpd.users ${D}${sysconfdir}/lighttpd/lighttpd.users
	rm ${D}/www/pages/index.html 
}




do_install_append () {
${@bb.utils.contains('DISTRO_FEATURES', 'meta-nas_ligthttpd_update_conf', 'do_metanas_ligthttpd_update_conf', '', d)}
}
