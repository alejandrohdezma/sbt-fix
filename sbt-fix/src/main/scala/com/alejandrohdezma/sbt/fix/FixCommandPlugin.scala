/*
 * Copyright (c) 2019-2020 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.alejandrohdezma.sbt.fix

import sbt.Keys._
import sbt.{Command, Def, _}

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

      Command.process(s"all scalafmtAll scalafmtSbt; $scalafixCommand", state)
    case (state, Nil) =>
      val scalafixCommand = configsWithScalafix(state)
        .map(c => s"$c:scalafix")
        .mkString(" ")

      Command.process(s"all scalafmtAll scalafmtSbt; all $scalafixCommand", state)
    case (state, args) =>
      state.log.error(s"Invalid argument `${args.mkString(" ")}`")
      state.log.error(s"The only argument allowed is `--check`")
      state.fail
  }

}
