name := "ship-happens"

version := "1.0"

scalaVersion := "2.11.2"

lazy val root = (project in file("."))
    .enablePlugins(PlayScala)
    .aggregate(lib)
    .dependsOn(lib)

lazy val lib = project
