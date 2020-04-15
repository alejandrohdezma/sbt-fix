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

import scalafix.sbt.ScalafixPlugin

/**
 * This plugin adds a `scalafixAll` new command that launches scalafix
 * in all available configurations. All scalafix arguments can
 * be used.
 */
object ScalafixAllCommandPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafixPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq {
    commands += Command.args("scalafixAll", "<rule>") { (state, args) =>
      val command = configsWithScalafix(state)
        .map(c => s"$c:scalafix${args.foldLeft("")(_ + " " + _)}")
        .mkString("; ")

      Command.process(command, state)
    }
  }

}
