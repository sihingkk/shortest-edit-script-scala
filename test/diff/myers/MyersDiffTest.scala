package diff.myers

import diff.{Change, ItemChanged, ItemAdded, ItemRemoved}
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class MyersDiffTest extends Specification with ScalaCheck {

  def comparer = new MyersDiff()


  "Applying diff" should {
    "always pass" in prop { (left: Seq[Int], right: Seq[Int]) =>
      val changes = comparer.compare(left, right)
      Change.applyChanges(left, changes) should beEqualTo(right)
    }
  }

  "compare " should {
    "handle empty list comparisons" in {
      comparer.compare(Seq("a"), Seq()) should equalTo(List(ItemRemoved(0, "a")))
      comparer.compare(Seq("a", "c"), Seq()) should equalTo(List(ItemRemoved(0, "a"), ItemRemoved(0, "c")))
      comparer.compare(Seq(), Seq("b")) should equalTo(List(ItemAdded(0, "b")))
      comparer.compare(Seq(), Seq("b", "c")) should equalTo(List(ItemAdded(0, "b"), ItemAdded(1, "c")))
      comparer.compare(Seq(), Seq()) should equalTo(List())
    }

    "handle addition" in {
      comparer.compare(Seq("A", "C"), Seq("A", "B", "C")) should equalTo(
        List(ItemAdded(1, "B"))
      )
    }

    "handle substitution" in {
      comparer.compare(Seq("A", "B", "C"), Seq("E", "B", "C")) should equalTo(List(ItemChanged(0, "A", "E")))
    }

    "handle removal" in {
      comparer.compare(Seq("A", "B", "C", "D"), Seq("A", "C", "D")) should equalTo(
        List(ItemRemoved(1, "B"))
      )
    }

    "handle equal lists" in {
      comparer.compare(Seq("A", "B"), Seq("A", "B")) should equalTo(List())
    }

    "Myers papers example" in {
      val left = Seq("A", "B", "C", "A", "B", "B", "A")
      val right = Seq("C", "B", "A", "B", "A", "C")

      val changes = comparer.compare(left, right)
      Change.applyChanges(left, changes) should beEqualTo(right)
    }
  }
}
