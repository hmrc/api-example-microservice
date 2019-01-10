/*
 * Copyright 2019 HM Revenue & Customs
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

import controllers.AssetsBuilder
import play.api.http.{HttpErrorHandler, LazyHttpErrorHandler}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.BaseController

class Documentation(httpErrorHandler: HttpErrorHandler) extends AssetsBuilder(httpErrorHandler) with BaseController {

  def documentation(version: String, endpointName: String): Action[AnyContent] = {
    super.at(s"/public/api/documentation/$version", s"${endpointName.replaceAll(" ", "-")}.xml")
  }

  def definition(): Action[AnyContent] = {
    super.at("/public/api", "definition.json")
  }

  def raml(version: String, file: String): Action[AnyContent] = {
    super.at(s"/public/api/conf/$version", file)
  }
}

object Documentation extends Documentation(LazyHttpErrorHandler)
