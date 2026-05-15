# SBT plugin that enhances scalafix & scalafmt duo

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-fix" % "@VERSION@")
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

## Chaining additional tasks

`fix` and `fix --check` can be extended with extra format/check tasks via the `fixExtra` and `fixCheckExtra` settings. Whatever is listed there is run sequentially **after** the built-in `scalafmt` + `scalafix` steps. Both settings hold `Seq[Def.Initialize[Task[Unit]]]`, so any of the following shapes work:

```sbt
// 1. A plain task key
fixExtra      += myFormatTask
fixCheckExtra += myFormatCheckTask

// 2. An input-task invocation (e.g. shelling out to a CLI plugin)
fixExtra      += someInputKey.toTask(" --write")
fixCheckExtra += someInputKey.toTask(" --check")

// 3. An inline task literal
fixExtra      += Def.task { /* anything returning Unit */ }
```

Defaults are empty seqs, so projects that don't set these see the original behavior.
