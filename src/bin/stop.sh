#!/bin/sh
APP_NAME=${run.app}

tpid=`ps -ef|grep $APP_NAME|grep -v grep`
if [ -z "$tpid" ]; then
    echo 'Stop Success!'
fi

tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|grep -v deploy_run|grep -v deploy_task|grep -v stop|awk '{print $2}'`
if [ -n "$tpid" ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 2
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|grep -v deploy_run|grep -v deploy_task|grep -v stop|awk '{print $2}'`
if [ -n "$tpid" ]; then
    echo 'Kill Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi
exit