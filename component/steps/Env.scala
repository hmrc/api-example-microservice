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

package component.steps

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import play.api.Mode
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.TestServer

trait Env extends ScalaDsl with EN with Matchers {
  val testServerPort = 9000
  val testServerHost = sys.env.getOrElse("HOST", s"http://localhost:$testServerPort")

  val config = Map[String, Any](
    "auditing.enabled" -> false,
    "microservice.services.datastream.host" -> stubHost,
    "microservice.services.datastream.port" -> stubPort,
    "microservice.services.datastream.enabled" -> false,
    "microservice.services.service-locator.host" -> stubHost,
    "microservice.services.service-locator.port" -> stubPort,
    "microservice.services.service-locator.enabled" -> false
  )

  private lazy val application =
    GuiceApplicationBuilder()
      .configure(config)
      .in(Mode.Test)
      .build()

  private lazy val testServer = TestServer(testServerPort, application)

  val stubPort = sys.env.getOrElse("WIREMOCK_PORT", "11111").toInt
  val stubHost = "localhost"
  val wireMockUrl = s"http://$stubHost:$stubPort"

  private lazy val wireMockServer = new WireMockServer(wireMockConfig().port(stubPort))

  Before { _ =>
    if(!wireMockServer.isRunning) {
      wireMockServer.start()
    }

    WireMock.configureFor(stubHost, stubPort)
  }

  After { _ =>
    if(wireMockServer.isRunning) {
      WireMock.reset()
    }
  }

  def startServer() {
    testServer.start()
  }

  def shutdown() = {
    wireMockServer.stop()
    if (testServer != null) testServer.stop()
  }
}

object Env extends Env