# sbt native packager


## https://www.scala-sbt.org/sbt-native-packager/introduction.html

Native formats should build on their respective platform.

    sbt package
    
Format plugins define how a package is created

* debian

archetype plugins define what a package should contain

Mappings define how your build files should be organized on the target system.

    Mappings are a Seq[(File, String)], which translates to “a list of tuples, where each tuple defines a source file that gets mapped to a path on the target system”.

packageBin
publishLocal

enablePlugins(SomePackageFormatPlugin)
enablePlugins(SomeArchetypePlugin)      <-- An archetype plugin should be the starting point for creating packages!

mappings: TaskKey[Seq[(File, String)]]


# https://www.scala-sbt.org/sbt-native-packager/gettingstarted.html

add to plugins.sbt:

    addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "x.y.z")
    
build.sbt, enable archetype:

    enablePlugins(JavaAppPackaging)
    
    
    $ stb stage
    
In /target/universal/stage/bin/complex-calculator that leaves a shell script for linux and for Windows to start the
program -- needs:

* java installation
* Complex-Calculator.jar
* scala-library.jar

The JARs are in /target/universal/stage/lib/.

## create a package

### Make a zip

    $ sbt universal:packageBin
    
makes target/universal/complex-calculator-0.0.1-SNAPSHOT.zip
You can unzip it. It contains:
    
    /bin/complex-calculator
    /bin/complex-calculator.bat
    /lib/cat.inspiracio.complex-calculator-0.0.1-SNAPSHOT.jar
    /lib/org.scala-lang.scala-library-2.12.8.jar
    
### Make a tgz

    $ sbt universal:packageZipTarball
    
makes target/universal/complex-calculator-0.0.1-SNAPSHOT.tgz


### Make a deb

    $ sbt debian:packageBin
    ...
    [error] dpkg-deb: warning: parsing file '/home/alex/git/Complex-Calculator/target/Complex Calculator-0.0.1-SNAPSHOT/DEBIAN/control' near line 11 package 'complex-calculator':
    [info] dpkg-deb: building package 'complex-calculator' in '../Complex Calculator_0.0.1-SNAPSHOT_all.deb'.
    [error]  missing maintainer
    [error] dpkg-deb: warning: ignoring 1 warning about the control file(s)
    [success] Total time: 1 s, completed 26-Feb-2019 18:05:18
    
makes target/Complex Calculator_0.0.1-SNAPSHOT_all.deb which you can install in Ubuntu and run it:

    $ complex-calculator
    
### Make a docker container

    $ sbt docker:publishLocal
    
makes target/docker/stage/Dockerfile and other files.

    $ cd target/docker/stage/
    $ docker build .
    Sending build context to Docker daemon      6MB
    Step 1/6 : FROM openjdk:latest
     ---> 9bbe44eb5d03
    Step 2/6 : WORKDIR /opt/docker
     ---> Using cache
     ---> 80eab333cf0f
    Step 3/6 : ADD --chown=daemon:daemon opt /opt
     ---> Using cache
     ---> 5907960e5181
    Step 4/6 : USER daemon
     ---> Using cache
     ---> 7e5b61fe216f
    Step 5/6 : ENTRYPOINT ["/opt/docker/bin/complex-calculator"]
     ---> Using cache
     ---> 5a8fb03b5b66
    Step 6/6 : CMD []
     ---> Using cache
     ---> c4407f5a58c5
    Successfully built c4407f5a58c5
    $ docker run c4407f5a58c5
    Exception in thread "main" java.lang.ExceptionInInitializerError
    	at cat.inspiracio.calculator.Calculator.main(Calculator.scala)
    Caused by: java.awt.HeadlessException: 
    No X11 DISPLAY variable was set, but this program performed an operation which requires it.
    	at java.desktop/java.awt.GraphicsEnvironment.checkHeadless(GraphicsEnvironment.java:208)
    	at java.desktop/java.awt.Window.<init>(Window.java:548)
    	at java.desktop/java.awt.Frame.<init>(Frame.java:423)
    	at java.desktop/javax.swing.JFrame.<init>(JFrame.java:224)
    	at cat.inspiracio.calculator.Calculator.<init>(Calculator.scala:55)
    	at cat.inspiracio.calculator.Calculator$.<init>(Calculator.scala:52)
    	at cat.inspiracio.calculator.Calculator$.<clinit>(Calculator.scala)
    	... 1 more
    	
