#!/bin/sh

# The purpose of this script is to change user and password for web access to nas.

if [ "$(id -u)" != "0" ]; then
  echo "Please execute it as root user"
  exit -1
fi
if [ -e /etc/lighttpd.conf -o -e /var/lib/transmission-daemon/settings.json ]; then
  echo "The character | is forbiden.Please Do not use it!"
  echo -n "Set User Name :" 
  read USR
  if [ "x$USR" = "x" ]; then
    echo "User is not set. Exiting..."
    exit -1
  fi

  echo -n "Set Password:"
  read PASS
  if [ "x$PASS" = "x" ]; then
    echo "Password is not set. Exiting..."
    exit -1
  fi

  if [ -e /etc/lighttpd.conf ];then
    echo "updating lighttpd.conf"
    sed -i -e 's|"require" => "user=.*|"require" => "user='$USR'"|g' /etc/lighttpd.conf
    sed -i -e 's|.*:|'$USR':"|g' /etc/lighttpd.users
    sed -i -e 's|:.*|:'$PASS'|g' /etc/lighttpd.users
  fi

  if [ -e /var/lib/transmission-daemon/settings.json ]; then
    echo "Updating transmission configuration..."
    pkill -9 transmission
    sed -i -e 's|"rpc-username": ".*",|"rpc-username": "'$USR'",|'  /var/lib/transmission-daemon/settings.json
    sed -i -e 's|"rpc-password": ".*",|"rpc-password": "'$PASS'",|' /var/lib/transmission-daemon/settings.json
  fi
  echo "Please reboot your target, to make sure changements take effects..."
else
  echo "Neither lighttpd config file nor Transmission configuration file are found!"
fi
