// For using the plugin in its own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "modules" / "sbt-fix" / "src" / "main" / "scala"
