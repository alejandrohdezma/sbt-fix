// For using the plugin in its own build
Compile / unmanagedSourceDirectories +=
  (ThisBuild / baseDirectory).value.getParentFile / "modules" / "sbt-fix" / "src" / "main" / "scala"
