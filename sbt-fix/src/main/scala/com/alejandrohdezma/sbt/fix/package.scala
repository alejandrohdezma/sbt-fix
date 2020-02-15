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

package com.alejandrohdezma.sbt

import sbt._
import sbt.io.IO

import scalaj.http.Http
import scalaj.http.HttpOptions.followRedirects

package object fix {

  /** Extracts from the state the list of configurations with the scalafix task */
  def configsWithScalafix(state: State): List[String] =
    Project
      .extract(state)
      .currentProject
      .settings
      .toList
      .map(_.key)
      .filter(_.key.label.contentEquals("scalafix"))
      .flatMap(_.scope.config.toOption.map(_.name).toList)

  /**
   *  Copies the content of a remote URL into the provided `to` file and includes the data
   *  from the `including` file if one is found.
   *
   * @param from the URL from which to download the data
   * @param to the file where data will be written
   * @param including an extra file whose content should be appended to the downloaded ones
   * @return the new local file
   */
  @SuppressWarnings(Array("scalafix:Disable.exists"))
  def copyRemoteFile(log: (=> String) => Unit)(from: String, to: File, including: File): File = {
    val header =
      s"""# This file has been automatically generated and should 
         |# not be edited nor added to source control systems.
         |# Extra configurations can be added to ${including.name}""".stripMargin

    lazy val localContent = IO.read(to)
    lazy val extraContent = IO.read(including)
    lazy val remoteContent = header + "\n\n" + Http(from)
      .option(followRedirects(true))
      .asString
      .body

    lazy val download     = log(s"Downloading $to from $from ...") → IO.write(to, remoteContent)
    lazy val includeExtra = log(s"Appending $including ...")       → IO.append(to, s"\n\n$extraContent")

    lazy val isUpdated          = !localContent.contentEquals(remoteContent)
    lazy val isUpdatedWithExtra = !localContent.contentEquals(s"$remoteContent\n\n$extraContent")

    (to.exists(), including.exists()) match {
      case (false, true)                      => download → includeExtra
      case (false, false)                     => download
      case (true, false) if isUpdated         => download
      case (true, true) if isUpdatedWithExtra => download → includeExtra
      case (_, _)                             => ()
    }

    to
  }

}
