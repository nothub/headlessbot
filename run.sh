#!/usr/bin/env sh

set -eu

cd "$(realpath "$(dirname "$(readlink -f "$0")")")"

# build mod
./gradlew --console "plain" --info -- build

mkdir -p run
cd run

volumes="-v ${PWD}/mc:/work/.minecraft -v ${PWD}/hmc:/work/HeadlessMC"

# msa login
if test ! -f "hmc/auth/.account.json"; then
    docker run --rm ${volumes} headlessmc:latest "login" "<username>" "<password>"
fi

# download mc
if test ! -e "mc/versions/1.19.4"; then
    docker run --rm ${volumes} headlessmc:latest "download" "1.19.4"
fi

# download fabric
if test ! -e "mc/versions/fabric-loader-0.14.22-1.19.4"; then
    docker run --rm ${volumes} headlessmc:latest "fabric" "1.19.4"
fi

mkdir -p mc/mods

# install fabric api
if test ! -e "mc/mods/fabric-api.jar"; then
    curl -sSL -o "mc/mods/fabric-api.jar" \
    "https://cdn.modrinth.com/data/P7dR8mSH/versions/LKgVmlZB/fabric-api-0.87.0+1.19.4.jar"
fi

# install mod
cp "../build/libs/headlessbot.jar" "mc/mods/"

# run
docker run -it --rm ${volumes} n0thub/headlessmc:latest "launch" "fabric-loader-0.14.22-1.19.4"
