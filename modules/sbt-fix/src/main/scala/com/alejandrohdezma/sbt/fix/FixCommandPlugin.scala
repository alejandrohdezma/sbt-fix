/*
 * Copyright 2019-2026 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt.fix

import sbt.Keys._
import sbt.util.Level
import sbt._

import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport.scalafixAll

/** Adds a `fix` task to every project in the build.
  *
  * This task can be used to launch both scalafmt and scalafix in all supported configurations.
  *
  * It can also be used for checking that all files have been fixed with both tools, exiting with non-zero code on
  * violations, by appending the `--check` argument.
  *
  * The task can be scoped to a specific project: `my-project/fix` or `my-project/fix --check`.
  *
  * In check mode the plugin runs every check even if earlier ones fail. A check tagged with `.abortOnError` (see
  * [[autoImport.NamedCheck]]) is the exception: its failure marks the remaining checks as Skipped without running them.
  * When the `CI` environment variable is set, the run also prints a labelled banner per check, logs a ✅ / ❌ / ⏭️
  * summary, and writes a Markdown status table to `$$GITHUB_STEP_SUMMARY` (when set) or to
  * `target/fix-check-summary.md`, so the report can be surfaced as a GitHub Actions job summary. Local (non-CI)
  * invocations stay quiet.
  *
  * Additional steps can be plugged in via the `fixExtra` (write mode) and `fixCheckExtra` (check mode) settings; they
  * run after the built-in scalafmt/scalafix steps.
  */
object FixCommandPlugin extends AutoPlugin {

  object autoImport {

    val fix = inputKey[Unit] {
      "Launch both scalafmt and scalafix in all supported configurations"
    }

    val ci = taskKey[Unit] {
      "Alias for `fix --check`"
    }

    val fixExtra = settingKey[Seq[Def.Initialize[Task[Unit]]]] {
      "Additional tasks chained after the built-in scalafmt/scalafix steps when `fix` is invoked"
    }

    val fixCheckExtra = settingKey[Seq[NamedCheck]] {
      "Additional checks chained after the built-in scalafmt/scalafix checks when `fix --check` is invoked"
    }

    /** A named check in the `fix --check` pipeline.
      *
      * The `name` is what appears in the per-check banner and in the Markdown summary table; `task` is the underlying
      * check to run. When `aborts` is true, the failure of this check stops the rest of the pipeline (subsequent checks
      * are reported as Skipped); the default is `false` (continue-on-error).
      */
    final case class NamedCheck(name: String, task: Def.Initialize[Task[Unit]], aborts: Boolean = false) {

      /** Returns a copy of this check with the given display name. */
      def named(newName: String): NamedCheck = copy(name = newName)

      /** Returns a copy of this check with its underlying task replaced by the result of applying `f` to it.
        *
        * Useful for wrapping the task with `.acrossAggregated`, retries, timing, etc., without losing the check's
        * label.
        */
      def mapTask(f: Def.Initialize[Task[Unit]] => Def.Initialize[Task[Unit]]): NamedCheck = copy(task = f(task))

      /** Marks this check so its failure stops the rest of the `fix --check` / `ci` pipeline; subsequent checks are
        * reported as Skipped (⏭️) without being run.
        *
        * Useful for foundational checks (e.g. `compile`) where running downstream checks after a failure is pointless.
        */
      def abortOnError: NamedCheck = copy(aborts = true)

      /** Marks this check so its failure does not stop the pipeline (the default). Subsequent checks still run. */
      def continueOnError: NamedCheck = copy(aborts = false)

    }

    /** Adds combinators to any `Def.Initialize[Task[A]]` so it can be turned into a [[NamedCheck]] or fanned out across
      * all projects aggregated by the current one.
      */
    implicit class NamedCheckOps[A](private val task: Def.Initialize[Task[A]]) extends AnyVal {

      /** Wraps this task as a [[NamedCheck]] with the given display name. The task's result is discarded — the check
        * pipeline only cares whether the task succeeded.
        */
      def named(name: String): NamedCheck = NamedCheck(name, task.map(_ => ()))

      /** Evaluates this task at every project aggregated by the project where `fix` / `fix --check` is invoked.
        *
        * Gives `fix` and `fix --check` the same multi-project coverage that sbt's task aggregation would provide,
        * without relying on aggregation of the `fix` task itself.
        */
      def acrossAggregated: Def.Initialize[Task[Unit]] =
        Def.task(task.all(ScopeFilter(inAggregates(ThisProject))).value).map(_ => ())

    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin && ScalafmtPlugin

  /** Disables aggregation for the `fix` and [[autoImport.ci]] tasks so a single invocation produces a single
    * plan/banner/summary block. Multi-project coverage is achieved internally via
    * [[autoImport.NamedCheckOps.acrossAggregated]].
    */
  override def globalSettings: Seq[Def.Setting[_]] = Seq(
    fix / aggregate := false,
    ci / aggregate  := false
  )

  val parser = Def.setting(sbt.complete.DefaultParsers.spaceDelimited("--check | -c"))

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    fixExtra      := Seq.empty,
    fixCheckExtra := Seq.empty,
    fix           := InputTask
      .createDyn(InputTask.initParserAsInput(parser))(Def.task[Seq[String] => Def.Initialize[Task[Unit]]] {
        case Seq("--check") | Seq("-c") =>
          runChecks {
            scalafmtCheckAll.acrossAggregated.named("scalafmt") +:
              (Compile / scalafmtSbtCheck).acrossAggregated.named("scalafmt-sbt") +:
              scalafixAll.toTask(" --check").acrossAggregated.named("scalafix") +: fixCheckExtra.value
          }

        case Nil =>
          chain {
            scalafixAll.toTask("").acrossAggregated +:
              scalafmtAll.acrossAggregated +:
              (Compile / scalafmtSbt).acrossAggregated +:
              fixExtra.value
          }

        case args =>
          sys.error(s"Invalid argument `${args.mkString(" ")}`. The only argument allowed is `--check`")
      })
      .evaluated,
    ci := fix.toTask(" --check").value
  )

