import play.core.PlayVersion
import sbt._

object AppDependencies {
  lazy val dependencies = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"      % "5.13.0"
  )

  def testDependencies(scope: String) = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"         % "5.13.0"            % scope,
    "com.vladsch.flexmark"    %  "flexmark-all"                   % "0.36.8"            % scope,
    "org.scalaj"              %% "scalaj-http"                    % "2.4.0"             % scope,
    "com.github.tomakehurst"  %  "wiremock-jre8-standalone"       % "2.27.1"            % scope,
    "org.mockito"             %% "mockito-scala-scalatest"        % "1.7.1"             % scope,
    "io.cucumber"             %% "cucumber-scala"                 % "4.7.1"             % scope,
    "io.cucumber"             %  "cucumber-junit"                 % "4.7.1"             % scope,
    "com.novocode"            %  "junit-interface"                % "0.11"              % scope
  )
}