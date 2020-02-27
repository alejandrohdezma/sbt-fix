ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

/**
 * Although the plugin already adds these dependencies,
 * this way we ensure that we get notified when a new version
 * for them gets published.
 */
ThisBuild / scalafixDependencies := ScalafixWithDefaultsPlugin.scalafixDefaultRules

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

lazy val `sbt-fix-it` = project
  .dependsOn(`sbt-fix`)
  .enablePlugins(SbtPlugin)
  .settings(description := "Enables scalafix/scalafmt settings in it configuration")
