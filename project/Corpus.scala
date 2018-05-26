import sbt._
import scala.io.Source
import java.io.PrintWriter
import java.io.File

case class Corpus(dataSource: File, repo: File, corpus: String) {

  /** Directory for corpus. */
  def dir : File = {
    var d = dataSource / corpus
    if (! d.exists) {  d.mkdir } else {}
    d
  }
}
