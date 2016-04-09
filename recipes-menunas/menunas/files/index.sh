#!/bin/sh
FILES=`find @DOWNLOAD_DIR@ -type f`
FILE_VIDEO=""
START_PATTERN=""
OUTPUT_FILE=index.html
file=""
ret=0
for file in $FILES;do 

    type=$(file -b --mime-type $file);
	echo $type | grep "video/";
	if [[ $? -eq 0 ]];then
	FILE_VIDEO+="$file:$type "
	fi
done

if [[ "" == $FILE_VIDEO ]]; then
	cat <<EOF 
<!DOCTYPE html>
<html>
    <head>
        <!-- En-tête de la page -->
        <meta charset="utf-8" />
        <title>Pas de contenu multimédia détecté</title>
    </head>

    <body>
    </body>
</html>
EOF
else
for video in $FILE_VIDEO;do 
	source=$(echo $video | sed s/:.*//g)
	type=$(echo $video | sed s/.*://g)
	echo '<video width="400" height="400" controls >' >> $OUTPUT_FILES
	echo "<source src=\"$START_PATTERN/$source\" type=\"$type\">"  >> $OUTPUT_FILES
	echo '</video>'  >> $OUTPUT_FILES

done

fi
