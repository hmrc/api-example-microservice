/*
 * Copyright 2022 HM Revenue & Customs
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

package uk.gov.hmrc.hello.services

import javax.inject.Singleton
import scala.concurrent.Future

import play.api.libs.json.Json

case class Hello(message: String)

object Hello {
  implicit val format = Json.format[Hello]
}

@Singleton
class HelloWorldService {
  def fetchWorld: Future[Hello] =
    Future.successful(Hello("Hello World"))

  def fetchApplication: Future[Hello] =
    Future.successful(Hello("Hello Application"))

  def fetchUser: Future[Hello] =
    Future.successful(Hello("Hello User"))
}

