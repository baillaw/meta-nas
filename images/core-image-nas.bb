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
     fi
}
ROOTFS_POSTPROCESS_COMMAND += "setDownloadDirectory; "


IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-full-cmdline \
    packagegroup-core-boot \
    transmission-web \
    filemanager \
    dhcp-client \
    minidlna \
     "

inherit core-image
