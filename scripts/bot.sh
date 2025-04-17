#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

project_root="$(dirname "$(realpath "$0")")/.."
cd "${project_root}"

prop() {
    cat gradle.properties \
        | grep -E -m 1 "${1}"'\s*=' \
        | sed -E 's/^\w+\s*=\s*//'
}

mc_version="$(prop 'minecraft_version')"
hmc_version="$(prop 'headlessmc_version')"
fabric_api_version="$(prop 'fabric_api_version')"

mkdir -p run
cd run

# msa login
if test ! -f "hmc/auth/.account.json"; then
    if test ! -f "${project_root}/auth.json"; then
        set +o nounset
        if test -z "${1}" || test -z "${2}"; then
            echo "Usage: ${0} [<username> <password>]" >&2
            echo "Create auth.json or supply username and password arguments." >&2
            exit 1
        fi
        set -o nounset
        jq ".username = \"${1}\" | .password = \"${2}\"" \
            "${project_root}/auth.example.json" > "${project_root}/auth.json"
    fi
    username="$(cat "${project_root}/auth.json" | jq -r '.username')"
    password="$(cat "${project_root}/auth.json" | jq -r '.password')"
    docker run -t --rm \
        --name "headlessbot" \
        -v "${PWD}/mc:/work/.minecraft" \
        -v "${PWD}/hmc:/work/HeadlessMC" \
        "n0thub/headlessmc:${hmc_version}" \
        login "${username}" "${password}"
fi

dlmod() {
    curl -fsSL -o "${1}" "${2}"
    if ! file --mime-type "${1}" | grep -E '.+: application/java-archive' 1> /dev/null; then
        printf >&2 '%s is not a jar file!\n' "${1}"
        file "${1}"
        file --mime-type "${1}"
        exit 1
    fi
}

mkdir -p "mc/mods"

dlmod 'mc/mods/fabric-api.jar' \
    "https://github.com/FabricMC/fabric/releases/download/${fabric_api_version}/fabric-api-${fabric_api_version}.jar"

dlmod 'mc/mods/baritone-api-fabric.jar' \
    "https://github.com/nothub/baritone-mirror/raw/refs/heads/main/${mc_version}/baritone-api-fabric-${mc_version}.jar"

(
    # build bot
    cd "${project_root}"
    ./gradlew --console=plain --info --full-stacktrace clean check build
    cp ./build/libs/headlessbot.jar run/mc/mods/
)

# launch bot
docker run -it --rm \
    --name "headlessbot" \
    -p "127.0.0.1:8080:8080" \
    -v "${PWD}/mc:/work/.minecraft" \
    -v "${PWD}/hmc:/work/HeadlessMC" \
    "n0thub/headlessmc:${hmc_version}" \
    launch "fabric:${mc_version}"
