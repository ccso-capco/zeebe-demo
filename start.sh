#!/usr/bin/env bash

SERVICES_REQUESTED=${1}

for d in ${SERVICES_REQUESTED:-services/*/}; do
    echo "Gradle build $d"
    gradle clean build --build-file "$d"/build.gradle
done

docker-compose up --build --detach ${SERVICES_REQUESTED}