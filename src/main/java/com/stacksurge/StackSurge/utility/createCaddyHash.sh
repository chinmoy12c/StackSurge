#!/bin/bash

while getopts c:p: flag
do
    case "${flag}" in
        c) container=${OPTARG};;
        p) passwd=${OPTARG};;
    esac
done

docker exec $container caddy hash-password -plaintext $passwd