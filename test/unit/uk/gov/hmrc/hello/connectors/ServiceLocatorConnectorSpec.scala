/*
 * Copyright 2018 HM Revenue & Customs
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

package unit.uk.gov.hmrc.hello.connectors

import uk.gov.hmrc.hello.domain.Registration
import org.mockito.Matchers
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import play.api.libs.json.Writes
import uk.gov.hmrc.hello.connectors.ServiceLocatorConnector
import uk.gov.hmrc.http.{HeaderCarrier, HttpPost, HttpReads, HttpResponse}
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future

class ServiceLocatorConnectorSpec extends UnitSpec with MockitoSugar with ScalaFutures {

  trait Setup {
    implicit val hc = HeaderCarrier()
    val serviceLocatorException = new RuntimeException

    val connector = new ServiceLocatorConnector {
      override val http = mock[HttpPost]
      override val appUrl: String = "http://example.com"
      override val appName: String = "api-microservice"
      override val serviceUrl: String = "https://SERVICE_LOCATOR"
      override val handlerOK: () => Unit = mock[() => Unit]
      override val handlerError: Throwable => Unit = mock[(Throwable) => Unit]
      override val metadata: Option[Map[String, String]] = Some(Map("third-party-api" -> "true"))
    }
  }

  "register" should {
    "register the JSON API Definition into the Service Locator" in new Setup {

      val registration = Registration(serviceName = "api-microservice", serviceUrl = "http://example.com", metadata = Some(Map("third-party-api" -> "true")))

      when(connector.http.POST(any[String](), any[Registration](), any[Seq[(String, String)]]())(any[Writes[Registration]](), any[HttpReads[HttpResponse]](), any(), any())).thenReturn(Future.successful(HttpResponse(200)))
      connector.register.futureValue shouldBe true
      verify(connector.http).POST(Matchers.eq("https://SERVICE_LOCATOR/registration"), Matchers.eq(registration), Matchers.eq(Seq("Content-Type" -> "application/json")))(any[Writes[Registration]](), any[HttpReads[HttpResponse]](), any(), any())
      verify(connector.handlerOK).apply()
      verify(connector.handlerError, never).apply(serviceLocatorException)
    }


    "fail registering in service locator" in new Setup {

      val registration = Registration(serviceName = "api-microservice", serviceUrl = "http://example.com", metadata = Some(Map("third-party-api" -> "true")))
      when(connector.http.POST(any[String](), any[Registration](), any[Seq[(String, String)]]())(any[Writes[Registration]](), any[HttpReads[HttpResponse]](), any(), any())).thenReturn(Future.failed(serviceLocatorException))

      connector.register.futureValue shouldBe false
      verify(connector.http).POST(Matchers.eq("https://SERVICE_LOCATOR/registration"), Matchers.eq(registration), Matchers.eq(Seq("Content-Type" -> "application/json")))(any[Writes[Registration]](), any[HttpReads[HttpResponse]](), any(), any())
      verify(connector.handlerOK, never).apply()
      verify(connector.handlerError).apply(serviceLocatorException)
    }

  }
}

