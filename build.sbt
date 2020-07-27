name := "FiraTools"

version := "1.0-SNAPSHOT"

lazy val `FiraTools` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )

// Dependencies version
val scalaTestPlay = "4.0.2"
val scalatest = "3.0.5"

