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

import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import steps.Request.{AcceptBadFormat, AcceptMissing, _}
import sttp.client3.{SimpleHttpClient, UriContext, basicRequest}

import play.api.libs.json.Json

import uk.gov.hmrc.hello.controllers.HmrcMimeTypes

object World {
  var responseCode: Int                   = 0
  var responseBody: String                = ""
  var responseContentType: Option[String] = None
  var acceptHeader: AcceptHeader          = AcceptUndefined
}

class HelloWorldServiceSteps extends ScalaDsl with EN with Matchers with HmrcMimeTypes with EitherValues {
  When("""^I GET the resource '(.*)'$""") { (url: String) =>
    val fullUrl    = s"${Env.testServerHost}$url"
    val httpClient = SimpleHttpClient()
    val request    = basicRequest
      .get(uri"$fullUrl")
      .addAcceptHeader(World.acceptHeader)
    val response   = httpClient.send(request)

    World.responseCode = response.code.code
    World.responseBody = response.body.merge
    World.responseContentType = response.contentType

    httpClient.close()
  }

  Then("""^the status code should be '(.*)'$""") { (st: String) =>
    Responses.statusCodes(st) shouldBe World.responseCode
  }

  Given("""^header 'Accept' is '(.*)'$""") { (acceptValue: String) =>
    World.acceptHeader = acceptValue match {
      case "not provided"    => AcceptMissing
      case "badly formatted" => AcceptBadFormat
      case "valid json"      => AcceptValidJsonv
      case "valid xml"       => AcceptValidXml
      case _                 => throw new scala.RuntimeException("Undefined value for accept in the step")
    }
  }

  Then("""^I should receive JSON response:$""") { (expectedResponseBody: String) =>
    Json.parse(expectedResponseBody) shouldBe Json.parse(World.responseBody)
  }

  Then("""^I should receive XML response:$""") { (expectedResponseBody: String) =>
    expectedResponseBody shouldBe World.responseBody
  }
}
