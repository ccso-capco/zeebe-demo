#!/usr/bin/env bash

SERVICES_REQUESTED=$1

for d in ${SERVICES_REQUESTED:-services/*/}; do
  # get directory name only
  d=$(basename $d)

  echo "Gradle build $d"
  gradle clean build --build-file services/$d/build.gradle
done

docker-compose up --build --detach $SERVICES_REQUESTED