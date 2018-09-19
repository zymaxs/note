#!/bin/bash
i=1
for arg in "$@"
do
    if [ "$i" -ge "9" ];then
        tmp=${tmp}${arg}
    fi
    i=$[i+1]
done
echo $tmp
