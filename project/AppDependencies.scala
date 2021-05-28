import play.core.PlayVersion
import sbt._

object AppDependencies {
  lazy val dependencies = Seq(
    "uk.gov.hmrc"             %% "bootstrap-play-26"          % "4.0.0"
  )

  def testDependencies(scope: String) = Seq(
    "org.scalaj"              %% "scalaj-http"                % "2.4.0" % scope,
    "org.pegdown"             %  "pegdown"                    % "1.6.0" % scope,
    "com.typesafe.play"       %% "play-test"                  % PlayVersion.current % scope,
    "com.github.tomakehurst"  %  "wiremock-jre8-standalone"   % "2.27.1" % scope,
    "org.scalatestplus.play"  %% "scalatestplus-play"         % "3.1.3" % scope,
    "org.mockito"             %% "mockito-scala-scalatest"    % "1.7.1" % scope,
    "io.cucumber"             %% "cucumber-scala"             % "4.7.1" % scope,
    "io.cucumber"             %  "cucumber-junit"             % "4.7.1" % scope,
    "com.novocode"            %  "junit-interface"            % "0.11"  % scope
  )
}