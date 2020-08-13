#!/bin/bash

for file in $1*
do
    xpath=${file%/*}
    xbase=${file##*/}
    xfext=${xbase##*.}
    xpref=${xbase%.*}
    
    iconv -f WINDOWS-1252 -t UTF-8 $file > $2"$xpref.$xfext"
done
