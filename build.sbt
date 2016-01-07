name := "hashing"

organization := "net.mcarolan"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.4", "2.11.2")

resolvers ++= Seq("RoundEights" at "http://maven.spikemark.net/roundeights")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  "com.google.guava" % "guava" % "19.0"
)

initialCommands := "import net.mcarolan.hashing._"
