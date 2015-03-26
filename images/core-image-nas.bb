DESCRIPTION = "A console-only image with more full-featured Linux system \
functionality installed."

IMAGE_FEATURES += "splash ssh-server-openssh"

set_hostname (){
     #!/bin/sh -e
     echo "MyNAS" > ${IMAGE_ROOTFS}/etc/hostname;
}

set_sudoers_rules (){
     #!/bin/sh -e
     echo '%sudo ALL=(ALL) ALL' > ${IMAGE_ROOTFS}/etc/sudoers.d/nas
}

ROOTFS_POSTPROCESS_COMMAND += "set_hostname; set_sudoers_rules;"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-full-cmdline \
    packagegroup-core-boot \
    transmission-web \
    filemanager \
    dhcp-client \
    minidlna \
    menunas \
     "

inherit core-image extrausers

EXTRA_USERS_PARAMS = "groupadd sudo; useradd -P '${PASS_FOR_AUTH}' -G sudo ${USER_FOR_AUTH}; usermod -L root;"

