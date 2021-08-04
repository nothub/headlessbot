#!/usr/bin/env bash
set -eo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"/../docker

cp ../build/libs/headlessbot-*.jar mc/mods/

docker --version || sudo su

docker rmi -f mc-headless:dev
docker build . -t mc-headless:dev
docker run -it --rm -v "${PWD}"/headless.json:/opt/app/headless.json mc-headless:dev
