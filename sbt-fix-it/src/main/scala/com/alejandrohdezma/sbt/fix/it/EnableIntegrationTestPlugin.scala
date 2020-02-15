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

package com.alejandrohdezma.sbt.fix.it

import sbt.PluginTrigger
import sbt.Plugins
import sbt._
import sbt.librarymanagement.Configuration

import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport.scalafixConfigSettings

/**
 * This plugin activates the `IntegrationTest` configuration by
 * default in all projects and adds the scalafix/scalafmt
 * settings for the `it` configuration.
 *
 * If this is not the desired outcome for a certain project add:
 *
 * {{{
 * .disablePlugins(EnableIntegrationTestPlugin)
 * }}}
 */
object EnableIntegrationTestPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = ScalafmtPlugin && ScalafixPlugin

  override def projectConfigurations: Seq[Configuration] = Seq(IntegrationTest)

  override def projectSettings: Seq[Def.Setting[_]] = inConfig(IntegrationTest) {
    Defaults.testSettings ++ scalafixConfigSettings(IntegrationTest) ++ scalafmtConfigSettings
  }

}
