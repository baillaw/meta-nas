DESCRIPTION = "MiniDLNA (aka ReadyDLNA) is server software with the aim of \
being fully compliant with DLNA/UPnP-AV clients."
LICENSE = "GPL-2.0|BSD"
DEPENDS = "virtual/gettext ffmpeg flac libav jpeg sqlite3 libexif libogg libid3tag libvorbis"

inherit autotools update-rc.d systemd

SRC_URI = "git://git.code.sf.net/p/minidlna/git;branch=master;module=git"
SRC_URI =+ "${@base_contains('DISTRO_FEATURES', 'systemd', 'file://minidlna-daemon.service', 'file://minidlna-daemon.init.d', d)}"

S = "${WORKDIR}/git"
B = "${S}"

do_configure_prepend() {
cd ${S}
./autogen.sh
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

SYSTEMD_SERVICE_${PN} = "minidlna-daemon.service"

INITSCRIPT_NAME = "minidlna"
INITSCRIPT_PARAMS = "defaults 90"

