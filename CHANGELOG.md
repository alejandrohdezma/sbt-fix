# Changelog

## [Unreleased](https://github.com/alejandrohdezma/sbt-fix/tree/HEAD)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.4.0...HEAD)

⚠️ **Breaking changes**

- `scalafixDependencies` should be added by config providers [\#91](https://github.com/alejandrohdezma/sbt-fix/pull/91) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make extra config location a fixed value [\#88](https://github.com/alejandrohdezma/sbt-fix/pull/88) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Use URL instead of String for config locations [\#85](https://github.com/alejandrohdezma/sbt-fix/pull/85) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Remove default values from `sbt-fix` [\#80](https://github.com/alejandrohdezma/sbt-fix/pull/80) ([alejandrohdezma](https://github.com/alejandrohdezma))

🚀 **Features**

- Add `sbt-remove-test-from-pom` plugin [\#74](https://github.com/alejandrohdezma/sbt-fix/pull/74) ([alejandrohdezma](https://github.com/alejandrohdezma))

📘 **Documentation**

- Fix Github Actions badge [\#79](https://github.com/alejandrohdezma/sbt-fix/pull/79) ([alejandrohdezma](https://github.com/alejandrohdezma))

📈 **Dependency updates**

- Update sbt-header to 5.5.0 [\#76](https://github.com/alejandrohdezma/sbt-fix/pull/76) ([scala-steward](https://github.com/scala-steward))
- Update sbt-github-header, sbt-github-mdoc to 0.7.0 [\#72](https://github.com/alejandrohdezma/sbt-fix/pull/72) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.5 [\#70](https://github.com/alejandrohdezma/sbt-fix/pull/70) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalafix to 0.9.13 [\#69](https://github.com/alejandrohdezma/sbt-fix/pull/69) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.9 [\#65](https://github.com/alejandrohdezma/sbt-fix/pull/65) ([scala-steward](https://github.com/scala-steward))
- Update scaluzzi to 0.1.5 [\#64](https://github.com/alejandrohdezma/sbt-fix/pull/64) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalafix to 0.9.12 [\#63](https://github.com/alejandrohdezma/sbt-fix/pull/63) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.3 [\#62](https://github.com/alejandrohdezma/sbt-fix/pull/62) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalafmt to 2.3.2 [\#60](https://github.com/alejandrohdezma/sbt-fix/pull/60) ([scala-steward](https://github.com/scala-steward))
- Update sbt-github-header, sbt-github-mdoc to 0.6.0 [\#57](https://github.com/alejandrohdezma/sbt-fix/pull/57) ([scala-steward](https://github.com/scala-steward))
- Update scaluzzi to 0.1.4.1 [\#53](https://github.com/alejandrohdezma/sbt-fix/pull/53) ([scala-steward](https://github.com/scala-steward))

## [v0.4.0](https://github.com/alejandrohdezma/sbt-fix/tree/v0.4.0) (2020-02-28)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.3.1...v0.4.0)

🚀 **Features**

- First run scalafix, then scalafmt [\#44](https://github.com/alejandrohdezma/sbt-fix/pull/44) ([sideeffffect](https://github.com/sideeffffect))
- Support a pretty simple offline mode [\#42](https://github.com/alejandrohdezma/sbt-fix/pull/42) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Ensure we get notified of new scalafix rules versions by scala-steward [\#40](https://github.com/alejandrohdezma/sbt-fix/pull/40) ([alejandrohdezma](https://github.com/alejandrohdezma))

🐛 **Bug Fixes**

- `fix --check` should call check scalafmt commands [\#39](https://github.com/alejandrohdezma/sbt-fix/pull/39) ([alejandrohdezma](https://github.com/alejandrohdezma))

📈 **Dependency updates**

- Update sort-imports to 0.3.2 [\#41](https://github.com/alejandrohdezma/sbt-fix/pull/41) ([scala-steward](https://github.com/scala-steward))

## [v0.3.1](https://github.com/alejandrohdezma/sbt-fix/tree/v0.3.1) (2020-02-26)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.3.0...v0.3.1)

📈 **Dependency updates**

- Update sbt-tpolecat to 0.1.11 [\#36](https://github.com/alejandrohdezma/sbt-fix/pull/36) ([scala-steward](https://github.com/scala-steward))
- Update sbt-github-header, sbt-github-mdoc to 0.5.2 [\#32](https://github.com/alejandrohdezma/sbt-fix/pull/32) ([scala-steward](https://github.com/scala-steward))

## [v0.3.0](https://github.com/alejandrohdezma/sbt-fix/tree/v0.3.0) (2020-02-15)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.2.6...v0.3.0)

🚀 **Features**

- Remove dependency on `scalaj-http` [\#28](https://github.com/alejandrohdezma/sbt-fix/pull/28) ([alejandrohdezma](https://github.com/alejandrohdezma))

📈 **Dependency updates**

- Update sbt-mdoc-toc to 0.2 [\#35](https://github.com/alejandrohdezma/sbt-fix/pull/35) ([scala-steward](https://github.com/scala-steward))
- Update sbt-ci-release to 1.5.2 [\#25](https://github.com/alejandrohdezma/sbt-fix/pull/25) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.8 [\#19](https://github.com/alejandrohdezma/sbt-fix/pull/19) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalafmt to 2.3.1 [\#18](https://github.com/alejandrohdezma/sbt-fix/pull/18) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.7 [\#17](https://github.com/alejandrohdezma/sbt-fix/pull/17) ([scala-steward](https://github.com/scala-steward))

## [v0.2.6](https://github.com/alejandrohdezma/sbt-fix/tree/v0.2.6) (2020-01-07)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.2.5...v0.2.6)

📈 **Dependency updates**

- Update sbt-mdoc to 2.1.1 [\#16](https://github.com/alejandrohdezma/sbt-fix/pull/16) ([scala-steward](https://github.com/scala-steward))
- Update sbt-ci-release to 1.5.0 [\#15](https://github.com/alejandrohdezma/sbt-fix/pull/15) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.0 [\#14](https://github.com/alejandrohdezma/sbt-fix/pull/14) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.6 [\#13](https://github.com/alejandrohdezma/sbt-fix/pull/13) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.5 [\#12](https://github.com/alejandrohdezma/sbt-fix/pull/12) ([scala-steward](https://github.com/scala-steward))

## [v0.2.5](https://github.com/alejandrohdezma/sbt-fix/tree/v0.2.5) (2019-12-11)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.2.4...v0.2.5)

📈 **Dependency updates**

- Update sbt-scalafmt to 2.3.0 [\#11](https://github.com/alejandrohdezma/sbt-fix/pull/11) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.0.3 [\#10](https://github.com/alejandrohdezma/sbt-fix/pull/10) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalafix to 0.9.11 [\#9](https://github.com/alejandrohdezma/sbt-fix/pull/9) ([scala-steward](https://github.com/scala-steward))
- Update sbt-tpolecat to 0.1.10 [\#8](https://github.com/alejandrohdezma/sbt-fix/pull/8) ([scala-steward](https://github.com/scala-steward))
- Update sbt-tpolecat to 0.1.9 [\#7](https://github.com/alejandrohdezma/sbt-fix/pull/7) ([scala-steward](https://github.com/scala-steward))
- Update sbt-scalafix to 0.9.9 [\#6](https://github.com/alejandrohdezma/sbt-fix/pull/6) ([scala-steward](https://github.com/scala-steward))
- Update sbt to 1.3.4 [\#5](https://github.com/alejandrohdezma/sbt-fix/pull/5) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.0.2 [\#4](https://github.com/alejandrohdezma/sbt-fix/pull/4) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.0.1 [\#3](https://github.com/alejandrohdezma/sbt-fix/pull/3) ([scala-steward](https://github.com/scala-steward))

## [v0.2.4](https://github.com/alejandrohdezma/sbt-fix/tree/v0.2.4) (2019-11-06)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.2.3...v0.2.4)

## [v0.2.3](https://github.com/alejandrohdezma/sbt-fix/tree/v0.2.3) (2019-11-04)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.2.2...v0.2.3)

## [v0.2.2](https://github.com/alejandrohdezma/sbt-fix/tree/v0.2.2) (2019-11-04)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.2.1...v0.2.2)

## [v0.2.1](https://github.com/alejandrohdezma/sbt-fix/tree/v0.2.1) (2019-11-02)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.2.0...v0.2.1)

📈 **Dependency updates**

- Update sbt-scalafix to 0.9.8 [\#2](https://github.com/alejandrohdezma/sbt-fix/pull/2) ([scala-steward](https://github.com/scala-steward))

## [v0.2.0](https://github.com/alejandrohdezma/sbt-fix/tree/v0.2.0) (2019-10-30)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.1.1...v0.2.0)

📈 **Dependency updates**

- Update sbt-me to 0.2.0 [\#1](https://github.com/alejandrohdezma/sbt-fix/pull/1) ([scala-steward](https://github.com/scala-steward))

## [v0.1.1](https://github.com/alejandrohdezma/sbt-fix/tree/v0.1.1) (2019-10-25)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/v0.1.0...v0.1.1)

## [v0.1.0](https://github.com/alejandrohdezma/sbt-fix/tree/v0.1.0) (2019-10-25)

[Full Changelog](https://github.com/alejandrohdezma/sbt-fix/compare/b3361462620559bb6af5c3a2aea07fd7e618c823...v0.1.0)



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
