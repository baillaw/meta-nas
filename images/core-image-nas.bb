DESCRIPTION = "A console-only image with more full-featured Linux system \
functionality installed."

IMAGE_FEATURES += "splash ssh-server-openssh"


setDownloadDirectory (){
     #!/bin/sh -e
     if [ x"${DOWNLOAD_DIR_TRANSMISSION}" = "x" ]; then
          exit 0
     else
         mkdir -p  ${IMAGE_ROOTFS}/${DOWNLOAD_DIR_TRANSMISSION}
         chmod 666 ${IMAGE_ROOTFS}/${DOWNLOAD_DIR_TRANSMISSION}
         # sed -i -e 's@"download-dir": "/var/lib/transmission-daemon/Downloads", @"download-dir": "${DOWNLOAD_DIR_TRANSMISSION}",@'   ${IMAGE_ROOTFS}/${localstatedir}/lib/transmission-daemon/.config/settings.json
         if ${@bb.utils.contains("DISTRO_FEATURES", "systemd", "true", "false", d)}; then
           sed -i -e 's@/usr/bin/transmission-daemon -f --log-error@/usr/bin/transmission-daemon -f --log-error -w ${DOWNLOAD_DIR_TRANSMISSION}@' ${IMAGE_ROOTFS}/${nonarch_base_libdir}/systemd/system/transmission-daemon.service
         else
           sed -i -e 's@#TRANSMISSION_ARGS=""@TRANSMISSION_ARGS=" -w ${DOWNLOAD_DIR_TRANSMISSION}"@'   ${IMAGE_ROOTFS}/etc/init.d/transmission-daemon
         fi
     fi
}
ROOTFS_POSTPROCESS_COMMAND += "setDownloadDirectory; "


IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-full-cmdline \
    packagegroup-core-boot \
    transmission-web \
    filemanager \
     "

inherit core-image
