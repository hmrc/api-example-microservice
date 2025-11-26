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

package uk.gov.hmrc.hello.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.http.HeaderNames
import play.api.http.Status.{NOT_ACCEPTABLE, OK}
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

import uk.gov.hmrc.hello.common.utils.AsyncHmrcSpec
import uk.gov.hmrc.hello.services.{Hello, HelloWorldService}

class HelloWorldControllerSpec extends AsyncHmrcSpec {

  trait Setup {
    val validator = new HeaderValidator(Helpers.stubControllerComponents())
    val underTest = new HelloWorldController(validator, new HelloWorldService, Helpers.stubControllerComponents())
  }

  "HelloWorldController" should {
    "/world accepting Json" in new Setup() {
      val result = underTest.world(FakeRequest("GET", "/").withHeaders(HeaderNames.ACCEPT -> HmrcMimeTypes.VndHmrcJson_1_0))
      status(result) shouldBe OK
      contentAsJson(result) shouldBe Json.toJson(Hello("Hello World"))
    }

    "/world accepting Xml" in new Setup() {
      val result = underTest.world(FakeRequest("GET", "/").withHeaders(HeaderNames.ACCEPT -> HmrcMimeTypes.VndHmrcXml_1_0))
      status(result) shouldBe OK
      contentAsString(result) shouldBe """<?xml version='1.0' encoding='UTF-8'?>
<Hello><message>Hello World</message></Hello>"""
    }

    "/application accepting Json" in new Setup() {
      val result = underTest.application(FakeRequest("GET", "/").withHeaders(HeaderNames.ACCEPT -> HmrcMimeTypes.VndHmrcJson_1_0))
      status(result) shouldBe OK
      contentAsJson(result) shouldBe Json.toJson(Hello("Hello Application"))
    }

    "/application accepting Xml" in new Setup() {
      val result = underTest.application(FakeRequest("GET", "/").withHeaders(HeaderNames.ACCEPT -> HmrcMimeTypes.VndHmrcXml_1_0))
      status(result) shouldBe OK
      contentAsString(result) shouldBe """<?xml version='1.0' encoding='UTF-8'?>
<Hello><message>Hello Application</message></Hello>"""
    }

    "/user accepting Json" in new Setup() {
      val result = underTest.user(FakeRequest("GET", "/").withHeaders(HeaderNames.ACCEPT -> HmrcMimeTypes.VndHmrcJson_1_0))
      status(result) shouldBe OK
      contentAsJson(result) shouldBe Json.toJson(Hello("Hello User"))
    }

    "/user accepting Xml" in new Setup() {
      val result = underTest.user(FakeRequest("GET", "/").withHeaders(HeaderNames.ACCEPT -> HmrcMimeTypes.VndHmrcXml_1_0))
      status(result) shouldBe OK
      contentAsString(result) shouldBe """<?xml version='1.0' encoding='UTF-8'?>
<Hello><message>Hello User</message></Hello>"""
    }

    "/world with no accept header" in new Setup() {
      val result = underTest.world(FakeRequest("GET", "/"))
      status(result) shouldBe NOT_ACCEPTABLE
    }

    "/world with bad accept header" in new Setup() {
      val result = underTest.world(FakeRequest("GET", "/").withHeaders(HeaderNames.ACCEPT -> "application/vnd.hmrc.999.0+xml"))
      status(result) shouldBe NOT_ACCEPTABLE
    }
  }
}
