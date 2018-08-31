ls /usr/local/nginx/vhost/* >> /tmp/nginx123.txt
check=`cat /tmp/nginx123.txt | grep $1`

if [ -z "$check" ]; then
        echo "这是个新域名"
else
        echo "有相同域名了"
        rm -rf /tmp/nginx123.txt
        exit
fi

echo $1 >> /tmp/linshiyuming.txt
xname=`sed  "s/\.//g" /tmp/linshiyuming.txt | sed "s/com//g"`
newfuzai=`sed "s/\./_/g" /tmp/linshiyuming.txt | sed "s/_com//g"`

cp /update/nginx_moban.conf /usr/local/nginx/vhost/$1.conf
echo "server $2:$3 weight=1 max_fails=2 fail_timeout=10000;" >> /tmp/linshiip.txt
sed -i "s/^/\t/g" /tmp/linshiip.txt
###sed  conf
cd /usr/local/nginx/vhost
sed -i '/serveripxx/r /tmp/linshiip.txt' $1.conf
sed -i "s/uname/$xname/g" $1.conf
sed -i "s/fuzai/$newfuzai/g" $1.conf
sed -i "s/yuming/$1/g" $1.conf
sed -i 's/serveripxx//g' $1.conf

rm -rf /tmp/nginx123.txt
rm -rf /tmp/linshiyuming.txt
rm -rf /tmp/linshiip.txt

/usr/local/nginx/sbin/nginx -s reload
