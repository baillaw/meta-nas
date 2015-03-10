SUMMARY = "PIP is a tool for installing and managing Python packages"
LICENSE = "MIT & LGPL-2.1"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=45665b53032c02b35e29ddab8e61fa91"

SRC_URI = "https://pypi.python.org/packages/source/p/pip/pip-${PV}.tar.gz"

SRC_URI[md5sum] = "2332e6f97e75ded3bddde0ced01dbda3"
SRC_URI[sha256sum] = "0d58487a1b7f5be2e5e965c11afbea1dc44ecec8069de03491a4d0d6c85f4551"

S = "${WORKDIR}/pip-${PV}"

inherit setuptools

# Since PIP is like CPAN for PERL we need to drag in all python modules to ensure everything works
RDEPENDS_${PN} = "python-modules python-distribute"
