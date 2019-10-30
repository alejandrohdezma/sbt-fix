ThisBuild / scalaVersion := "2.12.10"
ThisBuild / repository   := "sbt-fix"

lazy val `root` = project
  .in(file("."))
  .aggregate(`sbt-fix`)
  .settings(skip in publish := true)

lazy val `sbt-fix` = project
  .enablePlugins(SbtPlugin)
  .settings(addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.7"))
  .settings(addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.2.1"))
  .settings(libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2")
