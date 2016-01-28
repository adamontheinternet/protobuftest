#!/usr/bin/env bash
echo Protocol File is $1
cp lib/protobuf-java-2.6.1.jar build/libs
java -jar build/libs/protobuftest-1.0.jar $1
