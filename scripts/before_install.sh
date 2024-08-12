#!/bin/bash

echo "BeforeInstall 스크립트 시작" > /home/ec2-user/before_install.log

# 홈 디렉토리로 이동
cd /home/ec2-user

# 기존 zip 파일이 있는 경우 삭제
if [ -f "/home/ec2-user/backend.zip" ]; then
    echo "기존 backend.zip 파일 삭제" >> /home/ec2-user/before_install.log
    rm /home/ec2-user/backend.zip
fi

# S3에서 zip 파일 다운로드
echo "S3에서 backend.zip 다운로드" >> /home/ec2-user/before_install.log
aws s3 cp s3://groom-runner-bucket/backend/backend.zip /home/ec2-user/backend.zip >> /home/ec2-user/before_install.log 2>&1

# zip 파일이 존재하는지 확인
if [ -f "/home/ec2-user/backend.zip" ]; then
    echo "다운로드 후 backend.zip 파일 존재" >> /home/ec2-user/before_install.log
else
    echo "다운로드 후 backend.zip 파일이 존재하지 않음" >> /home/ec2-user/before_install.log
    exit 1
fi

echo "BeforeInstall 스크립트 완료" >> /home/ec2-user/before_install.log