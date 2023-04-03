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
lazy val ComponentTest = config("component") extend Test
lazy val plugins: Seq[Plugins] = Seq(PlayScala, SbtAutoBuildPlugin, SbtDistributablesPlugin)
lazy val playSettings: Seq[Setting[_]] = Seq.empty

scalaVersion := "2.13.8"

Global / bloopAggregateSourceDependencies := true
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val microservice = Project(appName, file("."))
  .enablePlugins(plugins: _*)
  .settings(playSettings: _*)
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    name := appName,
    libraryDependencies ++= dependencies ++ testDependencies("test, it, component"),
    Test / parallelExecution:= false,
    Test / fork:= false,
    retrieveManaged := true,
    majorVersion := 0,
  )
  .settings(ScoverageSettings())
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
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
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(inConfig(IntegrationTest)(BloopDefaults.configSettings))
  .settings(
    IntegrationTest / Keys.fork := false,
    IntegrationTest / unmanagedSourceDirectories += baseDirectory.value / "testcommon",
    IntegrationTest / unmanagedSourceDirectories += baseDirectory.value / "it",
    IntegrationTest / testGrouping := oneForkedJvmPerTest((IntegrationTest / definedTests).value),
    IntegrationTest / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-eT"),
    IntegrationTest / parallelExecution := false,
    addTestReportOption(IntegrationTest, "int-test-reports")
  )
  .configs(ComponentTest)
  .settings(inConfig(ComponentTest)(Defaults.testSettings): _*)
  .settings(inConfig(ComponentTest)(BloopDefaults.configSettings))
  .settings(
    ComponentTest / testOptions := Seq.empty,
    ComponentTest / unmanagedSourceDirectories += baseDirectory.value / "component" / "scala",
    ComponentTest / unmanagedSourceDirectories += baseDirectory.value / "testcommon",
    ComponentTest / unmanagedResourceDirectories += baseDirectory.value / "component" / "resources"
  )
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] =
  tests map { test =>
    Group(
      test.name,
      Seq(test),
      SubProcess(
        ForkOptions().withRunJVMOptions(
          Vector(s"-Dtest.name={test.name}", s"-Dtest_driver=${Properties.propOrElse("test_driver", "chrome")}"))
      )
    )
  }
