DESCRIPTION = "A console-only image with more full-featured Linux system \
functionality installed."

IMAGE_FEATURES += "splash ssh-server-openssh"

set_hostname (){
     #!/bin/sh -e
     echo "MyNAS" > ${IMAGE_ROOTFS}/etc/hostname;
}
ROOTFS_POSTPROCESS_COMMAND += "set_hostname; "

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-full-cmdline \
    packagegroup-core-boot \
    transmission-web \
    filemanager \
    dhcp-client \
    minidlna \
    menunas \
    flexget \
    "

inherit core-image
