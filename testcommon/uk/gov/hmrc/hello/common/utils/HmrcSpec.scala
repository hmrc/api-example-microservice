package uk.gov.hmrc.hello.common.utils

import org.mockito.{ArgumentMatchersSugar, MockitoSugar}
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.WsScalaTestClient

import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

abstract class HmrcSpec extends AnyWordSpec with Matchers with OptionValues with WsScalaTestClient with MockitoSugar with ArgumentMatchersSugar

abstract class AsyncHmrcSpec extends HmrcSpec with DefaultAwaitTimeout with FutureAwaits
