#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

headlessmc() {
    docker run --rm \
        -p "127.0.0.1:8080:8080" \
        -v "${PWD}/mc:/work/.minecraft" \
        -v "${PWD}/hmc:/work/HeadlessMC" \
        "n0thub/headlessmc:1.9.1" \
        "${@}"
}

# workdir is repository root
cd "$(dirname "$(realpath "$0")")/.."

./gradlew --console plain --info --full-stacktrace clean check build

mkdir -p run
cd run

# msa login
if test ! -f "hmc/auth/.account.json"; then
    if test -f "../auth.json"; then
        username="$(cat "../auth.json" | jq -r '.username')"
        password="$(cat "../auth.json" | jq -r '.password')"
    else
        username="${1}"
        password="${2}"
    fi
    # TODO: stop passing sensitive data as command arguments
    headlessmc login "${username}" "${password}"
fi

# download mc
if test ! -e "mc/versions/1.20.4"; then
    headlessmc download "1.20.4"
fi

# download fabric
if test ! -e "mc/versions/fabric-loader-0.15.7-1.20.4"; then
    headlessmc fabric "1.20.4"
fi

# download mods
mkdir -p "mc/mods"
mods=$(jq -r '.mods' "../mods.json")
for mod in $(echo "$mods" | jq -c '.[]'); do
    file=$(basename "$(echo "$mod" | jq -r '.url')")
    url=$(echo "$mod" | jq -r '.url')
    sha256=$(echo "$mod" | jq -r '.sha256')
    if test ! -e "mc/mods/${file}"; then
        echo "fetching ${file} from ${url}"
        curl -sSL -o "mc/mods/${file}" "${url}"
        echo "${sha256} mc/mods/${file}" | sha256sum -c -
    fi
done

# install our mod
cp ../build/libs/headlessbot.jar mc/mods/

# launch bot + monitoring
cd ..
docker compose up --abort-on-container-exit --force-recreate -V