  ///////////
  // Utils //
  ///////////

  private def chain(tasks: Seq[Def.Initialize[Task[Unit]]]) = tasks.reduce(Def.sequential(_, _))

  /** True when running under CI (i.e. the `CI` env var is set). Gates the rich console/Markdown output produced by
    * [[runChecks]] so local invocations stay quiet.
    */
  private def isCI: Boolean = sys.env.contains("CI")

  sealed abstract private class CheckResult(val emoji: String, val level: Level.Value)

  private object CheckResult {

    case object Passed extends CheckResult("✅", Level.Info)

    case object Failed extends CheckResult("❌", Level.Error)

    case object Skipped extends CheckResult("⏭️", Level.Info)

  }

  /** Orchestrates a sequence of checks with continue-on-error semantics.
    *
    * Every check normally runs via `.result` so a failure does not abort the chain. A check tagged with `.abortOnError`
    * is the exception: when it fails, all subsequent checks are marked as Skipped without being run. After the chain
    * completes, aborts via `sys.error` if any check failed.
    *
    * When [[isCI]] is true, also prints the ordered plan, logs a banner before each check, logs a ✅ / ❌ / ⏭️ summary,
    * and writes a Markdown status table via [[writeMarkdownSummary]]. Local invocations stay quiet.
    */
  private def runChecks(checks: Seq[NamedCheck]): Def.Initialize[Task[Unit]] = Def.taskDyn {
    val log = streams.value.log

    if (isCI) {
      log.info("==> CI checks plan:")
      checks.foreach(check => log.info(s"  - ${check.name}"))
    }

    val collected: Def.Initialize[Task[List[(NamedCheck, CheckResult)]]] =
      checks.foldLeft(Def.task(List.empty[(NamedCheck, CheckResult)])) { (acc, check) =>
        Def.taskDyn {
          val previous = acc.value
          val mustSkip = previous.exists {
            case (c, CheckResult.Failed) => c.aborts
            case _                       => false
          }

          if (mustSkip) {
            if (isCI) log.info(s"==> ${check.name} (skipped)")

            Def.task(previous :+ (check -> CheckResult.Skipped))
          } else {
            Def.taskDyn {
              if (isCI) log.info(s"==> ${check.name}")

              check.task.map(_ => ()).result.map { r =>
                val outcome = r match {
                  case Value(_) => CheckResult.Passed
                  case Inc(_)   => CheckResult.Failed
                }

                previous :+ (check -> outcome)
              }
            }
          }
        }
      }

    Def.task {
      val results = collected.value

      if (isCI) {
        log.info("==> CI checks summary:")
        results.foreach { case (c, result) => log.log(result.level, s"  ${result.emoji}  ${c.name}") }
        writeMarkdownSummary((LocalRootProject / target).value, name.value, scalaVersion.value, results, log)
      }

      val failed = results.collect { case (c, CheckResult.Failed) => c.name }

      if (failed.nonEmpty)
        sys.error(s"${failed.size} check(s) failed: ${failed.mkString(", ")}")
    }
  }

  /** Writes the `fix --check` Markdown status table. Only invoked from [[runChecks]] when [[isCI]] is true.
    *
    * Appends to `$$GITHUB_STEP_SUMMARY` when that env var is set so the report surfaces on the GitHub Actions workflow
    * run page; otherwise writes to `fix-check-summary.md` under the build's root project `target` directory. The
    * project `name` and `scalaVersion` are included in the heading so per-project / cross-built runs produce distinct,
    * identifiable blocks.
    */
  private def writeMarkdownSummary(
      rootTarget: File,
      name: String,
      scalaVersion: String,
      results: Seq[(NamedCheck, CheckResult)],
      log: Logger
  ): Unit = {
    val resultsSummary = results.map { case (c, result) => s"| ${c.name} | ${result.emoji} |" }.mkString("\n")

    val summary =
      s"""# CI checks for $name ($scalaVersion)
         |
         || Check | Status |
         || ----- | ------ |
         |$resultsSummary""".stripMargin

    val output = sys.env
      .get("GITHUB_STEP_SUMMARY")
      .map(file(_))
      .getOrElse(rootTarget / "fix-check-summary.md")

    IO.append(output, summary)
    log.info(s"==> wrote summary to `${output.getAbsolutePath}`")
  }

}
