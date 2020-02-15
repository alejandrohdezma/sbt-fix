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

/** Contains defaults URLs for scalafix/scalafmt config files */
object Defaults {

  /**
   * Don't change this value. It's automatically updated
   * from `sbt-fix-defaults` releases.
   */
  private val version = "v0.0.4"

  val scalafix: String =
    s"https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/$version/default.scalafix.conf"

  val scalafmt: String =
    s"https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/$version/default.scalafmt.conf"

}
