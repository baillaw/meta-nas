DESCRIPTION = "MiniDLNA (aka ReadyDLNA) is server software with the aim of \
being fully compliant with DLNA/UPnP-AV clients."
LICENSE = "GPL-2.0|BSD"
DEPENDS = "virtual/gettext ffmpeg flac libav jpeg sqlite3 libexif libogg libid3tag libvorbis"

inherit autotools-brokensep update-rc.d systemd

SRC_URI = "git://git.code.sf.net/p/minidlna/git;branch=master;module=git \
            file://minidlna-daemon.init.d"
SRC_URI =+ "${@base_contains('DISTRO_FEATURES', 'systemd', 'file://minidlna.service', '', d)}"

S = "${WORKDIR}/git"

# This remove "--exclude=autopoint" option from autoreconf argument to avoid 
# configure.ac:30: error: required file './ABOUT-NLS' not found
EXTRA_AUTORECONF = ""

do_install_initd() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/minidlna-daemon.init.d ${D}${sysconfdir}/init.d/minidlna
}

do_install_systemd() {
	install -d ${D}${nonarch_base_libdir}/systemd/system
	install -m 0755 ${WORKDIR}/minidlna.service ${D}${nonarch_base_libdir}/systemd/system
}

do_install_append(){
	install -d ${D}${sysconfdir}
	install -m 0755 minidlna.conf ${D}${sysconfdir}
	do_install_initd
	${@base_contains('DISTRO_FEATURES', 'systemd', 'do_install_systemd', '', d)}
}

SYSTEMD_SERVICE_${PN} = "minidlna.service"

INITSCRIPT_NAME = "minidlna"
INITSCRIPT_PARAMS = "defaults 90"

