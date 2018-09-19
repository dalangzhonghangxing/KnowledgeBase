#!/bin/bash


PID=$(lsof -i:16666)
echo "当前进程端口的进程号是 $PID"

if [ -n "$PID" ]; then
    kill -9 ${PID}
fi
echo "kill success"

git stash
git pull -f origin master
mvn clean
mvn install
nohup mvn spring-boot:run &
