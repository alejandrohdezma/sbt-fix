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

import sbt.Keys.sLog
import sbt._

import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._

/**
 * This plugin enables Scalafmt plugin, downloads configuration
 * from remote url and writes it to file so Scalafmt can use it.
 */
object ScalafmtWithDefaultsPlugin extends AutoPlugin {

  object autoImport {

    lazy val scalafmtConfigLocation: SettingKey[URL] = settingKey[URL] {
      s"Location of the remote scalafmt config"
    }

    lazy val scalafmtExtraConfig: SettingKey[File] = settingKey[File] {
      "Path to a scalafmt configuration file to specify extra configurations to be appended to " +
        "the downloaded ones. Defaults to a \".scalafmt-extra.conf\" file in the base directory"
    }

  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafmtPlugin

  override def buildSettings: Seq[Setting[_]] =
    Seq(
      scalafmtExtraConfig := file(".scalafmt-extra.conf"),
      scalafmtConfig      := scalafmtDownloadConfig.value
    )

  private lazy val scalafmtDownloadConfig: Def.Initialize[Task[File]] = Def.task {
    copyRemoteFile(sLog.value.info)(
      from = scalafmtConfigLocation.value,
      to = scalafmtConfig.value,
      including = scalafmtExtraConfig.value
    )
  }

}
