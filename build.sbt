import _root_.play.core.PlayVersion
import _root_.play.sbt.PlayImport._
import _root_.play.sbt.PlayScala
import play.routes.compiler.StaticRoutesGenerator
import play.sbt.routes.RoutesKeys.routesGenerator
import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._

lazy val appName = "api-example-microservice"

lazy val plugins: Seq[Plugins] = Seq(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
lazy val playSettings: Seq[Setting[_]] = Seq.empty
lazy val microservice = (project in file("."))
    .enablePlugins(plugins: _*)
    .settings(playSettings: _*)
    .settings(scalaSettings: _*)
    .settings(publishingSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      name := appName,
      targetJvm := "jvm-1.8",
      libraryDependencies ++= appDependencies,
      parallelExecution in Test := false,
      fork in Test := false,
      retrieveManaged := true,
      routesGenerator := StaticRoutesGenerator,
      scalaVersion := "2.11.11",
      majorVersion := 0
    )
    .settings(testOptions in Test := Seq(Tests.Filter(unitFilter)),
      addTestReportOption(Test, "test-reports")
    )
    .settings(
      unmanagedResourceDirectories in Compile += baseDirectory.value / "resources"
    )
    .configs(IntegrationTest)
    .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
    .settings(
      Keys.fork in IntegrationTest := false,
      unmanagedSourceDirectories in IntegrationTest := Seq((baseDirectory in IntegrationTest).value / "test/it" ),
      addTestReportOption(IntegrationTest, "int-test-reports"),
      testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
      parallelExecution in IntegrationTest := false)
    .configs(ComponentTest)
    .settings(inConfig(ComponentTest)(Defaults.testSettings): _*)
    .settings(
      testOptions in ComponentTest := Seq(Tests.Filter(componentTestFilter)),
      unmanagedSourceDirectories in ComponentTest := Seq((baseDirectory in ComponentTest).value / "test/component" ),
      unmanagedResourceDirectories in ComponentTest := Seq((baseDirectory in ComponentTest).value / "test/component" )
    )
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
    .settings(
      resolvers += Resolver.bintrayRepo("hmrc", "releases"),
      resolvers += Resolver.jcenterRepo)
    .settings(ivyScala := ivyScala.value map {
      _.copy(overrideScalaVersion = true)
    })
lazy val ComponentTest = config("component") extend IntegrationTest

lazy val appDependencies: Seq[ModuleID] = compile ++ test


lazy val compile = Seq(
  ws,
  "uk.gov.hmrc" %% "microservice-bootstrap" % "10.0.0"
)
lazy val test = Seq(
  "uk.gov.hmrc" %% "hmrctest" % "3.3.0" % "test,it",
  "org.scalaj" %% "scalaj-http" % "2.4.0" % "test,it",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test,it",
  "org.pegdown" % "pegdown" % "1.6.0" % "test,it",
  "com.typesafe.play" %% "play-test" % PlayVersion.current % "test,it",
  "com.github.tomakehurst" % "wiremock" % "1.58" % "test,it",
  "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % "test,it",
  "org.mockito" % "mockito-core" % "1.10.19" % "test,it",
  "info.cukes" %% "cucumber-scala" % "1.2.5" % "test,it",
  "info.cukes" % "cucumber-junit" % "1.2.5" % "test,it"
)

def componentTestFilter(name: String): Boolean = name startsWith "component"

def unitFilter(name: String): Boolean = name startsWith "unit"

def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
  tests map {
    test => Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
  }

// Coverage configuration
coverageMinimum := 80
coverageFailOnMinimum := true
coverageExcludedPackages := "<empty>;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;uk.gov.hmrc.BuildInfo"
