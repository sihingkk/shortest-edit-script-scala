package diff.myers


import diff._
import diff.myers.Myers.Path
import play.api.Logger



class MyersDiff extends Differ {

  def compare(left: Seq[_], right: Seq[_]): List[Change] = {
    val path = new Myers(new EditorGraph(left, right)).solution
    val reverse: List[Change] = buildChangeSet(left, right, path).reverse
    reverse
  }

  def buildChangeSet(left: Seq[_], right: Seq[_], path: Path): List[Change] = {
    path zip path.tail flatMap {
      case ( PathNode(x,y,false),PathNode(offsetX,offsetY,true)) =>
        val removed = left.slice(offsetX,x).map(Some(_))
        val added = right.slice(offsetY,y).map(Some(_))
        val changedCount = scala.math.min(added.size,removed.size)
        removed.zipAll(added, None, None).zipWithIndex.map {
          case (((Some(removedItem), None), index)) => ItemRemoved(changedCount + offsetY, removedItem)
          case (((None, Some(addedItem)), index)) => ItemAdded(index + offsetY, addedItem)
          case (((Some(removedItem), Some(addedItem)), index)) => ItemChanged(index + offsetY, removedItem, addedItem)
        }.reverse
      case _ => List()
    }
  }
}
