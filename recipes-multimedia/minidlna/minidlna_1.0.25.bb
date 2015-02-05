DESCRIPTION = "MiniDLNA (aka ReadyDLNA) is server software with the aim of \
being fully compliant with DLNA/UPnP-AV clients."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENCE;md5=b1a795ac1a06805cf8fd74920bc46b5c"

DEPENDS = "flac libav jpeg sqlite3 libexif libogg libid3tag libvorbis"

SRC_URI = "${SOURCEFORGE_MIRROR}/${PN}/${PN}_${PV}_src.tar.gz \
    file://search-for-headers-in-staging-dir.patch \
    file://fix-makefile.patch \
"

SRC_URI[md5sum] = "d966256baf2f9b068b9de871ab5dade5"
SRC_URI[sha256sum] = "170560fbe042c2bbcba78c5f15b54f4fac321ff770490b23b55789be463f2851"

SRC_URI =+ "${@base_contains('DISTRO_FEATURES', 'systemd', 'file://minidlna-daemon.service', 'file://minidlna-daemon.init.d', d)}"

export STAGING_DIR_HOST

inherit autotools update-rc.d

B = "${S}"
do_configure_prepend(){
	cd ${S}
	./genconfig.sh
}

do_install_initd() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/minidlna-daemon.init.d ${D}${sysconfdir}/init.d/minidlna
}

do_install_systemd() {
	install -d ${D}${nonarch_base_libdir}/systemd/system
	install -m 0755 ${WORKDIR}/minidlna-daemon.service ${D}${nonarch_base_libdir}/systemd/system
}

do_install_append(){
	install -d ${D}${sysconfdir}
	install -m 0755 minidlna.conf ${D}${sysconfdir}
	${@base_contains('DISTRO_FEATURES', 'systemd', 'do_install_systemd', 'do_install_initd', d)}
}


INITSCRIPT_NAME = "minidlna"
INITSCRIPT_PARAMS = "defaults 90"
