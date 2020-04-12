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

package com.alejandrohdezma.sbt.fix.http

import java.net.{HttpURLConnection, URL}
import java.util.concurrent.ConcurrentHashMap

import scala.io.Source

@SuppressWarnings(Array("all"))
object client {

  /** Calls the provided URL and returns its contents as `String` */
  def get(url: URL): String = cache.computeIfAbsent(
    url, { _ =>
      val connection = url.openConnection.asInstanceOf[HttpURLConnection]

      connection.setInstanceFollowRedirects(true)

      val inputStream = connection.getInputStream

      Source.fromInputStream(inputStream, "UTF-8").mkString
    }
  )

  private val cache: ConcurrentHashMap[URL, String] = new ConcurrentHashMap[URL, String]()

}
