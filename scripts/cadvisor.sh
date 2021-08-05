#!/usr/bin/env bash
# cAdvisor does not support rootless!

set -eo pipefail

sudo docker rm -f cadvisor
sudo docker run \
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
