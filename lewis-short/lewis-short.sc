
// Lewis-Short is big. Start with: scala -J-Xms256m -J-Xmx4096m

import scala.xml._
import edu.holycross.shot.xmlutils._

val ls = XML.loadFile("ls.xml")
val entries = ls \\ "entryFree"

case class ShortEntry(id: String, lemma: String, fullDef: String) {

  def cex: String = {
    s"${id}#${lemma}#${fullDef}"
  }
}

def threeFields(n: Node): ShortEntry = {
  val idNodes = n.attribute("id")
  val idVal  = (idNodes.get(0).text)

  val keyNodes = n.attribute("key")
  val keyVal  = (keyNodes.get(0).text)

  /*
  val senses = n \\ "sense"
  val initialDef = if (senses.size > 0) { senses(0).text.replaceAll("\"", "")} else {keyVal}
  */

  ShortEntry(idVal, keyVal, TextReader.collectText(n))

}

val tabular = entries.map(threeFields(_)).map(_.cex).mkString("\n")

import java.io.PrintWriter
new PrintWriter("lewis-short.cex") {write(tabular); close; }
