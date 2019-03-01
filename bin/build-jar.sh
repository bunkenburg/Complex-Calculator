#!/usr/bin/env bash

# Usage:

# cd Complex-Calculator/
# bin/build-jar.sh

# Creates executables JAR target/calculator.jar

# This file is in bin/ but it works in target/root/
# It expects to be called from Complex-Calculator/.

sbt stage

rm -rf target/root
mkdir -p target/root/main/
mkdir -p target/root/lib/
cp target/universal/stage/lib/cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar target/root/main/complex-calculator.jar
cp target/universal/stage/lib/org.scala-lang.scala-library-2.12.8.jar target/root/lib/scala-library-2.12.8.jar

# Unjar the one-jar-boot-0.97.jar file into the root directory,
# like ar xf one-jar.jar
unzip -d target/root/ bin/one-jar-boot-0.97.jar

rm -rf target/root/src/

echo "One-Jar-Main-Class: cat.inspiracio.calculator.Calculator" >> target/root/boot-manifest.mf

jar cfm target/calculator.jar target/root/boot-manifest.mf -C target/root/ .

# back to project directory and show result
# cd ../../
echo "Executable JAR: "
ls -lF target/calculator.jar