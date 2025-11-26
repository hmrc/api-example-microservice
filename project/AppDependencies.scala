import sbt._

object AppDependencies {
  val bootStrapVersion = "9.19.0"

  lazy val dependencies = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"      % bootStrapVersion
  )

  lazy val testDependencies = Seq(
    "uk.gov.hmrc"                   %% "bootstrap-test-play-30"         % bootStrapVersion,
    "com.softwaremill.sttp.client3" %% "core"                           % "3.9.8",
    "org.mockito"                   %% "mockito-scala-scalatest"        % "2.0.0",
  ).map(_ % "test")
}
