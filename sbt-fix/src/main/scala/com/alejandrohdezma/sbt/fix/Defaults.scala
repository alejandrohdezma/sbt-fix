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

/** Contains defaults URLs for scalafix/scalafmt config files */
object Defaults {

  /**
   * Don't change this value. It's automatically updated
   * from `sbt-fix-defaults` releases.
   */
  private val version = "v0.0.5"

  val scalafix: String =
    s"https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/$version/default.scalafix.conf"

  val scalafmt: String =
    s"https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/$version/default.scalafmt.conf"

}
