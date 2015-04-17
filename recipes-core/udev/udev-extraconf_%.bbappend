FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"


SRC_URI_append_raspberrypi = " \
    file://mount_raspberrypi.sh \
"
SRC_URI_append_raspberrypi2 = " \
    file://mount_raspberrypi.sh \
"

do_install_append_raspberrypi() {
    install -m 0755 ${WORKDIR}/mount_raspberrypi.sh ${D}${sysconfdir}/udev/scripts/mount.sh
    sed -i -e 's|@DOWNLOAD_DIR@|${DOWNLOAD_DIR_TRANSMISSION}|' ${D}${sysconfdir}/udev/scripts/mount.sh
}

do_install_append_raspberrypi2() {
    install -m 0755 ${WORKDIR}/mount_raspberrypi.sh ${D}${sysconfdir}/udev/scripts/mount.sh
    sed -i -e 's|@DOWNLOAD_DIR@|${DOWNLOAD_DIR_TRANSMISSION}|' ${D}${sysconfdir}/udev/scripts/mount.sh
}
