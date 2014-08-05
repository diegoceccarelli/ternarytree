#!/usr/bin/env bash

EXPECTED_ARGS=2

source scripts/config.sh


if [ $# -ne $EXPECTED_ARGS ];
then
  echo "Usage: `basename $0` keys-file.txt[.gz] serialized-trie.bin"
  exit $E_BADARGS
fi



echo "convert pois to json"
$JAVA $CLI.IndexKeysCLI -input $1 -output $2


