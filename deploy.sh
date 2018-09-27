#!/bin/bash

# 部署后台
cd KnowledgeBase
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
nohup mvn spring-boot:run -Pprod &

# 部署前台，下方目录需要根据实际情况进行修改
cd /home/yjsyTest/tomcat/tomcat1/webapps/knowledge-base-ui
git stash
git pull -f origin master
npm run-script clean
npm run-script bower-install
npm run-script dev
cd build/common
mv config_prob.js config.js
