# Copyright (C) 2012-2018 O.S. Systems Software LTDA.
# Copyright (C) 2012-2016 Freescale Semiconductor
# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)
DESCRIPTION = "Freescale Multimedia parser libs"
LICENSE = "Proprietary"
SECTION = "multimedia"
LIC_FILES_CHKSUM = "file://COPYING;md5=5ab1a30d0cd181e3408077727ea5a2db"

# For backwards compatibility
PROVIDES += "libfslparser"
RREPLACES_${PN} = "libfslparser"
RPROVIDES_${PN} = "libfslparser"
RCONFLICTS_${PN} = "libfslparser"

SRC_URI = "${FSL_MIRROR}/${PN}-${PV}.bin;fsl-eula=true"
SRC_URI[md5sum] = "0e425e143900f980c6fe7f735a679829"
SRC_URI[sha256sum] = "21d1066dcdc6f0eb7efc56910ee9b116883456a013837506a0632153baedcbf3"

inherit fsl-eula-unpack autotools pkgconfig

# Choose between 32-bit and 64-bit binaries and between Soft Float-Point and Hard Float-Point
EXTRA_OECONF = "${@bb.utils.contains('TUNE_FEATURES', 'aarch64', '--enable-armv8', \
                  bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', '--enable-fhw', '--enable-fsw', d), d)}"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

python __set_insane_skip() {
    # FIXME: All binaries lack GNU_HASH in elf binary but as we don't have
    # the source we cannot fix it. Disable the insane check for now.
    # FIXME: gst-fsl-plugin looks for the .so files so we need to deploy those
    for p in d.getVar('PACKAGES').split():
        d.setVar("INSANE_SKIP_%s" % p, "ldflags dev-so textrel")
}

do_package_qa[prefuncs] += "__set_insane_skip"

# FIXME: gst-fsl-plugin looks for the .so files so we need to deploy those
FILES_${PN} += "${libdir}/imx-mm/*/*${SOLIBS} ${libdir}/imx-mm/*/*${SOLIBSDEV}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_SYSROOT_STRIP = "1"

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"
