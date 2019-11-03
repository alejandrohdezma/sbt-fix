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
