name := "ship-happens"

version := "1.0"

scalaVersion := "2.11.2"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
