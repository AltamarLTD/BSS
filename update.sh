#!/bin/sh


if [ -z "$1" ]
then
echo "ERROR : Version is not set. Use './update.sh {version}' like './update.sh 0.1'"
exit 0
fi

echo ----------------------------------------------
echo Version is $1
echo ----------------------------------------------

echo remove altamar_container

docker rm -f altamar_container

echo ----------------------------------------------
echo removing image
echo ----------------------------------------------

docker rmi altamar_image:$1

echo ----------------------------------------------
echo git pull
echo ----------------------------------------------

git pull

echo ----------------------------------------------
echo Building project
echo ----------------------------------------------

mvn clean install

echo ----------------------------------------------
echo Up
echo ----------------------------------------------

docker-compose up

