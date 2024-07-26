#!/bin/bash

echo "Start server 스크립트 시작" > /home/ec2-user/start_server.log

# 환경 변수 로드
source /etc/environment

# 홈 디렉토리로 이동
cd /home/ec2-user

# zip 파일이 존재하는지 확인
if [ -f "/home/ec2-user/backend.zip" ]; then
    echo "backend.zip 압축 해제" >> /home/ec2-user/start_server.log
    unzip -o /home/ec2-user/backend.zip -d /home/ec2-user/ >> /home/ec2-user/start_server.log 2>&1
else
    echo "backend.zip 파일이 존재하지 않음" >> /home/ec2-user/start_server.log
    exit 1
fi

# build/libs 디렉토리로 이동
if [ -d "/home/ec2-user/build/libs" ]; then
    cd /home/ec2-user/build/libs
else
    echo "build/libs 디렉토리가 존재하지 않음" >> /home/ec2-user/start_server.log
    exit 1
fi

echo "> 🟢 새로운 스프링 서비스 실행" >> /home/ec2-user/start_server.log

# JAR 파일이 존재하는지 확인
JAR_NAME=$(ls | grep 'backend-0.0.1-SNAPSHOT.jar')
if [ -z "$JAR_NAME" ]; then
    echo "build/libs 디렉토리에 JAR 파일이 없음" >> /home/ec2-user/start_server.log
    exit 1
fi

# 기존 Java 프로세스 종료
CURRENT_PID=$(pgrep -f $JAR_NAME)
if [ -n "$CURRENT_PID" ]; then
    echo "기존 프로세스 종료: $CURRENT_PID" >> /home/ec2-user/start_server.log
    kill -9 $CURRENT_PID
    sleep 5
fi

# 추가 로그
echo "발견된 JAR 파일: $JAR_NAME" >> /home/ec2-user/start_server.log
echo "실행 명령: nohup java -jar /home/ec2-user/build/libs/$JAR_NAME" >> /home/ec2-user/start_server.log

# 애플리케이션 시작
nohup java -jar /home/ec2-user/build/libs/$JAR_NAME > /home/ec2-user/application.log 2> /home/ec2-user/error.log &

# 새로운 프로세스 ID 캡처
NEW_PID=$(pgrep -f $JAR_NAME)
echo "새로운 프로세스 ID: $NEW_PID" >> /home/ec2-user/start_server.log

echo "Start server 스크립트 완료" >> /home/ec2-user/start_server.log