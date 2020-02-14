ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; mdoc")
addCommandAlias("ci-docs", "mdoc; headerCreateAll")

lazy val root = project
  .in(file("."))
  .enablePlugins(MdocPlugin)
  .aggregate(`sbt-fix`, `sbt-fix-it`)
  .settings(name := "sbt-fix")
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)

lazy val `sbt-fix` = project
  .enablePlugins(SbtPlugin)
  .settings(addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.11"))
  .settings(addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.1"))
  .settings(libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2")

lazy val `sbt-fix-it` = project
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-fix`)
