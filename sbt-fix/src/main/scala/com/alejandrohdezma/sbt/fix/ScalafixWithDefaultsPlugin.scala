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

import scala.sys.process._

import sbt.Keys._
import sbt._

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

    lazy val scalafixConfigLocation: SettingKey[URL] = settingKey[URL] {
      s"Location of the remote scalafix config"
    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin

  override def buildSettings: Seq[Setting[_]] = Seq(
    SettingKey[Unit]("generateScalafixConfig") := generateConfig.value,
    scalafixConfig                             := Some(config),
    scalacOptions                              += "-P:semanticdb:synthetics:on",
    scalafixDependencies in ThisBuild          ++= scalafixDefaultRules,
    addCompilerPlugin(scalafixSemanticdb)
  )

  override def projectSettings: Seq[Def.Setting[_]] = Seq(commands += scalafixAll)

  private lazy val scalafixAll: Command = Command.args("scalafixAll", "<rule>") { (state, args) =>
    val command = configsWithScalafix(state)
      .map(c => s"$c:scalafix${args.foldLeft("")(_ + " " + _)}")
      .mkString("; ")

    Command.process(command, state)
  }

  private val config = file(".scalafix.conf")

  @SuppressWarnings(Array("scalafix:Disable.exists"))
  private lazy val generateConfig = Def.setting {
    val including = file(".scalafix-extra.conf")
    val remote    = scalafixConfigLocation.value

    if (including.exists)
      (remote #> config #&& (including #>> config)).!
    else (remote #> config).!

    ()
  }

  lazy val scalafixDefaultRules: Seq[ModuleID] = Seq(
    "com.github.vovapolu" %% "scaluzzi"         % "0.1.5",
    "com.nequissimus"     %% "sort-imports"     % "0.3.2",
    "com.eed3si9n.fix"    %% "scalafix-noinfer" % "0.1.0-M1"
  )

}
