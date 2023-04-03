package uk.gov.hmrc.hello.common.utils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}

trait WireMockSugar extends BeforeAndAfterEach with BeforeAndAfterAll {
  this: Suite =>
  val stubPort    = sys.env.getOrElse("WIREMOCK", "22222").toInt
  val stubHost    = "localhost"
  val wireMockUrl = s"http://$stubHost:$stubPort"

  private val wireMockConfiguration: WireMockConfiguration =
    wireMockConfig().port(stubPort)

  val wireMockServer = new WireMockServer(wireMockConfiguration)

  override def beforeAll(): Unit = {
    super.beforeAll()
    wireMockServer.start()
    WireMock.configureFor(stubHost, stubPort)
  }

  override protected def afterAll(): Unit = {
    wireMockServer.stop()
    super.afterAll()
  }

  override def afterEach(): Unit = {
    wireMockServer.resetMappings()
    super.afterEach()
  }
}
