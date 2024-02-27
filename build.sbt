import play.sbt.PlayScala
import play.core.PlayVersion
import sbt.Keys.{testOptions, _}
import sbt.Tests.{Group, SubProcess}
import sbt.{Tests, _}
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import AppDependencies._
import bloop.integrations.sbt.BloopDefaults

import scala.util.Properties

lazy val appName = "api-example-microservice"
lazy val plugins: Seq[Plugins] = Seq(PlayScala, SbtAutoBuildPlugin, SbtDistributablesPlugin)
lazy val playSettings: Seq[Setting[_]] = Seq.empty

Global / bloopAggregateSourceDependencies := true

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / majorVersion := 0
ThisBuild / organization := "uk.gov.hmrc"
ThisBuild / retrieveManaged := true
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

lazy val microservice = Project(appName, file("."))
  .enablePlugins(plugins: _*)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(playSettings: _*)
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    name := appName,
    libraryDependencies ++= dependencies ++ testDependencies(),
    Test / parallelExecution:= false,
    Test / fork:= false
  )
  .settings(ScoverageSettings())
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources"
  )
  .configs(Test)
  .settings(inConfig(Test)(Defaults.testSettings): _*)
  .settings(inConfig(Test)(BloopDefaults.configSettings))
  .settings(
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-eT"),
    Test / unmanagedSourceDirectories += baseDirectory.value / "testcommon",
    Test / unmanagedSourceDirectories += baseDirectory.value / "test",
    addTestReportOption(Test, "test-reports")
  )
  .settings(crossPaths := false)
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
  .settings(
    scalacOptions ++= Seq(
      "-Wconf:cat=unused&src=views/.*\\.scala:s",
      "-Wconf:cat=unused&src=.*RoutesPrefix\\.scala:s",
      "-Wconf:cat=unused&src=.*Routes\\.scala:s",
      "-Wconf:cat=unused&src=.*ReverseRoutes\\.scala:s"
    )
  )

lazy val component = (project in file("component"))
  .dependsOn(microservice % "test->test")
  .settings(
    name := "component-tests",
    Test / unmanagedResourceDirectories += baseDirectory.value / "resources",
    Test / testOptions := Seq(Tests.Argument(TestFrameworks.JUnit, "-a"))
  )

lazy val it = (project in file("it"))
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(
    name := "integration-tests",
  )

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: "it / test" :: "component / test" :: state },

  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  // Coverage does not need compile !
  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "it / scalafmtAll" :: "component / scalafmtAll" :: "scalafixAll" :: "it / scalafixAll" :: "component / scalafixAll" :: "coverage" :: "run-all-tests" :: "coverageOff" :: "coverageAggregate" :: state }
)
