# SBT plugin that enhances scalafix & scalafmt duo

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-fix" % "@VERSION@")
```

## Usage

This plugin adds a `fix` command to every project in the build.

This command can be used to launch both `scalafmt` and `scalafix` in all supported configurations.

```sbt
fix
```

It can also be used for checking that all files have been fixed with both tools, exiting with non-zero code on violations, by appending the `--check` argument (which can be used in CI to check formatting easily).

```sbt
fix --check
```
