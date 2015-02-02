#PRINC := "${@int(PRINC) + 1}"

# look for files in the layer first
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'busybox_send_email', 'file://sendmail.cfg', '', d)}"
