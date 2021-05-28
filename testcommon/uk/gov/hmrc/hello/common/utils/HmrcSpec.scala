package uk.gov.hmrc.hello.common.utils

import org.scalatest.{Matchers, OptionValues, WordSpec}
import org.scalatestplus.play.WsScalaTestClient
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import org.mockito.{ArgumentMatchersSugar, MockitoSugar}

abstract class HmrcSpec extends WordSpec with Matchers with OptionValues with WsScalaTestClient with MockitoSugar with ArgumentMatchersSugar

abstract class AsyncHmrcSpec extends HmrcSpec with DefaultAwaitTimeout with FutureAwaits