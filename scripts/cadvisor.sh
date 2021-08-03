#!/usr/bin/env bash
set -eo pipefail

docker --version || sudo su

docker rm -f cadvisor
docker run \
  --name=cadvisor \
  --volume=/:/rootfs:ro \
  --volume=/var/run:/var/run:ro \
  --volume=/sys:/sys:ro \
  --volume=/var/lib/docker/:/var/lib/docker:ro \
  --volume=/dev/disk/:/dev/disk:ro \
  --publish=127.0.0.1:8080:8080 \
  --device=/dev/kmsg \
  --privileged \
  --detach=true \
  gcr.io/cadvisor/cadvisor:latest
