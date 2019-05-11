# jpackager

https://medium.com/@adam_carroll/java-packager-with-jdk11-31b3d620f4a8

java packager for Java 11:
https://mail.openjdk.java.net/pipermail/openjfx-dev/2018-September/022500.html

Next packaging tool:
https://openjdk.java.net/jeps/343


## java 8

* introduces javapackager

## java 9

* introduces jlink. Makes custom runtime image of your application code and some JRE modules.

## java 11

* drops applets
* drops Java Web Start
* drops Java FX
* drops javapackager

## jlink

https://docs.oracle.com/en/java/javase/11/tools/jlink.html

## inno setup

http://www.jrsoftware.org/isdl.php

## gradle

https://gradle.org/

## gradle wrapper

https://docs.gradle.org/current/userguide/gradle_wrapper.html

## my javas

    $ update-alternatives --list java
    /usr/lib/jvm/java-10.0.2-openjdk-amd64/bin/java
    /usr/lib/jvm/java-11-openjdk-amd64/bin/java
    /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
    
    $ java -version
    openjdk version "11.0.1" 2018-10-16
    OpenJDK Runtime Environment (build 11.0.1+13-Ubuntu-3ubuntu3.18.10.1)
    OpenJDK 64-Bit Server VM (build 11.0.1+13-Ubuntu-3ubuntu3.18.10.1, mixed mode, sharing)
    
## jpackager

    jpackager create-image 
        Generates a platform-specific application image.
        
    jpackager create-installer <type>
        Generates a platform-specific installer for the application.
        Valid values for "type" are "msi", "rpm", "deb", "dmg", “pkg”,
        “pkg-app-store”. If "type" is omitted, all supported types of installable
        packages for current platform will be generated.
                  
    jpackager create-jre-installer <type>
        Generates a platform-specific installer for Server JRE.
        Valid values for "type" are "msi", "rpm", "deb", "dmg", “pkg”.
        If "type" is omitted, all supported types of installable packages
        for current platform will be generated.

options:

    --output    Output directory
    --input     Base directory for input files to package
    --files     List of files in the base directory. If omitted, all files from "input"
                directory (which is a mandatory argument in this case) will be packaged.
    --name "Complex Calculator"
    --main-jar  
    --class     cat.inspiracio.calculator.Calculator
    --version
    --arguments Command line arguments to main class
    --icon
    --singleton
    --identifier
    --verbose
    --strip-native-commands Removes native executables from the custom run-time images.
    --jvm-args
    --user-jvm-args
    --file-associations
    --secondary-launcher
    --build-root    Directory for temporary files
    --runtime-image
    --app-image
    --install-dir
    --echo-mode
    --license-file
    --copyright
    --description
    --category
    --vendor
    
    --module        Main module that contains the main class
    --module-path   JLink looks for modules on this path
    --add-modules   List of modules -- like classpath?
    --limit-modules 
    
    --linux-bundle-name
    --linux-package-deps
    --linux-rpm-license-type
    --linux-deb-maintainer
    
For Complex Calculator:

    bin/jpackager create-image --output target --input target/universal/stage/lib --files "cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar,org.scala-lang.scala-library-2.12.8.jar" --name "Complex Calculator" --class cat.inspiracio.calculator.Calculator --main-jar cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar --verbose
        
    bin/jpackager create-image 
    --output target
    --input target/universal/stage/lib 
    --files "cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar,org.scala-lang.scala-library-2.12.8.jar"
    --name "Complex Calculator"
    --class cat.inspiracio.calculator.Calculator
    --main-jar cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar
    --module-path .
    --verbose

An old JAR is not a module:
    
    --module cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar

An old JAR is not a module:

    --add-modules "cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar,org.scala-lang.scala-library-2.12.8.jar"

Too complicated. Do this when my JARs are really modules.