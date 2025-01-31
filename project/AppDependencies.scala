import sbt._

object AppDependencies {
  val bootStrapVersion = "9.7.0"

  lazy val dependencies = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"      % bootStrapVersion
  )

  lazy val testDependencies = Seq(
    "uk.gov.hmrc"                   %% "bootstrap-test-play-30"         % bootStrapVersion,
    "com.softwaremill.sttp.client3" %% "core"                           % "3.9.8",
    "org.mockito"                   %% "mockito-scala-scalatest"        % "1.17.29",
    "io.cucumber"                   %% "cucumber-scala"                 % "8.20.0",
    "io.cucumber"                   %  "cucumber-junit"                 % "7.15.0",
    "junit"                         %  "junit"                          % "4.13.2",
    "com.novocode"                  %  "junit-interface"                % "0.11"
  ).map(_ % "test")
}
