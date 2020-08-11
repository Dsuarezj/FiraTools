name := "FiraTools"

version := "1.0-SNAPSHOT"
// Dependencies version
val scalaTest: String = "3.0.5"
val scalaTestPlay: String = "4.0.2"

lazy val `FiraTools` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc,
  ehcache,
  ws,
  guice,
  "org.scalatest" %% "scalatest" % scalaTest % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % "test"
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )


