# Complex Calculator

A Java application that shows operations with complex numbers graphically.
Shows the two-dimensional number plane and the Riemann sphere.

https://xkcd.com/2028/

## Turbo Pascal for Macintosh

The first version was on Apple Macintosh System 6 in 1992. 

## Java 1.1 as applet and application

The first java version is from 6. 1. 1999. I made it in Java 1.1 which introduced inner classes.
I lost the source code of that program, so the present version is based on decompiled byte code.

## Scala 2.12.8 as application

The first scala version is from 6. 1. 2019. 
There are three areas of the program:
1. Complex numbers in package cat.inspiracio.complex
2. Vector and matrix operations for 2d and 3d graphics
3. Java Swing GUI.


## how to build it yourself

You need git, java, scala, sbt.

    $ git clone git@github.com:bunkenburg/Complex-Calculator.git
    $ sbt stage
    $ ls -lF target/universal/stage/lib
    total 5844
    -rw-r--r-- 1 alex alex  706227 feb 26 15:20 cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar
    -rw-r--r-- 1 alex alex 5272591 dic  4 11:15 org.scala-lang.scala-library-2.12.8.jar
    $ ls -lFr target/universal/stage/bin/
    total 20
    -rw-r--r-- 1 alex alex 5170 feb 26 15:15 complex-calculator.bat
    -rwxr--r-- 1 alex alex 9483 feb 26 15:15 complex-calculator*
    
Run it on linux (you need java):

    $ target/universal/stage/bin/complex-calculator
    
Run it on Windows (you need java):

    $ target/universal/stage/bin/complex-calculator.bat