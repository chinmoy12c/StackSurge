#!/bin/bash

while getopts c:v:t: flag
do
    case "${flag}" in
        c) container=${OPTARG};;
        v) volume=${OPTARG};;
        t) tag=${OPTARG};;
    esac
done

docker run --detach --volume=$volume:/home/student/studentData --net=mystack --name=$tag $container