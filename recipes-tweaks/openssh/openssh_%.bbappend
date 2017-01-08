FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://banner"
do_install_metanas_update_conf () {
  sed -i -e 's:^#PasswordAuthentication .*$:PasswordAuthentication no:g' ${D}${sysconfdir}/ssh/sshd_config
  sed -i -e 's:^#PermitRootLogin .*$:PermitRootLogin no:g' ${D}${sysconfdir}/ssh/sshd_config
  sed -i -e 's:^#MaxAuthTries .*$:MaxAuthTries 3:g' ${D}${sysconfdir}/ssh/sshd_config
}

do_install_metanas_banner() {
  install -D -m 644 ${WORKDIR}/banner  ${D}${sysconfdir}/banner
  sed -i -e 's:^#Banner .*$:Banner '${sysconfdir}'/banner:g' ${D}${sysconfdir}/ssh/sshd_config
}



do_install_append () {
${@bb.utils.contains('DISTRO_FEATURES', 'meta-nas_openssh_banner', 'do_install_metanas_banner', '', d)}
${@bb.utils.contains('DISTRO_FEATURES', 'meta-nas_openssh_update_conf', 'do_install_metanas_update_conf', '', d)}
}

FILES_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'meta-nas_openssh_banner', '${sysconfdir}/banner', '', d)}"

