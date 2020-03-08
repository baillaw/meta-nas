DESCRIPTION = "A web file-explorer"
SECTION = "network"
HOMEPAGE = "https://github.com/simogeo/Filemanager"

RDEPENDS_${PN} = "lighttpd php-cgi python bash"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/simogeo/${BPN}.git"
SRC_URI += "file://0001-Move-fromPython2.6toPython.patch"


SRC_URI += "file://filemanager.config.js"


do_compile[noexec] = "1"

do_configure () {
	sed -i -e "s:@FILE_PATH@:${DOWNLOAD_DIR_TRANSMISSION}:" ${WORKDIR}/filemanager.config.js
}

do_install () {
    # Do it carefully
    [ -d "${S}" ] || exit 1
    [ -d "${D}/www/pages/${PN}" ] || rm -rf ${D}/www/pages/${PN}
    mkdir -p ${D}/www/pages/${PN} || exit 1
    cd ${S} || exit 1
    cp -r $(ls  | grep -v '.git')  ${D}/www/pages/${PN}
    cp ${WORKDIR}/filemanager.config.js ${D}/www/pages/${PN}/scripts/
}

FILES_${PN} = "/"
