#!/bin/bash

while getopts i:c: flag
do
    case "${flag}" in
        i) instance=${OPTARG};;
        c) caddy=${OPTARG};;
    esac
done

docker stop $instance $caddy