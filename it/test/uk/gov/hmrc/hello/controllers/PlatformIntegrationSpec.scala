/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.hello.controllers

import com.github.tomakehurst.wiremock.client.WireMock._
import org.apache.pekko.stream.Materializer
import org.scalatest.TestData
import org.scalatestplus.play.guice.GuiceOneAppPerTest

import play.api.http.Status.{NO_CONTENT, OK}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsJson, contentAsString, status}
import play.api.{Application, Mode}
import uk.gov.hmrc.platform.controllers.DocumentationController

import uk.gov.hmrc.hello.common.utils.{AsyncHmrcSpec, WireMockSugar}

class PlatformIntegrationSpec extends AsyncHmrcSpec with GuiceOneAppPerTest with WireMockSugar {

  override def newAppForTest(testData: TestData): Application = GuiceApplicationBuilder()
    .configure(
      "run.mode"                                   -> "Stub",
      "appName"                                    -> "application-name",
      "appUrl"                                     -> "http://example.com",
      "auditing.enabled"                           -> false,
      "microservice.services.service-locator.host" -> stubHost,
      "microservice.services.service-locator.port" -> stubPort
    )
    .in(Mode.Test).build()

  trait Setup {
    implicit def mat: Materializer = app.injector.instanceOf[Materializer]
    val documentationController    = app.injector.instanceOf[DocumentationController]
    val request                    = FakeRequest()
    stubFor(post(urlMatching("http://localhost:11112/registration")).willReturn(aResponse().withStatus(NO_CONTENT)))
  }

  "microservice" should {
    "provide definition endpoint and documentation endpoint for each api" in new Setup {
      val result = documentationController.definition()(request)
      status(result) shouldBe OK

      val jsonResponse = contentAsJson(result)

      (jsonResponse \\ "version") map (_.as[String])
      (jsonResponse \\ "endpoints").map(_ \\ "endpointName").map(_.map(_.as[String]))
    }

    "provide yaml documentation" in new Setup {
      val result = documentationController.specification("1.0", "application.yaml")(request)

      status(result) shouldBe OK
      contentAsString(result) should startWith("openapi")
    }
  }

}
