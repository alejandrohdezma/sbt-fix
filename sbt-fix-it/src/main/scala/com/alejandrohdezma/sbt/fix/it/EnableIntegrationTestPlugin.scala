package com.alejandrohdezma.sbt.fix.it

import sbt.PluginTrigger
import sbt.Plugins
import sbt._
import sbt.librarymanagement.Configuration

import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport.scalafixConfigSettings

object EnableIntegrationTestPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafmtPlugin && ScalafixPlugin

  override def projectConfigurations: Seq[Configuration] = Seq(IntegrationTest)

  override def projectSettings: Seq[Def.Setting[_]] = inConfig(IntegrationTest) {
    Defaults.testSettings ++ scalafixConfigSettings(IntegrationTest) ++ scalafmtConfigSettings
  }

}
