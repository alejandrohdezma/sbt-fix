ThisBuild / scalaVersion                  := "2.13.7"
ThisBuild / organization                  := "com.alejandrohdezma"
ThisBuild / skip in publish               := true
ThisBuild / pluginCrossBuild / sbtVersion := "1.2.8"

addCommandAlias("ci-test", "fix --check; mdoc")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll")
addCommandAlias("ci-publish", "github; ci-release")

lazy val scalafix = "ch.epfl.scala" % "sbt-scalafix" % "[0.9.18,)" % Provided // scala-steward:off

lazy val scalafmt = "org.scalameta" % "sbt-scalafmt" % "[2.0.0,)" % Provided // scala-steward:off

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))

lazy val `sbt-fix` = project
  .enablePlugins(SbtPlugin)
  .settings(skip in publish := false)
  .settings(addSbtPlugin(scalafix))
  .settings(addSbtPlugin(scalafmt))
