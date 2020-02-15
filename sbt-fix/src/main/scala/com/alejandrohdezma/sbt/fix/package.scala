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

package com.alejandrohdezma.sbt

import sbt._
import sbt.io.IO

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

    lazy val localContent  = IO.read(to)
    lazy val extraContent  = IO.read(including)
    lazy val remoteContent = header + "\n\n" + http.client.get(from)

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
