# one jar

## for java

One-jar for java: http://one-jar.sourceforge.net/

http://one-jar.sourceforge.net/index.php?page=getting-started&file=quickstart
    
    You should end up with a One-JAR archive which mirrors the "root" tree:
    
    one-jar.jar
    |  META-INF/MANIFEST.MF
    |  .version
    |  com/simontuffs/onejar
       |  Boot.class, ...etc.
    |  doc/one-jar-license.txt
    |  main/main.jar
    |  lib/a.jar ...etc.

## for scala

One-jar for scala: https://github.com/sbt/sbt-onejar

project/plugins.sbt:

    addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")
    
Include the settings from com.github.retronym.SbtOneJar.oneJarSettings.

You can configure mainClass in oneJar := Some("com.acme.Woozler"). It defaults to mainClass in run in Compile.