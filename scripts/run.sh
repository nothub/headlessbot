#!/usr/bin/env bash
set -eo pipefail

MC_PATH=~/.minecraft
FORGE_VER=1.12.2-forge-14.23.5.2854

cd "$(dirname "${BASH_SOURCE[0]}")"/..
./gradlew clean build --stacktrace

cd docker
if [ ! -d mc ]; then
  # copy mc launcher files
  mkdir -p mc
  cp -R $MC_PATH/launcher mc/
  cp -R $MC_PATH/libraries mc/
  mkdir -p mc/versions/$FORGE_VER && cp -R $MC_PATH/versions/$FORGE_VER mc/versions/
  cp -R $MC_PATH/versions/version_manifest_v2.json mc/versions/
  # client options
  {
    echo "lang:en_us"
    echo "autoJump:false"
    echo "renderDistance:6"
    echo "pauseOnLostFocus:false"
  } >mc/options.txt
  # download mods
  mkdir -p mc/mods
  curl -L https://github.com/3arthqu4ke/HeadlessForge/releases/download/1.1/headlessforge-1.1.jar -o mc/mods/headlessforge-1.1.jar
  curl -L https://github.com/cabaletta/baritone/releases/download/v1.2.15/baritone-api-forge-1.2.15.jar -o mc/mods/baritone-api-forge-1.2.15.jar
  md5sum -c mods.md5
fi

cp ../build/libs/headlessbot-*.jar mc/mods/

# use rootless if possible
docker --version || sudo su

docker rmi -f mc-headless:dev
docker build . -t mc-headless:dev
docker run -it --rm -v "${PWD}"/headless.json:/opt/app/headless.json mc-headless:dev
