import de.johoop.jacoco4sbt._
import JacocoPlugin._
 
name := "conf-lib"

organization := "com.identityblitz"

version := "0.1.1-SNAPSHOT"

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

homepage := Some(url("https://github.com/brainysmith/conf-lib"))

scalaVersion := "2.10.3"

//Disable adding a scala version to artifact
crossPaths := false

publishMavenStyle := true

publishArtifact in Test := false

resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + "/.m2/repository"

val nexus = "http://build.reaxoft.loc/store/content/repositories"

credentials += Credentials("Sonatype Nexus Repository Manager", "build.reaxoft.loc", "deployment", "oracle_1")

publishTo <<= version { (v: String) =>
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "/blitz-dev")
  else
    Some("releases"  at nexus + "/blitz-dev")
}

libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.2.1",
    "org.scalatest" % "scalatest_2.10" % "2.0.1-SNAP" % "test",
    "org.scalacheck" %% "scalacheck" % "1.11.2" % "test"
)

scalacOptions ++= List("-feature","-deprecation", "-unchecked")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l", "org.scalatest.tags.Slow")

//Code Coverage section
jacoco.settings

//Style Check section 
org.scalastyle.sbt.ScalastylePlugin.Settings
 
org.scalastyle.sbt.PluginKeys.config <<= baseDirectory { _ / "src/main/config" / "scalastyle-config.xml" }
