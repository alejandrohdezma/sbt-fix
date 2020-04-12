ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "com.alejandrohdezma"

ThisBuild / scalafixDependencies ++= Seq(
  "com.github.vovapolu" %% "scaluzzi"         % "0.1.5",
  "com.nequissimus"     %% "sort-imports"     % "0.3.2",
  "com.eed3si9n.fix"    %% "scalafix-noinfer" % "0.1.0-M1"
)

ThisBuild / scalafixConfigLocation := url {
  "https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/v0.0.6/default.scalafix.conf"
}
ThisBuild / scalafmtConfigLocation := url {
  "https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/v0.0.6/default.scalafmt.conf"
}

Global / onChangedBuildSource := ReloadOnSourceChanges

addCommandAlias("ci-test", "fix --check; docs/mdoc")
addCommandAlias("ci-docs", "docs/mdoc; headerCreateAll")

lazy val `sbt-fix-root` = project
  .in(file("."))
  .aggregate(`sbt-fix`, `sbt-fix-it`)

lazy val docs = project
  .in(file("sbt-fix-docs"))
  .enablePlugins(MdocPlugin)
  .settings(name := "sbt-fix")
  .settings(mdocOut := file("."))
  .settings(skip in publish := true)

lazy val `sbt-fix` = project
  .enablePlugins(SbtPlugin)
  .settings(addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.12"))
  .settings(addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.2"))

lazy val `sbt-fix-it` = project
  .dependsOn(`sbt-fix`)
  .enablePlugins(SbtPlugin)
  .settings(description := "Enables scalafix/scalafmt settings in it configuration")
