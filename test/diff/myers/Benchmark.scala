package diff.myers

import org.scalameter.api._

import scala.collection.JavaConverters._

class MyerDiffBenchmark extends PerformanceTest.OfflineReport {
  def from(n: Int): Stream[Int] = n #:: from(n + 1)

  def withRemoved(ratio: Int): Stream[Int] = {
    if (List(0, 1).contains(ratio)) Stream.empty
    else from(0).filter(_ % ratio != 0)
  }


  val collectionSize = 10000

  val changes = Gen.range("changes")(1, collectionSize+1, collectionSize/5)
  val changesForDegeneratedSets = Gen.range("changes")(1, collectionSize/2, collectionSize/10)
  val compareWithChangeDense = for (c <- changes) yield (from(0).take(collectionSize), from(c).take(collectionSize))
  val compareWithSparseDeletion = for (c <- changesForDegeneratedSets) yield (from(0).take(collectionSize), withRemoved(collectionSize / c).take(collectionSize - c))


  val options = Context(
    exec.jvmflags -> "-server -Xms1g -Xmx1g",
    exec.minWarmupRuns -> 1,
    exec.maxWarmupRuns -> 1,
    exec.benchRuns -> 3
  )

  performance of "myers greedy functional" in {
    measure method "changes are in beginning" in {
      using(compareWithChangeDense) config options in { inputs =>
        new diff.myers.MyersDiff().compare(inputs._1, inputs._2)
      }
    }
    measure method "regular deleted spread" in {
      using(compareWithSparseDeletion) config options in { inputs =>
        new diff.myers.MyersDiff().compare(inputs._1, inputs._2)
      }
    }
  }
}