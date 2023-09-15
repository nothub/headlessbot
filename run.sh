#!/usr/bin/env sh

set -eu

cd "$(realpath "$(dirname "$(readlink -f "$0")")")"

# build mod
./gradlew --console "plain" --info -- build

mkdir -p run
cd run

# shellcheck disable=SC2139
alias headlessmc="docker run -it --rm -v ${PWD}/mc:/work/.minecraft -v ${PWD}/hmc:/work/HeadlessMC n0thub/headlessmc:latest"

# msa login
if test ! -f "hmc/auth/.account.json"; then
    if test -f "../auth.json"; then
        username="$(cat "../auth.json" | jq -r '.username')"
        password="$(cat "../auth.json" | jq -r '.password')"
    else
        username="${1}"
        password="${2}"
    fi
    headlessmc login "${username}" "${password}"
fi

# download mc
if test ! -e "mc/versions/1.19.4"; then
    headlessmc download "1.19.4"
fi

# download fabric
if test ! -e "mc/versions/fabric-loader-0.14.22-1.19.4"; then
    headlessmc fabric "1.19.4"
fi

mkdir -p mc/mods

# install fabric api
if test ! -e "mc/mods/fabric-api-0.87.0+1.19.4.jar"; then
    (
        cd mc/mods
        curl -sSLO "https://cdn.modrinth.com/data/P7dR8mSH/versions/LKgVmlZB/fabric-api-0.87.0+1.19.4.jar"
    )
fi

# install baritone
if test ! -e "mc/mods/baritone-api-fabric-1.9.3.jar"; then
    (
        cd mc/mods
        curl -sSLO "https://github.com/cabaletta/baritone/releases/download/v1.9.3/baritone-api-fabric-1.9.3.jar"
    )
fi

# install mod
cp ../build/libs/*.jar mc/mods/

# run client
headlessmc launch "fabric-loader-0.14.22-1.19.4"
