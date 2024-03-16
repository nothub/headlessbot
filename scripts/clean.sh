#!/usr/bin/env bash

set -o errexit
set -o nounset
set -o pipefail

# workdir is repository root
cd "$(dirname "$(realpath "$0")")/.."

set -x

rm -rf .gradle
rm -rf build
rm -rf run
rm -rf libs
