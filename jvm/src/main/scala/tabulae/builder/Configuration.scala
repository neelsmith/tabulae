package edu.holycross.shot.tabulae.builder

import better.files.{File => ScalaFile, _}
import better.files.Dsl._


/** Required configuration information to compile a binary parser.
*
* @param fstcompile Path to fst-compiler-utf8
* @param fstinfl Path to fst-infl
* @param make Path to make
* @param datadir Name, as a String, of root directory for data sets.
*/
case class Configuration(fstcompile: String, fstinfl: String, make: String, datadir: String = "datasets") {

  require(ScalaFile(fstcompile).exists(), "No fst compiler named " + fstcompile + " found.")
  require(ScalaFile(fstinfl).exists(), "No file named " + fstinfl + " found.")
  require(ScalaFile(make).exists(), "No file named " + make + " found.")
  require(ScalaFile(datadir).exists(), "No directory named " + datadir + " found.")
}


/** Factory for constructing a [[Configuration]] from a config file.
*/
object Configuration {

  /** Create a [[Configuration]] instance from a  configuration file.
  *
  * @param config Configuration file.
  */
  def apply(config: ScalaFile): Configuration = {
    val lines = config.lines.toVector.filter(_.nonEmpty).filter(_(0) != '#')
    val mapped = lines.map( l => {
      val parts = l.split("=")
      parts(0).trim -> parts(1).trim
    }).toMap
    Configuration(mapped("FSTCOMPILER"),mapped("FSTINFL"),mapped("MAKE"),mapped("datadir"))
  }

}
