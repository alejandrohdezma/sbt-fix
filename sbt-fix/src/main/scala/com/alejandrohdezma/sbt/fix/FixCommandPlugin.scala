/*
 * Copyright 2019-2020 Alejandro Hernández <https://github.com/alejandrohdezma>
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
import scalafix.sbt.ScalafixPlugin

/**
 * Adds a `fix` command to every project in the build.
 *
 * This command can be used to launch both scalafmt and scalafix in all
 * supported configurations.
 *
 * It can also be used for checking that all files have been fixed
 * with both tools, exiting with non-zero code on violations, by appending
 * the `--check` argument.
 */
object FixCommandPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin && ScalafmtPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(commands += fix)

  private lazy val fix: Command = Command.args("fix", "--check") {
    case (state, Seq("--check")) =>
      val scalafixCommand = configsWithScalafix(state)
        .map(c => s"$c:scalafix --check")
        .mkString("; ")

      Command.process(s"all scalafmtCheckAll scalafmtSbtCheck; $scalafixCommand", state)
    case (state, Nil) =>
      val scalafixCommand = configsWithScalafix(state)
        .map(c => s"$c:scalafix")
        .mkString(" ")

      Command.process(s"all $scalafixCommand; all scalafmtAll scalafmtSbt", state)
    case (state, args) =>
      state.log.error(s"Invalid argument `${args.mkString(" ")}`")
      state.log.error(s"The only argument allowed is `--check`")
      state.fail
  }

}
