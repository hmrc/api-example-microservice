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

package unit.uk.gov.hmrc.hello.config

import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import uk.gov.hmrc.hello.config.{ServiceLocatorRegistration, ServiceLocatorRegistrationConfig}
import uk.gov.hmrc.hello.connectors.ServiceLocatorConnector
import uk.gov.hmrc.play.test.UnitSpec

class ServiceLocatorRegistrationSpec extends UnitSpec with MockitoSugar {

  class Setup(implicit registrationEnabled: Boolean) {
    val mockConnector = mock[ServiceLocatorConnector]
    val underTest = new ServiceLocatorRegistration(
      ServiceLocatorRegistrationConfig(registrationEnabled),
      mockConnector
    )
  }

  "On ServiceLocatorRegistration initialisation" when {

    "registration is enabled" should {
      implicit val registrationEnabled = true

      "register with service locator" in new Setup {
        verify(mockConnector).register()
      }
    }

    "registration is not enabled" should {
      implicit val registrationEnabled = false

      "not register with service locator" in new Setup {
        verify(mockConnector, never).register()
      }
    }
  }
}

