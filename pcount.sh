#! /bin/bash
ps -ef|grep java |awk '{print $2}'>temp1
ps -ef|grep java |awk '{print $2}'|xargs ps hH p >temp2
for pid in `cat temp1`
do
    pline=`cat temp2|grep $pid|wc -l`
    echo $pid $pline
done
