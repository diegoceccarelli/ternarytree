#!/usr/bin/env bash

EXPECTED_ARGS=2

source scripts/config.sh


if [ $# -ne $EXPECTED_ARGS ];
then
  echo "Usage: `basename $0` serialized-trie.bin key"
  exit $E_BADARGS
fi



echo "search key $2 in index $1"
$JAVA $CLI.SearchKeyCLI -index $1 -key $2


