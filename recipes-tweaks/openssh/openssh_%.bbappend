FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://banner"
do_install_append () {
  install -D -m 644 ${WORKDIR}/banner  ${D}${sysconfdir}/banner
  sed -i -e 's:^#Banner .*$:Banner '${sysconfdir}'/banner:g' ${D}${sysconfdir}/ssh/sshd_config

  sed -i -e 's:^#PermitRootLogin .*$:PermitRootLogin no:g' ${D}${sysconfdir}/ssh/sshd_config
  sed -i -e 's:^#MaxAuthTries .*$:MaxAuthTries 3:g' ${D}${sysconfdir}/ssh/sshd_config
}

FILES_${PN} += "${sysconfdir}/banner"
