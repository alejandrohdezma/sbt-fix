package com.alejandrohdezma.sbt.fix

/** Contains defaults URLs for scalafix/scalafmt config files */
object Defaults {

  /**
   * Don't change this value. It's automatically updated
   * from `sbt-fix-defaults` releases.
   */
  private val version = "v0.0.3"

  val scalafix: String =
    s"https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/$version/default.scalafix.conf"

  val scalafmt: String =
    s"https://github.com/alejandrohdezma/sbt-fix-defaults/releases/download/$version/default.scalafmt.conf"

}
