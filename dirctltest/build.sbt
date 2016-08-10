organization := "antivoland"

name := "dirctltest"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

mainClass in(Compile, run) := Some("antivoland.dirctltest.BigBrother")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)
