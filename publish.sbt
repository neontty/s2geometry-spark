ThisBuild / organization := "io.github.neontty"
ThisBuild / organizationName := "neontty"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/awwsmm/zepto"),
    "scm:git@github.com:awwsmm/zepto.git"
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
ThisBuild / licenses := List("apache-2.0" -> new URL("https://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/neontty/s2geometry-spark"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

ThisBuild / publishMavenStyle := true

ThisBuild / versionScheme := Some("early-semver")

