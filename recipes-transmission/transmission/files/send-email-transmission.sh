#!/bin/sh

IP=`wget -qO- http://checkip.dyndns.org/ | sed -e 's/.*Current IP Address: //' -e 's/<.*$//'`
TMPFILE=`mktemp -t transmission.XXXXXXXXXX`
SMTP="smtp.gmail.com"
FROM="transmission@localhost.localdomain"
FROMNAME="Your Yocto NAS"
TO="XXX.YYYY@gmail.com"

echo "Subject: Transmission finished downloading \"$TR_TORRENT_NAME\" on $TR_TIME_LOCALTIME" > $TMPFILE
echo "From: \"$FROMNAME\"<$FROM>" >> $TMPFILE
echo "To: <$TO>" >> $TMPFILE
echo "Date: `date -R`" >> $TMPFILE
echo "FILEMANAGER-> http://$IP/filemanager" >> $TMPFILE
echo "Your friendly  NAS." >>$TMPFILE
echo "" >> $TMPFILE
 
cat $TMPFILE | /usr/sbin/sendmail -S"$SMTP" -f"$FROM" $TO
rm -f $TMPFILE
