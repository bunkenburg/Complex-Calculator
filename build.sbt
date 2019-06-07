ThisBuild / version      := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / organization := "cat.inspiracio"

lazy val calculator = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .enablePlugins(JDKPackagerPlugin)
  .settings(
    name := "Complex Calculator",
    maintainer := "Alexander Bunkenburg <alex@inspiracio.cat>",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1",
    // https://mvnrepository.com/artifact/org.scala-lang.modules/scala-parser-combinators
    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    javacOptions ++= Seq("-source", "1.8")
  )
