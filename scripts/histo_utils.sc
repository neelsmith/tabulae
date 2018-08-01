trait HistoType[V] extends Serializable {
  val name = this.getClass.getSimpleName
}

trait Histogram[V] extends HistoType[V] {
  val counts : Vector[(V, Int)]
}

case class StringHistogram (counts: Vector[(String,Int)]) extends Histogram[String]