Docker ist not useful because Complex Calculator needs screen.

### Make an rpm

    $ sbt rpm:packageBin
    [info] Loading global plugins from /home/alex/.sbt/1.0/plugins
    [info] Loading settings for project complex-calculator-build from plugins.sbt ...
    [info] Loading project definition from /home/alex/git/Complex-Calculator/project
    [info] Loading settings for project calculator from build.sbt ...
    [info] Set current project to Complex Calculator (in build file:/home/alex/git/Complex-Calculator/)
    [info] Updating ...
    [info] Packaging /home/alex/git/Complex-Calculator/target/scala-2.12/complex-calculator_2.12-0.0.1-SNAPSHOT-sources.jar ...
    [info] Wrote /home/alex/git/Complex-Calculator/target/scala-2.12/complex-calculator_2.12-0.0.1-SNAPSHOT.pom
    [info] Done packaging.
    [info] Done updating.
    [info] Main Scala API documentation to /home/alex/git/Complex-Calculator/target/scala-2.12/api...
    [info] Compiling 46 Scala sources to /home/alex/git/Complex-Calculator/target/scala-2.12/classes ...
    [warn] there were 14 feature warnings; re-run with -feature for details
    model contains 72 documentable templates
    [warn] there were 14 feature warnings; re-run with -feature for details
    [warn] one warning found
    [info] Done compiling.
    [info] Packaging /home/alex/git/Complex-Calculator/target/scala-2.12/complex-calculator_2.12-0.0.1-SNAPSHOT.jar ...
    [info] Done packaging.
    [warn] one warning found
    [info] Main Scala API documentation successful.
    [info] Packaging /home/alex/git/Complex-Calculator/target/scala-2.12/complex-calculator_2.12-0.0.1-SNAPSHOT-javadoc.jar ...
    [info] Done packaging.
    [error] `rpmVendor in Rpm` is empty.  Please provide a valid vendor for the rpm SPEC.
    [error] java.lang.RuntimeException: There are issues with the rpm spec data.
    [error] 	at scala.sys.package$.error(package.scala:26)
    [error] 	at com.typesafe.sbt.packager.rpm.RpmSpec.validate(RpmMetadata.scala:154)
    [error] 	at com.typesafe.sbt.packager.rpm.RpmHelper$.stage(RpmHelper.scala:25)
    [error] 	at com.typesafe.sbt.packager.rpm.RpmPlugin$.$anonfun$projectSettings$47(RpmPlugin.scala:158)
    [error] 	at scala.Function1.$anonfun$compose$1(Function1.scala:44)
    [error] 	at sbt.internal.util.$tilde$greater.$anonfun$$u2219$1(TypeFunctions.scala:40)
    [error] 	at sbt.std.Transform$$anon$4.work(System.scala:67)
    [error] 	at sbt.Execute.$anonfun$submit$2(Execute.scala:269)
    [error] 	at sbt.internal.util.ErrorHandling$.wideConvert(ErrorHandling.scala:16)
    [error] 	at sbt.Execute.work(Execute.scala:278)
    [error] 	at sbt.Execute.$anonfun$submit$1(Execute.scala:269)
    [error] 	at sbt.ConcurrentRestrictions$$anon$4.$anonfun$submitValid$1(ConcurrentRestrictions.scala:178)
    [error] 	at sbt.CompletionService$$anon$2.call(CompletionService.scala:37)
    [error] 	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
    [error] 	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
    [error] 	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
    [error] 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
    [error] 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
    [error] 	at java.lang.Thread.run(Thread.java:748)
    [error] (Rpm / stage) There are issues with the rpm spec data.
    [error] Total time: 15 s, completed 26-Feb-2019 18:17:55
    
Maybe missing configuration, maybe need an rpm system to build it.

### Make for Mac

    $ sbt universal:packageOsxDmg
    ...
    java.io.IOException: Cannot run program "hdiutil" 
    
Cannot make for Mac on linux.

### Package for Windows

    $ sbt windows:packageBin
    ...
    java.lang.RuntimeException: WIX environment not found.  Please ensure WIX is installed on this computer
    
You have to be on Windows to build for Windows.


## universal

https://www.scala-sbt.org/sbt-native-packager/formats/universal.html



