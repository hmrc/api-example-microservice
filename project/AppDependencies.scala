import play.core.PlayVersion
import sbt._

object AppDependencies {
  val bootStrapVersion = "7.19.0"

  lazy val dependencies = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"      % bootStrapVersion
  )

  def testDependencies(scope: String) = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"         % bootStrapVersion,
    "com.vladsch.flexmark"    %  "flexmark-all"                   % "0.36.8",
    "org.scalaj"              %% "scalaj-http"                    % "2.4.2",
    "com.github.tomakehurst"  %  "wiremock-jre8-standalone"       % "2.35.0",
    "org.mockito"             %% "mockito-scala-scalatest"        % "1.17.29",
    "io.cucumber"             %% "cucumber-scala"                 % "4.7.1",
    "io.cucumber"             %  "cucumber-junit"                 % "4.7.1",
    "com.novocode"            %  "junit-interface"                % "0.11"          
  ).map(_ % scope)
}