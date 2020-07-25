package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import scala.io.Source

case class UrnManager(collectionMap: Map[String, Cite2Urn]) {
  def resolve(id: String): Option[Cite2Urn] = {
    collectionMap get id
  }

  def urn(id: String): Option[Cite2Urn] = {
    val parts = id.split("\\.")
    parts.size match {
      case 2 => {
        try {
          val u = resolve(parts(0)).get
          Some(u.addSelector(parts(1)))

        } catch {
          case t: Throwable => {
            //warn
            None
          }
        }
      }
      case _ => {
        //warn
        None
      }
    }
  }
}


object UrnManager {

  def apply(lines : Vector[String]) : UrnManager = {
    val columns = lines.map(_.split("#")).toVector.tail
    val idMap = Map.empty[String, Cite2Urn] ++ columns.map(row => (row(0) -> Cite2Urn(row(1))))
    UrnManager (idMap)
  }

  def fromFile(f: String): UrnManager = {
    val lines = Source.fromFile(f).getLines.toVector
    UrnManager(lines)
  }

  def fromUrl(url: String): UrnManager = {
    val lines = Source.fromURL(url).getLines.toVector
    UrnManager(lines)
  }
}
