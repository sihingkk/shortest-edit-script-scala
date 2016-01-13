package diff

trait Differ {
  def compare(left: Seq[_], right: Seq[_]): List[Change]
}