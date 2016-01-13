import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.Json
import play.api.libs.ws.WS

import play.api.test._
import play.api.test.Helpers._


class IntegrationSpec extends Specification {
  val port = {
    val portFromEnv = System.getProperties.getProperty("testserver.port")
    if(portFromEnv == null) "19001" else portFromEnv
  }
  val serverUrl = s"http://localhost:${port}"

  "Appl should" should {
    "return detected changes" in  new WithServer() {
      Helpers.await(WS.url(s"${serverUrl}/v1/compare").post(Json.obj(
        "left" -> Json.arr(1,2),
        "right" -> Json.arr(4)
      ))).json shouldEqual Json.obj(
        "changes" -> Json.arr(
          Json.obj("change" -> Json.obj("index" -> 0, "original" -> 1, "revision" ->4)),
          Json.obj("remove" -> Json.obj("index" -> 1, "value" ->2))
        )
      )
    }

    "contains information about processed time" in  new WithServer() {
      Helpers.await(WS.url(s"${serverUrl}/v1/compare").post(Json.obj(
        "left" -> Json.arr(1,2),
        "right" -> Json.arr(4)
      ))).header("Request-Time") should not beEmpty
    }

    "return bad request for mailformed json" in new WithServer()  {
      Helpers.await(WS.url(s"${serverUrl}/v1/compare").post(Json.obj(
        "left" -> Json.arr(1,2)
      ))).status should beEqualTo(BAD_REQUEST)
    }

  }
}
