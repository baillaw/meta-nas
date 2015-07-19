# meta-nas
# Description

Meta-nas (OpenEmbedded / Yocto Meta) provides mainly these following packages
* A torrent client : Transmission (www.transmissionbt.com/)
* A web file-explorer: Filemanager (https://github.com/simogeo/Filemanager)
* A ssh guardian : fail2ban (https://github.com/fail2ban/fail2ban)
* A DLNA sever : miniDlna

Theses services are avaibles throught WEB Interface

# Screenshots
* HomePage
![HOMEPAGE](https://raw.githubusercontent.com/baillaw/meta-nas/master/screenshots/Homepage.jpg)

* FileMnager
![FILEMANAGER](https://raw.githubusercontent.com/baillaw/meta-nas/master/screenshots/FilesManager.jpg)

* DLNA Server
![DLNA Server status ](https://raw.githubusercontent.com/baillaw/meta-nas/master/screenshots/DLNA.jpg)

* Transmission
![TRANSMISSION](https://raw.githubusercontent.com/baillaw/meta-nas/master/screenshots/Transmission.jpg)

# How to test meta-nas
The easy way to test the meta-nas is to use the poky distribution.

A good point to start yocto :
 http://www.yoctoproject.org/docs/current/ref-manual/ref-manual.html

Note:
```text
> WORKINGDIR="~/WORKING"
> mkdir -p ${WORKINGDIR}
> cd ${WORKINGDIR}
```
## get poky, clone with Git (preferred)
* from fido version
```text
> git clone -b fido git://git.yoctoproject.org/poky.git
```

* from master
```text
> git clone git://git.yoctoproject.org/poky.git
```
For more information:
 https://www.yoctoproject.org/downloads
 
## get meta-oe, clone with Git (preferred)

* from fido version
```text
> git clone -b fido git://git.openembedded.org/meta-openembedded
```

* from master
```text
> git clone git://git.openembedded.org/meta-openembedded
```

## get meta-nas
```text
> git clone git@github.com:baillaw/meta-nas.git
```

## init your project
```text
> cd poky
> . oe-init-build-env build-nas
```
## add your meta
```text
>  bitbake-layers add-layer ../../meta-nas
>  bitbake-layers add-layer ../../meta-openembedded/meta-oe

```

## add your meta-nas
add LICENSE_FLAGS_WHITELIST
```text
> echo "LICENSE_FLAGS_WHITELIST += \"commercial\"" >> conf/local.conf
```


## Build your image
```text
> bitbake core-image-nas 
```
## Test your image
On a terminal
```text
> runqemu qemux86 core-image-nas
```

On a web navigator such as firefox, go to

http://192.168.7.2 and enjoy.

192.168.7.2 should be the address given to qemu machine by default

You should see a HTML Menu with file manager & Transmission (Torrent Client)

## User / Password for web interface

Default user and password are : 
```text
meta-nas / meta-nas
```

You could change them by using change_webpass.sh script
```text
change_webpass.sh
```

## Troubleshooting :

OS doesn't start automatically, blocked in grub menu.

Add theses lines in /boot/grub/grub.cfg
```text 
set default="0"
set timeout="10"
```
