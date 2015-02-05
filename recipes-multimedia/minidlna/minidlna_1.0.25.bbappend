
#change the media-dir of minidlna config file :
do_install_append() {
	sed -i -e "s:media_dir=/opt:media_dir=${DOWNLOAD_DIR_TRANSMISSION}:" ${D}${sysconfdir}/minidlna.conf
}
