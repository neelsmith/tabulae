package edu.holycross.shot.tabulae

import scala.io.Source


/**
* A utility object for converting analyses of lexical tokens
* written in SFST output form into objects.
*
*/
object FstFileReader {

  def fromFile(fileName: String) : Vector[AnalyzedToken]= {
    val lines = Source.fromFile(fileName).getLines.toVector
    FstReader.parseFstLines(lines.filter(_.nonEmpty))
  }

}
