import sbt._
import scala.io.Source
import java.io.PrintWriter


case class Configuration(fstcompile: String, fstinfl: String, make: String, datadir: String = "datasets") {

  require(new File(fstcompile).exists(), "No fst compiler named " + fstcompile + " found.")
  require(new File(fstinfl).exists(), "No file named " + fstinfl + " found.")
  require(new File(make).exists(), "No file named " + make + " found.")
  require(new File(datadir).exists(), "No directory named " + datadir + " found.")
}


/** Factory for constructing a [[Configuration]] from a config file.
*/
object Configuration {

  /** Create a [[Configuration]] instance from a  configuration file.
  *
  * @config Configuration file.
  */
  def apply(config: File): Configuration = {
    val lines = Source.fromFile(config).getLines.toVector.filter(_.nonEmpty).filter(_(0) != '#')
    val mapped = lines.map( l => {
      val parts = l.split("=")
      parts(0).trim -> parts(1).trim
    }).toMap
    Configuration(mapped("FSTCOMPILER"),mapped("FSTINFL"),mapped("MAKE"))
  }

}
