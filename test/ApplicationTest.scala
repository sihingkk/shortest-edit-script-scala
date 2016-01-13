import org.specs2.mutable._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class ApplicationTest extends Specification {


  "Application" should {

    "render information about changes" in new WithApplication {
      val json = """{
                   | "left": ["a","b","c"],
                   | "right": ["d","c","x"]
                   | }
                   | """.stripMargin
      val request = FakeRequest(Helpers.POST, "/v1/compare").withJsonBody(Json.parse(json))
      val response = route(request).get
      status(response) must equalTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
      contentAsJson(response) should equalTo(
          Json.obj(
            "changes" -> Json.arr(
              Json.obj(
                "change" -> Json.obj(
                "index" -> 0,
                "original" -> "a",
                "revision" -> "d"
              )),
              Json.obj(
                "remove" -> Json.obj(
                "index" -> 1,
                "value" -> "b"
              )),
              Json.obj(
                "add" -> Json.obj(
                "index" -> 2,
                "value" -> "x"
              ))
            )
          )
      )

    }
  }
}
