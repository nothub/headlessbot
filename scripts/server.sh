#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

prop() {
    cat gradle.properties \
        | grep -E -m 1 "${1}"'\s*=' \
        | sed -E 's/^\w+\s*=\s*//'
}

# workdir is repository root
cd "$(dirname "$(realpath "$0")")/.."

mc_version="$(prop 'minecraft_version')"

mkdir -p run/server
cd run/server

if test ! -f "paper-${mc_version}.jar"; then
    build_id="$(curl -s https://api.papermc.io/v2/projects/paper/versions/${mc_version}/builds \
        | jq -r '.builds | map(select(.channel == "default") | .build) | .[-1]')"
    jar_url="https://api.papermc.io/v2/projects/paper/versions/${mc_version}/builds/${build_id}/downloads/paper-${mc_version}-${build_id}.jar"
    curl -fsSL -o "paper-${mc_version}.jar" "${jar_url}"
fi

{
    echo "motd=headlessbot test-server"
    echo "difficulty=hard"
    echo "spawn-protection=0"
    echo "enforce-secure-profile=false"
} > server.properties

java \
    -Dcom.mojang.eula.agree=true \
    -jar "paper-${mc_version}.jar" \
    nogui
