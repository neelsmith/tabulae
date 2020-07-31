import edu.holycross.shot.cite._
import edu.holycross.shot.tabulae._
import scala.io.Source
val f = "cex/morphology-permutation.cex"

case class ProofRead(urn: Cite2Urn, label: String)

val data = Source.fromFile(f).getLines.toVector

val permuted = data.tail.map{ ln =>
  val cols = ln.split("#")
  val urn = Cite2Urn(cols(0))
  ProofRead(urn, cols(1))
}
