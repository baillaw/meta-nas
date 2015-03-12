SUMMARY = "FlexGet is a multipurpose automation tool for content like torrents, nzbs, podcasts, comics, series, movies, etc"
HOMEPAGE = "http://flexget.com"
SECTION = "devel/python"
LICENSE = "MIT"
SRCNAME = "Flexget"

inherit bin_package

RDEPENDS_${PN} += "python-pip"
