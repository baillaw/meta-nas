SUMMARY = "FlexGet is a multipurpose automation tool for content like torrents, nzbs, podcasts, comics, series, movies, etc"
HOMEPAGE = "http://flexget.com"
SECTION = "devel/python"
LICENSE = "MIT"
SRCNAME = "Flexget"
inherit  pip_package

SRC_URI = "git://github.com/Flexget/Flexget.git;branch=master"
S = "${WORKDIR}/git"

DIRECTORY_PYTHON_FILES = "flexget"
export DIRECTORY_PYTHON_FILES
SRCREV = "master"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d629068a8097b3b584e48e30573a6971"

