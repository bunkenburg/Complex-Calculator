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

The "Command-line approach" on http://one-jar.sourceforge.net/index.php?page=getting-started&file=quickstart 
works, except that curiously the font on the displayed calculator seems a bit impaired.

## Command-Line Approach

The use of Ant is not required: a One-JAR archive is simple to build using just the jar tool using the following steps.

    Create a working directory to act as the "root" of the one-jar with main, lib sub-directories.
    Copy your main application jar file into root/main and library dependencies into root/lib
    Unjar the one-jar-boot-0.97.jar file into the root directory, and delete the "src" tree
    Edit the boot-manifest.mf file and add a new line: One-Jar-Main-Class: your-main-class

    Note: make sure that your final one-jar contains a manifest that looks like this:

    Manifest-Version: 1.0
    Main-Class: com.simontuffs.onejar.Boot
    One-Jar-Main-Class: YOUR.MAIN.CLASS.NAME.HERE

    cd root; jar -cvfm ../one-jar.jar boot-manifest.mf .

You should end up with a One-JAR archive which mirrors the "root" tree:

one-jar.jar
|  META-INF/MANIFEST.MF
|  .version
|  com/simontuffs/onejar
   |  Boot.class, ...etc.
|  doc/one-jar-license.txt
|  main/main.jar
|  lib/a.jar ...etc.

That's it: no code to write, just a directory tree, some copy operations, and a file edit. The One-JAR classloader discovers the libraries and main code based on their position in the archive, and ignores any other Jar files should you need to embed archives which should not be on the classpath. Embedding the one-jar-license.txt ensures compliance with the BSD-style license. 


## automate it

See bash script

    bin/build.sh
    
On Ubuntu, I can open the resulting JAR with double click.


## for scala

One-jar for scala: https://github.com/sbt/sbt-onejar

https://index.scala-lang.org/sbt/sbt-onejar/sbt-onejar/0.8?target=_2.10_0.13

project/plugins.sbt:

    addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")
    
Include the settings from com.github.retronym.SbtOneJar.oneJarSettings.

You can configure mainClass in oneJar := Some("com.acme.Woozler"). It defaults to mainClass in run in Compile.

Cannot load it:

    $ sbt
    [info] Loading global plugins from /home/alex/.sbt/1.0/plugins
    [info] Loading settings for project complex-calculator-build from plugins.sbt ...
    [info] Loading project definition from /home/alex/git/Complex-Calculator/project
    [info] Updating ProjectRef(uri("file:/home/alex/git/Complex-Calculator/project/"), "complex-calculator-build")...
    [warn] 	module not found: org.scala-sbt.plugins#sbt-onejar;0.8
    [warn] ==== typesafe-ivy-releases: tried
    [warn]   https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt.plugins/sbt-onejar/scala_2.12/sbt_1.0/0.8/ivys/ivy.xml
    [warn] ==== sbt-plugin-releases: tried
    [warn]   https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/org.scala-sbt.plugins/sbt-onejar/scala_2.12/sbt_1.0/0.8/ivys/ivy.xml
    [warn] ==== local: tried
    [warn]   /home/alex/.ivy2/local/org.scala-sbt.plugins/sbt-onejar/scala_2.12/sbt_1.0/0.8/ivys/ivy.xml
    [warn] ==== public: tried
    [warn]   https://repo1.maven.org/maven2/org/scala-sbt/plugins/sbt-onejar_2.12_1.0/0.8/sbt-onejar-0.8.pom
    [warn] ==== local-preloaded-ivy: tried
    [warn]   /home/alex/.sbt/preloaded/org.scala-sbt.plugins/sbt-onejar/0.8/ivys/ivy.xml
    [warn] ==== local-preloaded: tried
    [warn]   file:////home/alex/.sbt/preloaded/org/scala-sbt/plugins/sbt-onejar_2.12_1.0/0.8/sbt-onejar-0.8.pom
    [warn] 	::::::::::::::::::::::::::::::::::::::::::::::
    [warn] 	::          UNRESOLVED DEPENDENCIES         ::
    [warn] 	::::::::::::::::::::::::::::::::::::::::::::::
    [warn] 	:: org.scala-sbt.plugins#sbt-onejar;0.8: not found
    [warn] 	::::::::::::::::::::::::::::::::::::::::::::::
    [warn] 
    [warn] 	Note: Some unresolved dependencies have extra attributes.  Check that these dependencies exist with the requested attributes.
    [warn] 		org.scala-sbt.plugins:sbt-onejar:0.8 (scalaVersion=2.12, sbtVersion=1.0)
    
Cannot load it in plugins.sbt.

//https://github.com/sbt/sbt-onejar
//exists https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/com.github.retronym/sbt-onejar/scala_2.10/sbt_0.13/0.8/ivys/ivy.xml
//tried https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/org.scala-sbt.plugins/sbt-onejar/scala_2.12/sbt_1.0/0.8/ivys/ivy.xml
//addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8")
//addSbtPlugin("org.scala-sbt.plugins" % "sbt-onejar" % "0.8")

Abandon this line.