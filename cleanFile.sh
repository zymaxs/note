#!/bin/bash
#清理指定目录指定日期30天内0~10点的指定后缀文件
start_date="2019-02-01"
 
for i in `seq 0 30`
do
    num=$((${i}+1))
    datatime=`date '+%Y-%m-%d' -d ${start_date}'+'${num}' day'`
    
    echo ${datatime}
    find /data/data1/data/ -name *.jpg -newermt ${datatime}' 00:00' ! -newermt ${datatime}' 10:00' |xargs rm &

done