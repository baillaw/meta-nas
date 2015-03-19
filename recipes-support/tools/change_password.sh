#!/bin/sh

echo "The character | is forbiden.Please Do not use it!"
echo -n "Set User Name :" 
read USR
echo -n "Set Password:"
read PASS


echo "updating lighttpd.conf"

sed -i -e 's|"require" => "user=.*|"require" => "user='$USR'"|g' /etc/lighttpd.conf
sed -i -e 's|.*:|'$USR':"|g' /etc/lighttpd.users
sed -i -e 's|:.*|:'$PASS'|g' /etc/lighttpd.users                                    


pkill -9 transmission
sed -i -e 's|"rpc-username": ".*",|"rpc-username": "'$USR'",|'  /var/lib/transmission-daemon/settings.json
sed -i -e 's|"rpc-password": ".*",|"rpc-password": "'$PASS'",|' /var/lib/transmission-daemon/settings.json
