import uk.gov.hmrc.DefaultBuildSettings

lazy val appName = "api-example-microservice"

Global / bloopAggregateSourceDependencies := true
Global / bloopExportJarClassifiers := Some(Set("sources"))

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / majorVersion := 0
ThisBuild / retrieveManaged := true
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

lazy val microservice = Project(appName, file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    libraryDependencies ++= AppDependencies.dependencies ++ AppDependencies.testDependencies
  )
  .settings(ScoverageSettings())
  .settings(
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources"
  )
  .settings(
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-eT"),
    Test / unmanagedSourceDirectories += baseDirectory.value / "testcommon",
    Test / parallelExecution := false,
    Test / fork := false
  )
  .settings(
    scalacOptions ++= Seq(
      // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
      // suppress warnings in generated routes files
      "-Wconf:src=routes/.*:s"
    )
  )

lazy val component = (project in file("component"))
  .dependsOn(microservice % "test->test")
  .settings(
    name := "component-tests",
    Test / unmanagedSourceDirectories += baseDirectory.value / "test",
    Test / unmanagedResourceDirectories += baseDirectory.value / "resources",
    Test / testOptions := Seq(Tests.Argument(TestFrameworks.JUnit, "-a")),
    publishArtifact := false,
    Test / fork := false
  )

lazy val it = (project in file("it"))
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(
    name := "integration-tests",
    DefaultBuildSettings.itSettings()
  )

commands ++= Seq(
  Command.command("cleanAll") { state => "clean" :: "it/clean" :: "component/clean" :: state },
  Command.command("fmtAll") { state => "scalafmtAll" :: "it/scalafmtAll" :: "component/scalafmtAll" :: state },
  Command.command("fixAll") { state => "scalafixAll" :: "it/scalafixAll" :: "component/scalafixAll" :: state },
  Command.command("testAll") { state => "test" :: "it/test" :: "component/test" :: state },
  Command.command("run-all-tests") { state => "testAll" :: state },
  Command.command("clean-and-test") { state => "cleanAll" :: "compile" :: "run-all-tests" :: state },
  Command.command("pre-commit") { state => "cleanAll" :: "fmtAll" :: "fixAll" :: "coverage" :: "testAll" :: "coverageOff" :: "coverageAggregate" :: state }
)
