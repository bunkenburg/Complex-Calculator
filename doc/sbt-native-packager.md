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
    