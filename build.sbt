ThisBuild / scalaVersion := "2.12.10"

lazy val `root` = project
  .in(file("."))
  .aggregate(`sbt-fix`, `sbt-fix-it`)
  .settings(name := "sbt-fix")
  .settings(skip in publish := true)

lazy val `sbt-fix` = project
  .enablePlugins(SbtPlugin, MdocPlugin)
  .settings(mdocVariables := Map("VERSION" -> version.value.replaceAll("\\+.*", "")))
  .settings(addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.11"))
  .settings(addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.0"))
  .settings(libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2")

lazy val `sbt-fix-it` = project
  .enablePlugins(SbtPlugin)
  .dependsOn(`sbt-fix`)
