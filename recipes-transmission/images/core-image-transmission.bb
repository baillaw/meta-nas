DESCRIPTION = "A console-only image with more full-featured Linux system \
functionality installed."

IMAGE_FEATURES += "splash ssh-server-openssh"

inherit core-image

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-full-cmdline \
    packagegroup-core-boot \
    transmission-web \
    filemanager \
     "

inherit core-image
