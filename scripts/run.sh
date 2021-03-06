#!/usr/bin/env bash
set -eo pipefail

MC_PATH=~/.minecraft
FORGE_VER=1.12.2-forge-14.23.5.2854
URL_HEADLESSFORGE=https://github.com/3arthqu4ke/HeadlessForge/releases/download/1.1/headlessforge-1.1.jar
URL_BARITONE=https://github.com/cabaletta/baritone/releases/download/v1.2.15/baritone-api-forge-1.2.15.jar

if [ ! -d $MC_PATH ]; then
  echo "Minecraft launcher is missing at path: $MC_PATH"
  exit 1
fi
if [ ! -d $MC_PATH/versions/$FORGE_VER ]; then
  echo "Forge is missing at path: $MC_PATH/versions/$FORGE_VER"
  echo "Get it from: https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html"
  exit 1
fi

cd "$(dirname "${BASH_SOURCE[0]}")"/..
./gradlew clean jar --stacktrace

if [ ! -d build/docker ]; then
  # copy mc launcher files
  mkdir -p build/docker/versions/$FORGE_VER
  cp -R $MC_PATH/launcher build/docker/
  cp -R $MC_PATH/libraries build/docker/
  cp -R $MC_PATH/versions/$FORGE_VER build/docker/versions/
  cp -R $MC_PATH/versions/version_manifest_v2.json build/docker/versions/
  # client options
  {
    echo "lang:en_us"
    echo "autoJump:false"
    echo "renderDistance:6"
    echo "pauseOnLostFocus:false"
  } >build/docker/options.txt
  # download mods
  mkdir -p build/docker/mods
  curl -L $URL_HEADLESSFORGE -o build/docker/mods/headlessforge-1.1.jar
  curl -L $URL_BARITONE -o build/docker/mods/baritone-api-forge-1.2.15.jar
  md5sum -c mods.md5
fi

cp build/libs/headlessbot-*.jar build/docker/mods/

# use rootless if possible
docker --version || sudo su

# cleanup
docker-compose rm -f
docker rmi -f headlessbot:dev

# rebuild image
docker build . -t headlessbot:dev

# run
docker-compose up #--force-recreate
