dockid=`docker ps|grep $1|grep bin|awk '{ print $1 }'`
pid=`docker exec -it $dockid ps auxww|grep java|grep -v grep|awk '{ print $2 }'`
docker exec -it $dockid jstack $pid > /usr/local/log/$1/jstack.log