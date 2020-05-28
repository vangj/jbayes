#!/bin/bash

updateVersion() {
  echo "replace version of software to ${APP_VERSION}"
  cd /jbayes \
    && mvn versions:set -DnewVersion=${APP_VERSION}
}

setupCredentials() {
  if [[ -f /jbayes/.docker/public.pgp  ]]; then
    echo "importing public.pgp"
    gpg --import /jbayes/.docker/public.pgp
  else
    echo "public.pgp not found!"
  fi

  if [[ -f /jbayes/.docker/private.pgp  ]]; then
    echo "importing private.pgp"
    gpg --batch --import /jbayes/.docker/private.pgp
  else
    echo "private.pgp not found!"
  fi

  echo "modifying settings.xml"
  cp -f /maven/conf/settings.xml /maven/conf/settings.bak
  cp -f /jbayes/.docker/settings.xml /maven/conf
  sed -i "s/__OSSRH_USERNAME__/${OSSRH_USERNAME}/g" /maven/conf/settings.xml
  sed -i "s/__OSSRH_PASSWORD__/${OSSRH_PASSWORD}/g" /maven/conf/settings.xml
  sed -i "s/__GPG_PASSPHRASE__/${GPG_PASSPHRASE}/g" /maven/conf/settings.xml
}

publish() {
  echo "publishing to OSSRH"

  if [[ -f /jbayes/.docker/public.pgp && -f /jbayes/.docker/private.pgp ]]; then
    export GPG_TTY=$(tty)
    cd /jbayes \
      && mvn clean deploy -P ${MVN_PROFILES}
  else
    echo "no pgp keys found!"
  fi
}

cleanUp() {
  echo "overriding maven settings.xml"
  cp -f /maven/conf/settings.bak /maven/conf/settings.xml

  gpg --list-secret-keys --with-colons --fingerprint ${GPG_KEY} |\
    grep "^fpr" |\
    sed -n 's/^fpr:::::::::\([[:alnum:]]\+\):/\1/p' |\
    xargs gpg --batch --delete-secret-key

  gpg --list-keys --with-colons --fingerprint ${GPG_KEY} |\
    grep "^fpr" |\
    sed -n 's/^fpr:::::::::\([[:alnum:]]\+\):/\1/p' |\
    xargs gpg --batch --delete-key
}

updateVersion
setupCredentials
publish
cleanUp

echo "done!"