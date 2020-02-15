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

    lazy val scalafmtConfigLocation: SettingKey[String] = settingKey[String] {
      s"Location of the remote scalafmt config. Defaults to ${Defaults.scalafmt}"
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
      scalafmtConfigLocation := Defaults.scalafmt,
      scalafmtExtraConfig    := file(".scalafmt-extra.conf"),
      scalafmtConfig         := scalafmtDownloadConfig.value
    )

  private lazy val scalafmtDownloadConfig: Def.Initialize[Task[File]] = Def.task {
    copyRemoteFile(sLog.value.info)(
      from = scalafmtConfigLocation.value,
      to = scalafmtConfig.value,
      including = scalafmtExtraConfig.value
    )
  }

}
