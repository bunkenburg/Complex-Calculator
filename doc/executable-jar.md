# executable JAR

## what is it?

MANIFEST.MF:

    Manifest-Version: 1.0
    Main-Class: cat.inspiracio.calculator.Calculator
    
Make it:

    jar cvfe myjar.jar cat.inspiracio.calculator.Calculator *.class
    
Run it:

    java -jar MyJavaTool.jar
    
If the user has Java, many OS make it possible to double-click the JAR.

But it must be one jar, no dependencies. Maybe consider one-jar.

## linux

https://askubuntu.com/questions/192914/how-run-a-jar-file-with-a-double-click

## Mac

## Windows