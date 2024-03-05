
name := """GestorRecetas"""
organization := "com.mimo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.12"

enablePlugins(PlayEbean)
libraryDependencies += evolutions
libraryDependencies += jdbc

libraryDependencies += guice
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.5"
libraryDependencies += "com.typesafe.play" %% "play-ws" % "2.8.8"
libraryDependencies += "com.typesafe.play" %% "play-java-forms" % "2.8.17"
libraryDependencies += ws
libraryDependencies ++= Seq(
  "org.eclipse.persistence" % "eclipselink" % "3.0.0"
)
libraryDependencies += "com.h2database" % "h2" % "2.1.214"


















