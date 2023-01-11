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

import steps.Env
import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.{AfterClass, BeforeClass}
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("classpath:features"),
  glue = Array("classpath:steps"),
  plugin = Array("pretty",
    "html:target/component-reports/cucumber",
    "json:target/component-reports/cucumber.json"),
  tags = Array("not @wip")
)
class FeatureSuite

object FeatureSuite {

  @BeforeClass
  def setup(): Unit = {
    Env.startServer()
  }

  @AfterClass
  def cleanup(): Unit = {
    Env.shutdown()
  }

}
