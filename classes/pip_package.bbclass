#
# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# Common variable and task for the binary package recipe.
# Basic principle:
# * The files have been unpacked to ${S} by base.bbclass
# * Skip do_configure and do_compile
# * Use do_install to install the files to ${D}
#
# Note:
# The "subdir" parameter in the SRC_URI is useful when the input package
# is rpm, ipk, deb and so on, for example:
#
# SRC_URI = "http://foo.com/foo-1.0-r1.i586.rpm;subdir=foo-1.0"
#
# Then the files would be unpacked to ${WORKDIR}/foo-1.0, otherwise
# they would be in ${WORKDIR}.
#
inherit  distutils-base

DISTUTILS_INSTALL_ARGS = " --user"

DEPENDS += " python-pip-native python-distribute "
RDEPENDS_${PN} += " python-distribute "
# Skip the unwanted steps
do_configure[noexec] = "1"
do_compile[noexec] = "1"
DIRECTORIES_PYTHON_FILES_COPY ?= "${PN}"
pip_package_do_install() {
        
        if test -e  ${S}/tmp-build; then
          rm -rf ${S}/tmp-build/*
        else
          mkdir ${S}/tmp-build/
        fi
        if ! test -e  ${STAGING_DIR_NATIVE}${PYTHON_SITEPACKAGES_DIR}/easy-install.pth; then
          touch ${STAGING_DIR_NATIVE}${PYTHON_SITEPACKAGES_DIR}/easy-install.pth
        fi
        
        install -d ${D}${PYTHON_SITEPACKAGES_DIR}
        install -d ${D}/tmp
        STAGING_INCDIR=${STAGING_INCDIR} \
        STAGING_LIBDIR=${STAGING_LIBDIR} \
        PYTHONPATH="${D}${PYTHON_SITEPACKAGES_DIR}" \
        PYTHONUSERBASE=${D}/tmp \
        BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
        ${STAGING_BINDIR_NATIVE}/pip install paver ${DISTUTILS_INSTALL_ARGS}

        STAGING_INCDIR=${STAGING_INCDIR} \
        STAGING_LIBDIR=${STAGING_LIBDIR} \
        PYTHONPATH="${D}${PYTHON_SITEPACKAGES_DIR}" \
        BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
        PYTHONUSERBASE=${D}/tmp \
        ${STAGING_BINDIR_NATIVE}/pip install -e ${S}  ${DISTUTILS_INSTALL_ARGS} || \
        bbfatal "${STAGING_BINDIR_NATIVE}/pip install -e ${S}  ${DISTUTILS_INSTALL_ARGS}  execution failed."

        cp -r   ${S}/*.egg-info   ${D}${PYTHON_SITEPACKAGES_DIR}/
        cp -r   ${DIRECTORY_PYTHON_FILES} ${D}${PYTHON_SITEPACKAGES_DIR}/
        
        # support filenames with *spaces*
        # only modify file if it contains path to avoid recompilation on the target
        find ${D} -name "*.py" -exec grep -q ${D} {} \; -exec sed -i -e s:${D}::g {} \;

        if test -e ${D}${bindir} ; then	
            for i in ${D}${bindir}/* ; do \
                if [ ${PN} != "${BPN}-native" ]; then
                	sed -i -e s:${STAGING_BINDIR_NATIVE}/python-native/python:${bindir}/env\ python:g $i
		fi
                sed -i -e s:${STAGING_BINDIR_NATIVE}:${bindir}:g $i
            done
        fi

        if test -e ${D}${sbindir}; then
            for i in ${D}${sbindir}/* ; do \
                if [ ${PN} != "${BPN}-native" ]; then
                	sed -i -e s:${STAGING_BINDIR_NATIVE}/python-native/python:${bindir}/env\ python:g $i
		fi
                sed -i -e s:${STAGING_BINDIR_NATIVE}:${bindir}:g $i
            done
        fi

        rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/easy-install.pth
        rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/site.py*
        rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/*.egg-link
        
        #
        # FIXME: Bandaid against wrong datadir computation
        #
        if test -e ${D}${datadir}/share; then
            mv -f ${D}${datadir}/share/* ${D}${datadir}/
            rmdir ${D}${datadir}/share
        fi
}




EXPORT_FUNCTIONS do_install
