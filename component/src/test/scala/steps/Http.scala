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

package steps

import sttp.client3.Request

import play.mvc.Http.HeaderNames

object Request {

  sealed trait AcceptHeader
  case object AcceptValidJsonv extends AcceptHeader
  case object AcceptValidXml   extends AcceptHeader
  case object AcceptMissing    extends AcceptHeader
  case object AcceptBadFormat  extends AcceptHeader
  case object AcceptUndefined  extends AcceptHeader

  implicit class RequestBuilder(httpRequest: Request[Either[String, String], Any]) {

    def addAcceptHeader(acceptHeader: AcceptHeader): Request[Either[String, String], Any] = {
      acceptHeader match {
        case AcceptMissing    => httpRequest.header(HeaderNames.ACCEPT, "")
        case AcceptValidJsonv => httpRequest.header(HeaderNames.ACCEPT, "application/vnd.hmrc.1.0+json")
        case AcceptValidXml   => httpRequest.header(HeaderNames.ACCEPT, "application/vnd.hmrc.1.0+xml")
        case AcceptBadFormat  => httpRequest.header(HeaderNames.ACCEPT, "application/hmrc.1.0+xml")
        case AcceptUndefined  => throw new scala.RuntimeException("Undefined accept in the scenario - no accept status defined")
      }
    }
  }
}

object Responses {

  val statusCodes = Map(
    "OK"                     -> 200,
    "UNSUPPORTED_MEDIA_TYPE" -> 415,
    "CONFLICT"               -> 409,
    "REQUEST_TIMEOUT"        -> 408,
    "NOT_FOUND"              -> 404,
    "BAD_REQUEST"            -> 400,
    "UNAUTHORIZED"           -> 401,
    "NOT_ACCEPTABLE"         -> 406,
    "INTERNAL_SERVER_ERROR"  -> 500,
    "BAD_GATEWAY"            -> 502
  )
}
