package diff.myers

case class EditorGraph(left: Seq[_], right:Seq[_]) {
  private val _left = left.toArray
  private val _right = right.toArray
  
  lazy val getColsCount = _left.size
  lazy val getRowsCount = _right.size

  def isDiagonalAt(x:Int, y:Int):Boolean = {
    (x < _left.length) && y < _right.length && _left(x) == _right(y)
  }

  def isAtEndPoint(x:Int, y:Int):Boolean = {
    x >= _left.length && y >= _right.length
  }
  def isAtEndPoint(pathNode: PathNode):Boolean = {
    pathNode.x >= _left.length && pathNode.y >= _right.length
  }
}
