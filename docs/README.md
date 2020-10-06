# SBT plugin that enhances scalafix & scalafmt duo

[![][github-action-badge]][github-action] [![][maven-badge]][maven] [![][steward-badge]][steward]

> :exclamation: This project no longer enables synchronizing configuration across repositories. To enable that functionality, please check [alejandrohdezma/sbt-scalafix-defaults](https://github.com/alejandrohdezma/sbt-scalafix-defaults) and [alejandrohdezma/sbt-scalafmt-defaults](https://github.com/alejandrohdezma/sbt-scalafmt-defaults).
>
> If you want to provide your own organization's configuration you can use the previous repositories as templates and edit the `.scalafix.conf` and `.scalafmt.conf` at will.

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-fix" % "@VERSION@")
```

## Usage

This plugin adds a `fix` command to every project in the build.

This command can be used to launch both `scalafmt` and `scalafix` in all supported configurations.

```sbt
fix
```

It can also be used for checking that all files have been fixed with both tools, exiting with non-zero code on violations, by appending the `--check` argument (which can be used in CI to check formatting easily).

```sbt
fix --check
```

[github-action]: https://github.com/alejandrohdezma/sbt-fix/actions
[github-action-badge]: https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Falejandrohdezma%2Fsbt-fix%2Fbadge%3Fref%3Dmaster&style=flat

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-fix
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-fix/badge.svg?kill_cache=1

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=