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

package uk.gov.hmrc.hello.config

import javax.inject.{Inject, Provider, Singleton}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.hello.connectors.ServiceLocatorConfig
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class ServiceLocatorRegistrationConfigProvider @Inject()(runModeConfiguration: Configuration, environment: Environment, servicesConfig: ServicesConfig)
  extends Provider[ServiceLocatorRegistrationConfig] {

  override def get() = {
    val registrationEnabled = servicesConfig.getConfBool("service-locator.enabled", defBool = true)
    ServiceLocatorRegistrationConfig(registrationEnabled)
  }
}

@Singleton
class ServiceLocatorConfigProvider @Inject()(runModeConfiguration: Configuration, environment: Environment, servicesConfig: ServicesConfig)
  extends Provider[ServiceLocatorConfig] {

  override def get() = {
    val appName = servicesConfig.getString("appName")
    val appUrl = servicesConfig.getString("appUrl")
    val serviceLocatorBaseUrl = servicesConfig.baseUrl("service-locator")
    ServiceLocatorConfig(appName, appUrl, serviceLocatorBaseUrl)
  }
}
