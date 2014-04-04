import de.johoop.jacoco4sbt._
import JacocoPlugin._
 
name := "conf-lib"

organization := "com.identityblitz"

version := "1.0"

scalaVersion := "2.10.3"

crossPaths := false

libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.2.0",
    "org.scalatest" % "scalatest_2.10" % "2.0.1-SNAP" % "test",
    "org.scalacheck" %% "scalacheck" % "1.11.2" % "test"
)

scalacOptions ++= List("-feature","-deprecation", "-unchecked")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l", "org.scalatest.tags.Slow")

//Code Coverage section
jacoco.settings

//itJacoco.settings

//Style Check section 
org.scalastyle.sbt.ScalastylePlugin.Settings
 
org.scalastyle.sbt.PluginKeys.config <<= baseDirectory { _ / "src/main/config" / "scalastyle-config.xml" }
