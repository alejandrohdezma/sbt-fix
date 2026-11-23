// For using the plugin in its own build
Compile / unmanagedSourceDirectories ++= Seq("scala", "scala-2.12").map {
  (ThisBuild / baseDirectory).value.getParentFile / "modules" / "sbt-fix" / "src" / "main" / _
}
