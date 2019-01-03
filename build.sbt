ThisBuild / version      := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "cat.inspiracio"

lazy val calculator = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .settings(
    name := "Complex Calculator",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    javacOptions ++= Seq("-source", "1.8")
  )