#!/bin/bash

DOCKER_FILE=Dockerfile
DOCKER_REPO=jbayes
DOCKER_TAG=local
AOSSRH_USERNAME=dummy
AOSSRH_PASSWORD=dummy
AGPG_PASSPHRASE=dummy
AAPP_VERSION=dummy
AMVN_PROFILE=env-dev

while getopts u:p:g:v:m: option
do
  case "${option}"
  in
  u) AOSSRH_USERNAME=${OPTARG};;
  p) AOSSRH_PASSWORD=${OPTARG};;
  g) AGPG_PASSPHRASE=${OPTARG};;
  v) AAPP_VERSION=${OPTARG};;
  m) AMVN_PROFILES=${OPTARG};;
esac
done

if [[ "dummy" == $AOSSRH_USERNAME || "dummy" == $AOSSRH_PASSWORD || "dummy" == $AGPG_PASSPHRASE || "dummy" == $AAPP_VERSION ]]; then
  echo "Usage: ./build.sh -u [OSSRH_USERNAME] -p [OSSRH_PASSWORD] -g [PGP_PASSPHRASE] -v [APP_VERSION] -m [MVN_PROFILE]"
  echo "     -u OSSRH username"
  echo "     -p OSSRH password"
  echo "     -g GPG passphrase"
  echo "     -v version e.g. 0.0.4-SNAPSHOT"
  echo "     -m maven profile e.g. -P env-prd,ossrh"
else
  docker build --no-cache \
    -f $DOCKER_FILE \
    --build-arg AOSSRH_USERNAME=$AOSSRH_USERNAME \
    --build-arg AOSSRH_PASSWORD=$AOSSRH_PASSWORD \
    --build-arg AGPG_PASSPHRASE=$AGPG_PASSPHRASE \
    --build-arg AAPP_VERSION=$AAPP_VERSION \
    --build-arg AMVN_PROFILES=$AMVN_PROFILES \
    -t ${DOCKER_REPO}:${DOCKER_TAG} .
fi