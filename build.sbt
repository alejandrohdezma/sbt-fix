ThisBuild / scalaVersion := "2.12.10"
ThisBuild / repository   := "sbt-fix"
ThisBuild / name         := "sbt-fix"

enablePlugins(SbtPlugin)

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.7")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.2.1")

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2"

disablePlugins(ScalafixWithDefaultRulesPlugin, ScalafmtWithDefaultConfigPlugin)
