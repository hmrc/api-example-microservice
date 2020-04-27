/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package component

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import component.steps.Env._
import io.cucumber.junit.CucumberOptions
import io.cucumber.junit.Cucumber
import org.junit.runner.RunWith
import org.junit.{AfterClass, BeforeClass}
import play.api.Mode
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.TestServer

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("features"),
  glue = Array("component/steps"),
  plugin = Array("pretty",
    "html:target/component-reports/cucumber",
    "json:target/component-reports/cucumber.json"),
  tags = Array("not @wip")
)
class FeatureSuite

object FeatureSuite extends StubApplicationConfiguration {

  private lazy val testServer = TestServer(hostPost, app)

  private lazy val app =
    GuiceApplicationBuilder()
      .configure(config)
      .in(Mode.Test)
      .build()

  private lazy val wireMockServer = new WireMockServer(wireMockConfig().port(stubPort))

  private var isSetup = false

  /**
    * Apparently necessary for running individual features from within IntelliJ.
    */
  def ensureSetup(): Unit = if (!isSetup) setup()

  @BeforeClass
  def setup(): Unit = {
    wireMockServer.start()
    WireMock.configureFor(stubHost, stubPort)
    testServer.start()
    isSetup = true
  }

  @AfterClass
  def cleanup(): Unit = {
    testServer.stop()
    wireMockServer.stop()
  }

}
