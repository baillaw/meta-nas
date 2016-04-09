DESCRIPTION = "A web Nas Menu"
SECTION = "network"
HOMEPAGE = "https://github.com/baillaw/meta-nas"

RDEPENDS_${PN} = "transmission-web filemanager minidlna "

SRC_URI = "file://index.php "
SRC_URI_rasberrypi = "file://index.php_raspberrypi "
SRC_URI_rasberrypi2 = "file://index.php_raspberrypi "
SRC_URI += "file://index.sh "
SRC_URI += "file://index.html "
SRC_URI += "file://COPYING"

SRC_URI[md5sum] = "665547ad10bd4aeda5f36fd23558fd8b"
SRC_URI[sha256sum] = "03057cf948d9a63ed832931bd0dbaf692889a9da51015ec7c294451643090942"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

S = "${WORKDIR}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=6b2a2e44797724bf46ea769edb0f54a7"

do_compile[noexec] = "1"
do_configure[noexec] = "1"

do_install () {
    # Do it carefully
    [ -d "${S}" ] || exit 1
    mkdir -p ${D}/www/pages/mutimedia_player || exit 1
# As php-cgi binary is now broken
#    cp ${WORKDIR}/"${@bb.utils.contains_any('MACHINE', [ 'rasberrypi2', 'rasberrypi' ], 'index.php_raspberrypi', 'index.php', d)}" ${D}/www/pages/index.php
# mv to index.html
    cp ${WORKDIR}/index.html ${D}/www/pages/index.html
    install -m 0755 ${WORKDIR}/index.sh ${D}/www/pages/mutimedia_player/index.sh
#     sed -i -e 's|@DOWNLOAD_DIR@|${DOWNLOAD_DIR_TRANSMISSION}|' ${D}/www/pages/index.php
    sed -i -e 's|@DOWNLOAD_DIR@|${DOWNLOAD_DIR_TRANSMISSION}|' ${D}/www/pages/index.html
    sed -i -e 's|@DOWNLOAD_DIR@|${DOWNLOAD_DIR_TRANSMISSION}|' ${D}/www/pages/mutimedia_player/index.sh

}

FILES_${PN} = "/"
