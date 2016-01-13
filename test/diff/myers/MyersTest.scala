package diff.myers

import org.specs2.mutable.Specification

class MyersTest extends Specification {

  "extending envelope" should {
    "go right and left if there is no diagonal" in {
      new Myers(EditorGraph( Seq("a"),Seq("b"))).findNextEnvelope(
        List(List(PathNode(0, 0)))
      ) should containTheSameElementsAs(
        List(
          List(PathNode(1, 0), PathNode(0, 0)),
          List(PathNode(0, 1), PathNode(0, 0))
        )
      )

      "choose further (with greater x) node as previous node in next envelope" in {
        new Myers(EditorGraph(
            Seq("a", "b", "c", "d"),
            Seq("1", "2", "3", "4")
        )).findNextEnvelope(
          List(
            List(PathNode(1, 0)),
            List(PathNode(2, 1))
          )
        ) should equalTo(
          List(
            List(PathNode(2, 0), PathNode(1, 0)),
            List(PathNode(3, 1), PathNode(2, 1)),
            List(PathNode(2, 2), PathNode(2, 1))
          )
        )
      }

      "dont go over the graph boundary" in {
        val envelope = new Myers(EditorGraph(List("a"), List("c"))).envelopes(2)
        envelope.map(_.head.x) must contain(be_<(2)).forall
        envelope.map(_.head.y) must contain(be_<(2)).forall
      }

      "follow diagonal in the beginning" in {
        new Myers(EditorGraph(List("a","b"), List("a","b"))).solution  should beEqualTo(List(PathNode(2,2,diagonalMove = true)))
      }

      "follow the diagonal" in {
        new Myers(EditorGraph(List("a","b","c"), List("a","b","d"))).solution.last  should beEqualTo(PathNode(2,2,diagonalMove = true))
      }
    }
  }
}
