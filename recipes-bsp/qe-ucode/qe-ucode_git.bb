DESCRIPTION = "qe microcode binary"
SECTION = "qe-ucode"
LICENSE = "NXP-Binary-EULA"
LIC_FILES_CHKSUM = "file://NXP-Binary-EULA;md5=c62f8109b4df15ca37ceeb5e4943626c"

inherit deploy fsl-eula-unpack

SRC_URI = "git://github.com/NXP/qoriq-qe-ucode.git;fsl-eula=true;nobranch=1"
SRCREV= "57401f6dff6507055558eaa6838116baa8a2fd46"

S = "${WORKDIR}/git"

python () {
    if not d.getVar("QE_UCODE"):
        PN = d.getVar("PN")
        FILE = os.path.basename(d.getVar("FILE"))
        bb.debug(1, "To build %s, see %s for instructions on \
                     setting up your qe-ucode" % (PN, FILE))
        raise bb.parse.SkipRecipe("because QE_UCODE is not set")
}

do_install () {
       install -d ${D}/boot
       install -m 644 ${B}/${QE_UCODE} ${D}/boot
}

do_deploy () {
       install -d ${DEPLOYDIR}/boot
       install -m 644 ${B}/${QE_UCODE} ${DEPLOYDIR}/boot
}
addtask deploy before do_build after do_install

PACKAGES += "${PN}-image"
FILES_${PN}-image += "/boot/*"

COMPATIBLE_MACHINE = "(ls1021a|ls1043a|t1042|t1024)"
PACKAGE_ARCH = "${MACHINE_SOCARCH}"

