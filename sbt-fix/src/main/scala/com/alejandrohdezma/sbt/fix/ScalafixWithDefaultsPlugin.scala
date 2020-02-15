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
import sbt.{Command, Def, addCompilerPlugin, _}

import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._

/**
 * This plugin enables Scalafix plugin with several extra rules,
 * downloads configuration from remote url and writes it to file
 * so Scalafix can use it.
 *
 * Also adds a `scalafixAll` new command that launches scalafix
 * in all available configurations. All scalafix arguments can
 * be used.
 */
object ScalafixWithDefaultsPlugin extends AutoPlugin {

  object autoImport {

    lazy val scalafixConfigLocation: SettingKey[String] = settingKey[String] {
      s"Location of the remote scalafix config. Defaults to ${Defaults.scalafix}"
    }

    lazy val scalafixExtraConfig: SettingKey[File] = settingKey[File] {
      "Path to a scalafix configuration file to specify extra configurations to be appended to " +
        "the downloaded ones. Defaults to a \".scalafix-extra.conf\" file in the base directory"
    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin

  override def buildSettings: Seq[Setting[_]] = Seq(
    scalafixExtraConfig               := file(".scalafix-extra.conf"),
    scalafixConfig                    := scalafixDownloadConfig.value,
    scalafixConfigLocation            := Defaults.scalafix,
    scalacOptions                     += "-P:semanticdb:synthetics:on",
    scalafixDependencies in ThisBuild ++= scalafixDefaultRules,
    addCompilerPlugin(scalafixSemanticdb)
  )

  override def projectSettings: Seq[Def.Setting[_]] = Seq(commands += scalafixAll)

  private lazy val scalafixAll: Command = Command.args("scalafixAll", "<rule>") { (state, args) =>
    val command = configsWithScalafix(state)
      .map(c => s"$c:scalafix${args.foldLeft("")(_ + " " + _)}")
      .mkString("; ")

    Command.process(command, state)
  }

  private lazy val scalafixDownloadConfig = Def.setting {
    Some(
      copyRemoteFile(sLog.value.info)(
        from = scalafixConfigLocation.value,
        to = scalafixConfig.value.getOrElse(file(".scalafix.conf")),
        including = scalafixExtraConfig.value
      )
    )
  }

  private lazy val scalafixDefaultRules: Seq[ModuleID] = Seq(
    "com.github.vovapolu" %% "scaluzzi"         % "0.1.3",
    "com.nequissimus"     %% "sort-imports"     % "0.3.0",
    "com.eed3si9n.fix"    %% "scalafix-noinfer" % "0.1.0-M1"
  )

}
