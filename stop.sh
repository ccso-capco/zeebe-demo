#!/usr/bin/env bash

for i in "$@"
do
case ${i} in
    --remove-volumes)
    REMOVE_VOLUMES=YES
    shift
    ;;
    *)
    # unknown option
    ;;
esac
done

docker-compose down

if [ "${REMOVE_VOLUMES}" == "YES" ]; then
    docker volume rm zeebe_data
    docker volume rm zeebe_elasticsearch_data
fi