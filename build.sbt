ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "scalafixEnable; fix --check; docs/mdoc")
addCommandAlias("ci-docs", "docs/mdoc; headerCreateAll")

skip in publish := true

lazy val docs = project
  .in(file("sbt-fix-docs"))
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)

lazy val `sbt-fix` = project
  .enablePlugins(SbtPlugin)
  .settings(addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.12"))
  .settings(addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.2"))
