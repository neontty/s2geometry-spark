ThisBuild / organization := "io.github.neontty"
ThisBuild / organizationName := "neontty"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/neontty/s2geometry-spark"),
    "scm:git@github.com:neontty/s2geometry-spark.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "neontty",
    name  = "RJ Marcus",
    email = "neontty@gmail.com",
    url   = url("https://github.com/neontty")
  )
)

ThisBuild / description := "Lightweight wrapper for s2geometry-java to expose as spark native functions with convenience functions."
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/neontty/s2geometry-spark"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://oss.sonatype.org/service/local"

ThisBuild / publishMavenStyle := true

ThisBuild / versionScheme := Some("early-semver")

ThisBuild / publishConfiguration := publishConfiguration.value.withOverwrite(true)
ThisBuild / publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
