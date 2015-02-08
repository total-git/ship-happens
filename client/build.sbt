name := "ship-happens-client"

version := "1.0"

scalaVersion := "2.11.2"

resolvers += "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"

libraryDependencies ++= Seq(
    "io.spray" %%  "spray-json" % "1.3.1",
    "uk.co.bigbeeconsultants" %% "bee-client" % "0.28.+",
    "org.slf4j" % "slf4j-api" % "1.7.+",
    "ch.qos.logback" % "logback-core"    % "1.0.+",
    "ch.qos.logback" % "logback-classic" % "1.0.+"
)

