# SBT plugin that enhances scalafix & scalafmt duo

[![][travis-badge]][travis] [![][maven-badge]][maven] [![][steward-badge]][steward] [![][mergify-badge]][mergify]

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" %% "sbt-fix" % "0.1.0")
```

## Usage

All included plugins are automatically activated, so you don't have to do anything to start using them, however, some of them provide new features or improve existing ones.

### Download scalafix/scalafmt configuration from the cloud

One of the main reasons of this plugin is to keep scalafix/scalafmt configurations synchronized across projects. To do so, both configurations are automatically downloaded from a remote location. By default both configurations are downloaded from [alejandrohdezma/sbt-fix-defaults](https://github.com/alejandrohdezma/sbt-fix-defaults).

#### Edit configuration location

To download configurations from a different location, edit `scalafixConfigLocation` and `scalafmtConfigLocation` to a valid url:

```sbt
scalafixConfigLocation := "your-url"
scalafmtConfigLocation := "your-url"
```  

#### Adding extra configurations

Extra configurations can be added without needing to alter the original file, just by adding a `.scalafix-extra.conf`, for scalafix, or `.scalafmt-extra.conf`, for scalafmt, and adding there your extra configurations.

For example, for adding a new scalafix rule:

```hocon
# .scalafix-extra.conf

rules += MyNewRule
```

> The location of this files can be altered by using the `scalafixExtraConfig` and `scalafmtExtraConfig` settings.

### Running scalafix in all configurations

Enabling this plugin also adds a few interesting commands that aren't available in the original plugins. For example, executing the `scalafix` task in all the available configurations. This can be done by executing the `scalafixAll` command:

```sbt
scalafixAll
```

Also, this command accepts the same arguments as `scalafix`, so we can:

```sbt
// Execute a single rule
scalafixAll DisableSyntax

// Check without altering files
scalafixAll --check

// Insert /* scalafix:ok */ suppressions instead of reporting linter errors.
scalafixAll --auto-suppress-linter-errors
```

> For all the other possible arguments, refer to [scalafix docs](https://scalacenter.github.io/scalafix/docs/users/installation.html#help)

### Running both scalafix & scalafmt in a single command

This plugin also enables a `fix` command to every project in the build.

This command can be used to launch both scalafmt and scalafix in all supported configurations.

```sbt
fix
```

It can also be used for checking that all files have been fixed with both tools, exiting with non-zero code on violations, by appending the `--check` argument.

```sbt
fix --check
```

Which can be used in CI to check formatting easily.

[travis]: https://travis-ci.com/alejandrohdezma/sbt-fix
[travis-badge]: https://travis-ci.com/alejandrohdezma/sbt-fix.svg?branch=master

[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-fix
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-fix/badge.svg?kill_cache=1

[mergify]: https://mergify.io
[mergify-badge]: https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/alejandrohdezma/sbt-fix&style=flat

[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=