$Id: README.TXT,v 1.1 2005/09/07 20:24:45 gmurray Exp $
28 Aug 2013

Here is a Java implementation of the matrix for rotating a point about
an arbitrary line in three dimensions, as described at

https://sites.google.com/site/glennmurray/Home/rotation-matrices-and-formulas

and at 

http://www.mines.edu/~gmurray/ArbitraryAxisRotation/ArbitraryAxisRotation.html

The tests can be run with this command.

    java -cp classes:lib/junit_3.8.1-mac.jar junit.swingui.TestRunner arbaxisrot.AllTests

The distribution includes an Apache Ant build.xml file.  To use it you
will need a working ant implementation.  To compile the classes, in
the directory containing this file run "ant compile".  To run the
tests, enter "ant test".

Have fun,
Glenn
