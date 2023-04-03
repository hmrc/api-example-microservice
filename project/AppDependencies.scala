import play.core.PlayVersion
import sbt._

object AppDependencies {
  val bootStrapVersion = "7.12.0"

  lazy val dependencies = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"      % bootStrapVersion
  )

  def testDependencies(scope: String) = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"         % bootStrapVersion    % scope,
    "com.vladsch.flexmark"    %  "flexmark-all"                   % "0.36.8"            % scope,
    "org.scalaj"              %% "scalaj-http"                    % "2.4.2"             % scope,
    "com.github.tomakehurst"  %  "wiremock-jre8-standalone"       % "2.35.0"            % scope,
    "org.mockito"             %% "mockito-scala-scalatest"        % "1.16.46"           % scope,
    "io.cucumber"             %% "cucumber-scala"                 % "4.7.1"             % scope,
    "io.cucumber"             %  "cucumber-junit"                 % "4.7.1"             % scope,
    "com.novocode"            %  "junit-interface"                % "0.11"              % scope
  )
}