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
  * In check mode the plugin runs every check even if earlier ones fail. When the `CI` environment variable is set, the
  * run also prints a labelled banner per check, logs a `PASS`/`FAIL` summary, and writes a Markdown status table to
  * `$$GITHUB_STEP_SUMMARY` (when set) or to `target/fix-check-summary.md`, so the report can be surfaced as a GitHub
  * Actions job summary. Local (non-CI) invocations stay quiet.
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
      * check to run.
      */
    final case class NamedCheck(name: String, task: Def.Initialize[Task[Unit]]) {

      /** Returns a copy of this check with the given display name. */
      def named(newName: String): NamedCheck = copy(name = newName)

      /** Returns a copy of this check with its underlying task replaced by the result of applying `f` to it.
        *
        * Useful for wrapping the task with `.acrossAggregated`, retries, timing, etc., without losing the check's
        * label.
        */
      def mapTask(f: Def.Initialize[Task[Unit]] => Def.Initialize[Task[Unit]]): NamedCheck = copy(task = f(task))

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
          runChecks(
            scalafmtCheckAll.acrossAggregated.named("scalafmt-check") +:
              (Compile / scalafmtSbtCheck).acrossAggregated.named("scalafmt-sbt-check") +:
              scalafixAll.toTask(" --check").acrossAggregated.named("scalafix-check") +: fixCheckExtra.value
          )

        case Nil =>
          chain(
            scalafixAll.toTask("").acrossAggregated +:
              scalafmtAll.acrossAggregated +:
              (Compile / scalafmtSbt).acrossAggregated +:
              fixExtra.value
          )

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

  /** Orchestrates a sequence of checks with continue-on-error semantics.
    *
    * Every check runs via `.result` so a failure does not abort the chain. After all checks complete, aborts via
    * `sys.error` if any check failed.
    *
    * When [[isCI]] is true, also prints the ordered plan, logs a banner before each check, logs a `PASS`/`FAIL`
    * summary, and writes a Markdown status table via [[writeMarkdownSummary]]. Local invocations stay quiet.
    */
  private def runChecks(checks: Seq[NamedCheck]): Def.Initialize[Task[Unit]] = Def.taskDyn {
    val log = streams.value.log

    if (isCI) {
      log.info("==> CI checks plan:")
      checks.foreach(check => log.info(s"  - ${check.name}"))
    }

    val tagged: Seq[Def.Initialize[Task[(String, Result[Unit])]]] = checks.map { check =>
      Def.taskDyn {
        if (isCI) {
          log.info(s"==> ${check.name}")
        }
        check.task.map(_ => ()).result.map(r => check.name -> r)
      }
    }

    val collected = tagged.foldLeft(Def.task(List.empty[(String, Result[Unit])])) { (acc, next) =>
      Def.taskDyn {
        val previous = acc.value
        next.map(r => previous :+ r)
      }
    }

    Def.task {
      val results = collected.value
      val failed  = results.collect { case (name, Inc(_)) => name }
      val passed  = results.collect { case (name, Value(_)) => name }

      if (isCI) {
        log.info("==> CI checks summary:")
        passed.foreach(name => log.info(s"  PASS  $name"))
        failed.foreach(name => log.error(s"  FAIL  $name"))

        writeMarkdownSummary((LocalRootProject / target).value, scalaVersion.value, results, log)
      }

      if (failed.nonEmpty)
        sys.error(s"${failed.size} check(s) failed: ${failed.mkString(", ")}")
    }
  }

  /** Writes the `fix --check` Markdown status table. Only invoked from [[runChecks]] when [[isCI]] is true.
    *
    * Appends to `$$GITHUB_STEP_SUMMARY` when that env var is set so the report surfaces on the GitHub Actions workflow
    * run page; otherwise writes to `fix-check-summary.md` under the build's root project `target` directory. The
    * `scalaVersion` is included in the heading so cross-built runs produce distinct, identifiable blocks.
    */
  private def writeMarkdownSummary(
      rootTarget: File,
      scalaVersion: String,
      results: Seq[(String, Result[Unit])],
      log: Logger
  ): Unit = {
    val md = new StringBuilder
    md.append(s"# CI checks ($scalaVersion)\n\n")
    md.append("| Check | Status |\n| --- | --- |\n")

    results.foreach {
      case (name, Value(_)) => md.append(s"| $name | PASS |\n")
      case (name, Inc(_))   => md.append(s"| $name | FAIL |\n")
    }

    val output = sys.env
      .get("GITHUB_STEP_SUMMARY")
      .map(file(_))
      .getOrElse(rootTarget / "fix-check-summary.md")

    IO.append(output, md.toString)
    log.info(s"==> wrote summary to `${output.getAbsolutePath}`")
  }

}
