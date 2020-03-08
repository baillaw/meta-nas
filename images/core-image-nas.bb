DESCRIPTION = "A console-only image with more full-featured Linux system \
functionality installed."

IMAGE_FEATURES += "splash ssh-server-openssh"
IMAGE_FEATURES += "package-management" 

inherit core-image extrausers
set_hostname (){
     #!/bin/sh -e
     echo "MyNAS" > ${IMAGE_ROOTFS}/etc/hostname;
}

set_sudoers_rules (){
     #!/bin/sh -e
     echo '%sudo ALL=(ALL) ALL' > ${IMAGE_ROOTFS}/etc/sudoers.d/nas
}


set_ssh_keys (){
     #!/bin/sh -e
     if [ ! -s ${HOME}/.ssh/id_rsa_meta-nas ]; then
     mkdir -p ${HOME}/.ssh/
     /usr/bin/ssh-keygen -b 4048 -t rsa -C "Meta Nas!" -f ${HOME}/.ssh/id_rsa_meta-nas
     fi
     mkdir -p ${IMAGE_ROOTFS}/home/${USER_FOR_AUTH}/.ssh
     cp ${HOME}/.ssh/id_rsa_meta-nas.pub  ${IMAGE_ROOTFS}/home/${USER_FOR_AUTH}/.ssh/authorized_keys
     chmod 700 ${IMAGE_ROOTFS}/home/${USER_FOR_AUTH}/.ssh
     chmod 600 ${IMAGE_ROOTFS}/home/${USER_FOR_AUTH}/.ssh/authorized_keys
     chown ${UID_FOR_AUTH}:${UID_FOR_AUTH} -R ${IMAGE_ROOTFS}/home/${USER_FOR_AUTH}
}

add_change_web_password_script (){
     mkdir -p ${IMAGE_ROOTFS}/usr/bin/
     cp ${THISDIR}/change_webpass.sh ${IMAGE_ROOTFS}/usr/bin/change_webpass.sh
     chmod 700 ${IMAGE_ROOTFS}/usr/bin/change_webpass.sh
     chown ${UID_FOR_AUTH}:${UID_FOR_AUTH} ${IMAGE_ROOTFS}/usr/bin/change_webpass.sh
}

add_ACPI_powerbutton (){
	 mkdir -p ${IMAGE_ROOTFS}/etc/acpi/events
     cat <<EOF > ${IMAGE_ROOTFS}/etc/acpi/powerbtn.sh
     #!/bin/sh
     # /etc/acpi/powerbtn.sh
     # Initiates a shutdown when the power putton has been
     # pressed.
     /sbin/shutdown -h now "Power button pressed"
EOF

     chmod a+x ${IMAGE_ROOTFS}/etc/acpi/powerbtn.sh
     
     cat <<EOF > ${IMAGE_ROOTFS}/etc/acpi/events/powerbtn
     event=button[ /]power
     action=/etc/acpi/powerbtn.sh
EOF   
}


ROOTFS_POSTPROCESS_COMMAND += "set_hostname; set_sudoers_rules; set_ssh_keys;add_change_web_password_script;add_ACPI_powerbutton;"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    udev-extraconf \
    packagegroup-core-full-cmdline \
    packagegroup-core-boot \
    transmission-web \
    filemanager \
    dhcp-client \
    minidlna \
    menunas \
     "


EXTRA_USERS_PARAMS = "groupadd sudo; useradd -u ${UID_FOR_AUTH} -P '${PASS_FOR_AUTH}' -G sudo ${USER_FOR_AUTH}; "

