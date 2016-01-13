

package diff.myers

import diff.myers.Myers.{Path, Envelope}

object Myers {
  type Path = List[PathNode]
  type Envelope = List[Path]
  val firstPath = List(PathNode(0, 0, diagonalMove = true))
  val firstEnvelope = List(firstPath)
  val guardian = List(List(PathNode(-1, -1)))
}

class Myers(graph: EditorGraph) {

  lazy val firstDifference: Envelope = List(moveDiagonalsAsFarAsPossible(Myers.firstPath))

  def solution: Path = {
    def step(envelope: Envelope): Envelope = {
      if (envelope.exists(isSolution)) envelope
      else step(findNextEnvelope(envelope))
    }
    step(firstDifference).find(isSolution).get
  }

  def isSolution(path: Path): Boolean = {
    graph.isAtEndPoint(path.head)
  }

  lazy val envelopes: Stream[Envelope] = {
    def loop(v: Envelope): Stream[Envelope] = v #:: loop(findNextEnvelope(v))
    loop(firstDifference)
  }


  def findNextEnvelope(envelope: Envelope): Envelope = {
    val topNeightbours = if (topNode(envelope).x < graph.getColsCount) Myers.guardian ::: envelope else envelope
    val leftExtension = if (topNode(envelope).x < graph.getColsCount) envelope else envelope.tail
    val leftNeightbours = if (bottomNode(envelope).y < graph.getRowsCount) leftExtension ::: Myers.guardian else leftExtension

    leftNeightbours zip topNeightbours map { case (left: Path, top: Path) =>
      if (left.head > top.head) moveDiagonalsAsFarAsPossible(moveRightFrom(left))
      else moveDiagonalsAsFarAsPossible(moveDownFrom(top))
    }
  }


  def attachGuardians(envelope: Envelope, graph: EditorGraph): Envelope = {
    val maybeTopGuardian = if(topNode(envelope).x < graph.getColsCount) Myers.guardian else List()
    val maybeBottomGuardian = if(bottomNode(envelope).y < graph.getRowsCount) Myers.guardian else List()
    maybeTopGuardian ::: envelope ::: maybeBottomGuardian
  }

  private def topNode(envelope: Envelope): PathNode = {
    val topPath: Path = envelope.head
    topPath.head
  }

  private def bottomNode(envelope: Envelope): PathNode = {
    val bottomPath: Path = envelope.last
    bottomPath.head
  }

  

  private def moveRightFrom(path: Path): Path = {
    val node = path.head
    PathNode(x = node.x + 1, y = node.y) :: (if (!node.diagonalMove && path.tail.nonEmpty) path.tail else path)
  }

  private def moveDownFrom(path: Path): Path = {
    val node = path.head
    PathNode(x = node.x, y = node.y + 1) :: (if (!node.diagonalMove && path.tail.nonEmpty) path.tail else path)
  }

  private def moveDiagonalFrom(path: Path): Path = {
    val node = path.head
    PathNode(node.x + 1, node.y + 1, diagonalMove = true) :: (if (node.diagonalMove) path.tail else path)
  }

  private def moveDiagonalsAsFarAsPossible(path: Path): Path = {
    val node = path.head
    if (!graph.isDiagonalAt(node.x, node.y)) path
    else moveDiagonalsAsFarAsPossible(moveDiagonalFrom(path))
  }
}

case class PathNode(x: Int, y: Int, diagonalMove: Boolean = false) {
  def <(node: PathNode) = x < node.x

  def >(node: PathNode) = x > node.x
}


