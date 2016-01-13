package diff

import play.Logger
import play.api.libs.json._
import play.api.mvc._

class JsonDiffController(differ: Differ) extends Controller {

  def compare() = Action { request =>
    request.body.asJson.flatMap { json =>
      for (
        left <- (json \ "left").asOpt[JsArray];
        right <- (json \ "right").asOpt[JsArray]
      ) yield Json.obj(
        "changes" -> Json.toJson(differ.compare(left.value, right.value).map(_.toJson()))
      )
    } match {
        case Some(json) => Ok(json)
        case None => {
          Logger.info(s"Bad request. request body: ${request.body}")
          BadRequest("Expecting Json with left and right collection to compare.")
        }
    }
  }
}