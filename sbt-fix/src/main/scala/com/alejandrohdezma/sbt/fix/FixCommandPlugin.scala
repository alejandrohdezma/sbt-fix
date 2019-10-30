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
