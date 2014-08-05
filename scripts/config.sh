#!/usr/bin/env bash

VERSION="0.0.1-SNAPSHOT"
XMX="-Xmx4000m"
#LOG=INFO
PROJECT_NAME=ternarytree
LOG=INFO
CLI=de.mpii.ternarytree.cli
E_BADARGS=65

JAVA="java $XMX -Dlog=$LOG -Dlogat=$LOGAT  -cp ./target/$PROJECT_NAME-$VERSION-jar-with-dependencies.jar "

export LC_ALL=C

