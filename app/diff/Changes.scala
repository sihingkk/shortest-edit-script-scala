package diff

import java.util

import play.api.libs.json.{JsValue, Json, JsObject}
import play.api.libs.json.Json.JsValueWrapper

import scala.collection.JavaConverters._
import scala.collection.mutable

sealed abstract trait Change {
  def toJson(): JsObject
}

case class ItemChanged(index:Int,original:Any,revision:Any) extends Change {
  override def toJson(): JsObject = Json.obj(
    "change" -> Json.obj(
    "index" -> index,
    "original" -> Change.wrap(original),
    "revision" ->  Change.wrap(revision)
  ))
}
case class ItemAdded(index:Int,value:Any) extends Change {
  override def toJson(): JsObject = Json.obj(
    "add" -> Json.obj(
    "index" -> index,
    "value" ->  Change.wrap(value)
  ))
}
case class ItemRemoved(index:Int, value:Any) extends Change {
  override def toJson(): JsObject = Json.obj(
    "remove" -> Json.obj(
    "index" -> index,
    "value" -> Change.wrap(value)
  ))
}

object Change {

  def applyChanges(list: Seq[Any],changes: List[Change]): Seq[Any] = {
    val result = new util.ArrayList[Any]()
    result.addAll(list.asJava)
    for (change <- changes) {
      change match {
        case ItemChanged(index,_,value) => result.set(index,value)
        case ItemAdded(index,value) => result.add(index,value)
        case ItemRemoved(index,_) => result.remove(index)
      }
    }
    result.asScala.toSeq
  }

  def wrap(any:Any):JsValueWrapper =  {
    any match {
      case wrapper: JsValueWrapper => wrapper
      case string:JsValue => string
    }
  }
}