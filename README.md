# SBT plugin that enhances scalafix & scalafmt duo

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-fix" % "0.8.0")
```

## Usage

This plugin adds a `fix` task to every project in the build.

This task can be used to launch both `scalafmt` and `scalafix` in all supported configurations.

```sbt
fix
```

It can also be used for checking that all files have been fixed with both tools, exiting with non-zero code on violations, by appending the `--check` argument (which can be used in CI to check formatting easily).

```sbt
fix --check
```

Both usages can be scoped to a specific project in the build:

```sbt
my-project/fix
my-project/fix --check
```
