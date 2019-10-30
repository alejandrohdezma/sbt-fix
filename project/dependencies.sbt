libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2"

// For using the plugins in their own build
unmanagedSourceDirectories in Compile +=
  baseDirectory.in(ThisBuild).value.getParentFile / "sbt-fix" / "src" / "main" / "scala"
