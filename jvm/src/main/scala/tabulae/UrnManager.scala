package edu.holycross.shot.tabulae

import edu.holycross.shot.cite._
import scala.io.Source

case class UrnManager(collectionMap: Map[String, Cite2Urn]) {
  def resolve(id: String): Option[Cite2Urn] = {
    collectionMap get id
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
