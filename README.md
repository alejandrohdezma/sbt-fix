# SBT plugin that enhances scalafix & scalafmt duo

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-fix" % "0.10.0")
```

## Usage

This plugin adds a `fix` task to every project in the build.

This task can be used to launch both `scalafmt` and `scalafix` in all supported configurations.

```sbt
fix
```

It can also be used for checking that all files have been fixed with both tools, exiting with non-zero code on violations, by appending the `--check` argument (which can be used in CI to check formatting easily). A `ci` task alias is also provided as a shorter form of `fix --check`.

```sbt
fix --check
ci
```

All usages can be scoped to a specific project in the build:

```sbt
my-project/fix
my-project/fix --check
my-project/ci
```

## Chaining additional tasks

`fix` and `fix --check` can be extended with extra format/check tasks via the `fixExtra` and `fixCheckExtra` settings. Whatever is listed there is run **after** the built-in `scalafmt` + `scalafix` steps.

`fixExtra` holds `Seq[Def.Initialize[Task[Unit]]]`, so any of the following shapes work:

```sbt
// 1. A plain task key
fixExtra += myFormatTask

// 2. An input-task invocation (e.g. shelling out to a CLI plugin)
fixExtra += someInputKey.toTask(" --write")

// 3. An inline task literal
fixExtra += Def.task { /* anything returning Unit */ }
```

`fixCheckExtra` holds `Seq[NamedCheck]`. A `NamedCheck` is a `(name, task)` pair shown in the labelled CI output and the Markdown summary table. The `.named(...)` combinator on any task initializer wraps it as a `NamedCheck`:

```sbt
fixCheckExtra += myFormatCheckTask.named("my-linter")
fixCheckExtra += someInputKey.toTask(" --check").named("foo")
fixCheckExtra += Def.task { /* anything */ }.named("bar")
fixCheckExtra += NamedCheck("baz", myLinterTask)            // explicit form
```

Defaults are empty seqs, so projects that don't set these see the original behavior.

## `fix --check` output

`fix --check` keeps going even when an earlier check fails so a single CI run surfaces every formatter/linter violation at once. It exits with a non-zero status if any check failed.

When the `CI` environment variable is set (GitHub Actions, GitLab, CircleCI, Travis, Buildkite, etc. all do this by default), the task additionally:

1. Prints the ordered plan of checks.
2. Logs a `==> <check>` banner before each check.
3. Logs a `PASS` / `FAIL` summary block after all checks finish.
4. Writes the same summary as a Markdown status table. If `GITHUB_STEP_SUMMARY` is defined (the GitHub Actions runtime sets it), the table is appended to that file and shows up on the [workflow run summary page](https://docs.github.com/en/actions/reference/workflows-and-actions/workflow-commands#adding-a-job-summary). Otherwise it is written to `target/fix-check-summary.md`.

Local invocations (no `CI` env var) stay quiet — only the underlying scalafmt/scalafix output appears.

Markdown sample:

```md
# CI checks (2.12.20)

| Check | Status |
| --- | --- |
| scalafmt-check | PASS |
| scalafmt-sbt-check | PASS |
| scalafix-check | FAIL |
```

The Scala version is included in the heading so a cross-built run (`+fix --check`) produces a distinct, identifiable block on the GitHub summary page for each Scala version.
