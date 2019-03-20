# Complex Calculator

A Java application that shows operations with complex numbers graphically.
Shows the two-dimensional number plane and the Riemann sphere.

https://xkcd.com/2028/

## Turbo Pascal for Macintosh

The first version was on Apple Macintosh System 6 in 1992. 

## Java 1.1 as applet and application

The first java version is from 6. 1. 1999. I made it in Java 1.1 which introduced inner classes.
I lost the source code of that program, but decompiled byte code survived.

## Scala 2.12.8 as application

The first scala version is from 6. 1. 2019. 
There are three areas of the program:
1. Complex numbers in package cat.inspiracio.complex
2. Vector and matrix operations for 2d and 3d graphics
3. Java Swing GUI.


## how to run the executable jar

You need only java, at least a JRE at least version 8.

Get the latest executable JAR from 

    https://github.com/bunkenburg/Complex-Calculator/blob/master/bin/calculator.jar
    
Save it somewhere.

Open the JAR by double-click, with a Java 8 runtime. 

If your windowing system is not configured to do this via GUI, you can start from command line:

    $ java -jar calculator.jar



## how to build it yourself

You need git, java, scala, sbt.

Clone the repository.

    $ git clone git@github.com:bunkenburg/Complex-Calculator.git

Build an executable JAR. (Works on linux and apparently on Windows 10.)

    $ bin/build-jar.sh

The executable JAR is in bin/calculator.jar. Double-click to run it, or:

    $ java -jar calculator.jar