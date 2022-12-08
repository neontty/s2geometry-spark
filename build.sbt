libraryDependencies += "com.google.geometry" % "s2-geometry" % "2.0.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.2.0" % "provided"
libraryDependencies += "com.swoop" %% "spark-alchemy" % "1.2.1"
//we're using swoop to hijack their cool function registration code. Thank you swoop.


name := "s2geometry-spark"

version := "0.2.0"

scalaVersion := "2.12"

