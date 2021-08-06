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
  curl -L $URL_HEADLESSFORGE -o mc/mods/headlessforge-1.1.jar
  curl -L $URL_BARITONE -o mc/mods/baritone-api-forge-1.2.15.jar
  md5sum -c mods.md5
fi

cp ../build/libs/headlessbot-*.jar mc/mods/

# sanitize file names
if detox -V >/dev/null 2>&1; then detox -v -r ./configs; fi

# TODO: replace the following crap with something like docker-compose

# use rootless if possible
docker --version || sudo su

# rebuild image
docker rmi -f headlessbot:dev
docker build . -t headlessbot:dev

# run worker for each config file
for f in ./configs/*.json; do
  name=headlessbot_$(basename "$f" | cut -d'.' -f1)
  echo "starting worker $name"
  docker run -tid --rm -v "$(realpath "$f"):/opt/app/headless.json" --name "$name" headlessbot:dev
  sleep 10
done

docker ps
