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
  * Additional tasks can be plugged in via the `fixExtra` / `fixCheckExtra` settings; they run after the built-in
  * scalafmt/scalafix steps.
  */
object FixCommandPlugin extends AutoPlugin {

  object autoImport {

    val fix = inputKey[Unit]("Launch both scalafmt and scalafix in all supported configurations")

    val fixExtra = settingKey[Seq[Def.Initialize[Task[Unit]]]](
      "Additional tasks chained after the built-in scalafmt/scalafix steps when `fix` is invoked"
    )

    val fixCheckExtra = settingKey[Seq[Def.Initialize[Task[Unit]]]](
      "Additional tasks chained after the built-in scalafmt/scalafix checks when `fix --check` is invoked"
    )

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin && ScalafmtPlugin

  val parser = Def.setting(sbt.complete.DefaultParsers.spaceDelimited("--check | -c"))

  private def chain(tasks: Seq[Def.Initialize[Task[Unit]]]) = tasks.reduce(Def.sequential(_, _))

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    fixExtra      := Seq.empty,
    fixCheckExtra := Seq.empty,
    fix           := InputTask
      .createDyn(InputTask.initParserAsInput(parser))(Def.task[Seq[String] => Def.Initialize[Task[Unit]]] {
        case Seq("--check") | Seq("-c") =>
          chain {
            ThisProject / scalafmtCheckAll +: Compile / scalafmtSbtCheck +: scalafixAll.toTask(" --check") +:
              fixCheckExtra.value
          }

        case Nil =>
          chain(scalafixAll.toTask("") +: ThisProject / scalafmtAll +: Compile / scalafmtSbt +: fixExtra.value)

        case args =>
          sys.error(s"Invalid argument `${args.mkString(" ")}`. The only argument allowed is `--check`")
      })
      .evaluated
  )

}
