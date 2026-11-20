/*
 * Copyright 2024 HM Revenue & Customs
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

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

import play.api.Configuration
import play.api.http.Status.*
import play.api.mvc.*
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.backend.http.JsonErrorHandler
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent

import uk.gov.hmrc.hello.controllers.*

@Singleton
class ErrorHandler @Inject() (auditConnector: AuditConnector, httpAuditEvent: HttpAuditEvent, configuration: Configuration)(using ec: ExecutionContext)
    extends JsonErrorHandler(auditConnector, httpAuditEvent, configuration) with ErrorConversion {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    given RequestHeader = request

    super.onClientError(request, statusCode, message).map { auditedError =>
      statusCode match {
        case NOT_FOUND    => ErrorNotFound.toResult
        case BAD_REQUEST  => ErrorGenericBadRequest.toResult
        case UNAUTHORIZED => ErrorUnauthorized.toResult
        case _            => auditedError
      }
    }
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    given RequestHeader = request

    super.onServerError(request, exception).map(_ => ErrorInternalServerError.toResult)
  }
}
