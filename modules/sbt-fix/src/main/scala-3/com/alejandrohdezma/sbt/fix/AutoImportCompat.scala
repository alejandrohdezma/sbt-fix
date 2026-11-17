/*
 * Copyright 2019-2026 Alejandro Hernández <https://github.com/alejandrohdezma>
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

import sbt.*

/** sbt 2 specific members of [[FixCommandPlugin.autoImport]]. */
trait AutoImportCompat {

  /** Makes `fixExtra += someTask` compile on sbt 2.
    *
    * sbt 2's `+=` treats a `Def.Initialize[Task[Unit]]` argument as a dependency and appends its value, a `Task[Unit]`,
    * so this instance wraps that task back into a `Def.Initialize`.
    *
    * It is an `implicit` rather than a `given` because `.sbt` files import `autoImport` with a wildcard, which does not
    * import givens.
    */
  implicit def fixExtraAppendTask: Append.Value[Seq[Def.Initialize[Task[Unit]]], Task[Unit]] =
    (tasks, task) => tasks :+ Def.valueStrict(task)

}
