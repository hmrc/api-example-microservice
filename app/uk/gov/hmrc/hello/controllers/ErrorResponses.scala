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


sealed abstract class ErrorResponse(
  val httpStatusCode: Int,
  val errorCode: String,
  val message: String
)
import play.api.libs.json._

object ErrorResponse {
  implicit val errorResponseWrites = new Writes[ErrorResponse] {
    def writes(e: ErrorResponse): JsValue = Json.obj("code" -> e.errorCode, "message" -> e.message)
  }
}

case object ErrorUnauthorized extends ErrorResponse(401, "UNAUTHORIZED", "Bearer token is missing or not authorized") {
  implicit val fmt: Writes[ErrorUnauthorized.type] = ErrorResponse.errorResponseWrites.contramap(x => x)
}

case object ErrorNotFound extends ErrorResponse(404, "NOT_FOUND", "Resource was not found") {
  implicit val fmt: Writes[ErrorNotFound.type] = ErrorResponse.errorResponseWrites.contramap(x => x)
}

case object ErrorGenericBadRequest extends ErrorResponse(400, "BAD_REQUEST", "Bad Request") {
  implicit val fmt: Writes[ErrorGenericBadRequest.type] = ErrorResponse.errorResponseWrites.contramap(x => x)
}

case object ErrorAcceptHeaderInvalid extends ErrorResponse(406, "ACCEPT_HEADER_INVALID", "The accept header is missing or invalid") {
  implicit val fmt: Writes[ErrorAcceptHeaderInvalid.type] = ErrorResponse.errorResponseWrites.contramap(x => x)
}

case object ErrorInternalServerError extends ErrorResponse(500, "INTERNAL_SERVER_ERROR", "Internal server error") {
  implicit val fmt: Writes[ErrorInternalServerError.type] = ErrorResponse.errorResponseWrites.contramap(x => x)
}

