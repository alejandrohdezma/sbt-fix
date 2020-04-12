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

import sbt._

import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._

/**
 * This plugin enables Scalafmt plugin, downloads configuration
 * from remote url and writes it to file so Scalafmt can use it.
 */
object ScalafmtWithDefaultsPlugin extends AutoPlugin {

  object autoImport {

    lazy val generateScalafmtConfig: SettingKey[Unit] = settingKey[Unit] {
      "Generates scalafmt aggregated config"
    }

    lazy val scalafmtConfigLocation: SettingKey[URL] = settingKey[URL] {
      s"Location of the remote scalafmt config"
    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafmtPlugin

  override def buildSettings: Seq[Setting[_]] =
    Seq(
      generateScalafmtConfig := generateConfig.value,
      scalafmtConfig         := config
    )

  private val config = file(".scalafmt.conf")

  @SuppressWarnings(Array("scalafix:Disable.exists"))
  private lazy val generateConfig: Def.Initialize[Unit] = Def.setting {
    val including = file(".scalafmt-extra.conf")
    val remote    = scalafmtConfigLocation.value

    if (including.exists) (remote #> config #&& (including #>> config)).!
    else (remote #> config).!

    ()
  }

}
