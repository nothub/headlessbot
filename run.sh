#!/usr/bin/env sh

set -eu

headlessmc() {
    docker run -it --rm \
        -p "127.0.0.1:8080:8080" \
        -v "${PWD}/mc:/work/.minecraft" \
        -v "${PWD}/hmc:/work/HeadlessMC" \
        "n0thub/headlessmc:latest" \
        "${@}"
}

cd "$(realpath "$(dirname "$(readlink -f "$0")")")"

make clean build

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
if test ! -e "mc/versions/1.19.4"; then
    headlessmc download "1.19.4"
fi

# download fabric
if test ! -e "mc/versions/fabric-loader-0.14.22-1.19.4"; then
    headlessmc fabric "1.19.4"
fi

# download mods
mkdir -p "mc/mods"
mods=$(jq -r '.mods' "../mods.json")
for mod in $(echo "$mods" | jq -c '.[]'); do
    file=$(echo "$mod" | jq -r '.file')
    url=$(echo "$mod" | jq -r '.url')
    sha256=$(echo "$mod" | jq -r '.sha256')
    if test ! -e "mc/mods/${file}"; then
        echo "fetching ${file} from ${url}"
        curl -sSL -o "mc/mods/${file}" "${url}"
        echo "${sha256} mc/mods/${file}" | sha256sum -c -
    fi
done

# install mod
cp ../build/libs/headlessbot.jar mc/mods/

# run client
headlessmc launch "fabric-loader-0.14.22-1.19.4"
