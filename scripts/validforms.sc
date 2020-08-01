import edu.holycross.shot.cite._
import edu.holycross.shot.tabulae._
import scala.io.Source
val f = "cex/morphology-permutation.cex"

val data = Source.fromFile(f).getLines.toVector

val permuted = data.tail.map{ ln =>
  val cols = ln.split("#")
  val urn = Cite2Urn(cols(0))
  val label = cols(1)
  (ValidForm(urn),label)
}
// Nicer to have them sorted by URN
val permutedSorted = permuted.sortBy(_._1.urn.toString)
val ok = permutedSorted.filter(_._1.validUrnValue)

val cex = ok.map{ case (vf, label) => vf.urn + "#" + label }

import java.io.PrintWriter
val hdr = "urn#label\n"
new PrintWriter("forms.cex") { write(hdr + cex.mkString("\n")); close;}
