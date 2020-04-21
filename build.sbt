ThisBuild / scalaVersion := "2.12.11"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; docs/mdoc")
addCommandAlias("ci-docs", "docs/mdoc; headerCreateAll")

skip in publish := true

lazy val scalafix = "ch.epfl.scala" % "sbt-scalafix" % "[0.9.0,)" % Provided // scala-steward:off
lazy val scalafmt = "org.scalameta" % "sbt-scalafmt" % "[2.0.0,)" % Provided // scala-steward:off

lazy val docs = project
  .in(file("sbt-fix-docs"))
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)

lazy val `sbt-fix` = project
  .enablePlugins(SbtPlugin)
  .settings(addSbtPlugin(scalafix))
  .settings(addSbtPlugin(scalafmt))
