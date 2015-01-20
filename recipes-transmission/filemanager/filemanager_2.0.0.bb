DESCRIPTION = "A web file-explorer"
SECTION = "network"
HOMEPAGE = "https://github.com/simogeo/Filemanager"

RDEPENDS_${PN} = "lighttpd php-cgi"

SRC_URI = "https://github.com/simogeo/Filemanager/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "665547ad10bd4aeda5f36fd23558fd8b"
SRC_URI[sha256sum] = "03057cf948d9a63ed832931bd0dbaf692889a9da51015ec7c294451643090942"

SRC_URI += "file://filemanager.config.js"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://scripts/filemanager.min.js;md5=7ee657ac1dce0e7353033fc06c8087d2"

S = "${WORKDIR}/Filemanager-${PV}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"


do_install () {
    # Do it carefully
    [ -d "${S}" ] || exit 1
    mkdir -p ${D}/www/pages/${PN} || exit 1
    cd ${S} || exit 1
    tar --no-same-owner -cpf - . \
        | tar --no-same-owner -xpf - -C ${D}/www/pages/${PN}
    cp ${WORKDIR}/filemanager.config.js ${D}/www/pages/${PN}/scripts/
}

FILES_${PN} = "/"
