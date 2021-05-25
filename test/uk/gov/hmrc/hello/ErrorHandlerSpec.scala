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

package uk.gov.hmrc.hello

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.`given`
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector, AuditResult}
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent
import uk.gov.hmrc.play.test.UnitSpec

import play.api.Configuration
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

import uk.gov.hmrc.hello.controllers.{ErrorGenericBadRequest, ErrorInternalServerError, ErrorNotFound, ErrorUnauthorized}

class ErrorHandlerSpec extends UnitSpec with MockitoSugar {
  trait BaseSetup {
    implicit val sys = ActorSystem("MyTest")
    implicit val mat = ActorMaterializer()

    implicit val fakeRequest = FakeRequest()
    val mockAuditConnector = mock[AuditConnector]
    val mockAuditResult = mock[AuditResult]
    val mockHttpAuditEvent = mock[HttpAuditEvent]
    val mockConfiguration = mock[Configuration]

    when(mockAuditConnector.sendEvent(any())(any(), any())).thenReturn(Future.successful(mockAuditResult))

    given(mockConfiguration.getOptional[Seq[Int]]("bootstrap.errorHandler.warnOnly.statusCodes")).willReturn(None)

    val errorHandler = new ErrorHandler(mockAuditConnector, mockHttpAuditEvent, mockConfiguration)
  }

  "onClientError" should {
    class Setup(statusCode: Int) extends BaseSetup {
      val response = await(errorHandler.onClientError(fakeRequest, statusCode, "A message"))
    }

    "return ErrorNotFound on 404 Not Found" in new Setup(NOT_FOUND) {
      jsonBodyOf(response) shouldBe Json.toJson(ErrorNotFound)
    }

    "return ErrorGenericBadRequest on 400 Bad Request" in new Setup(BAD_REQUEST) {
      jsonBodyOf(response) shouldBe Json.toJson(ErrorGenericBadRequest)
    }

    "return ErrorUnauthorized on 401 Unauthorized" in new Setup(UNAUTHORIZED) {
      jsonBodyOf(response) shouldBe Json.toJson(ErrorUnauthorized)
    }

    "return a statusCode of 405 with the provided message on 405 Method Not Allowed" in new Setup(METHOD_NOT_ALLOWED) {
      jsonBodyOf(response) shouldBe Json.obj("statusCode" -> METHOD_NOT_ALLOWED, "message" -> "A message")
    }
  }

  "onServerError" should {
    "return ErrorInternalServerError" in new BaseSetup {
      val response = await(errorHandler.onServerError(fakeRequest, new RuntimeException("Internal Server Error")))

      jsonBodyOf(response) shouldBe Json.toJson(ErrorInternalServerError)
    }
  }
}
