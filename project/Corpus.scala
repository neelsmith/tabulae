import better.files.{File => ScalaFile, _}
import better.files.Dsl._
import java.io.File

case class Corpus(dataSource: ScalaFile, repo: File, corpus: String) {

  /** Directory for corpus. */
  def dir : ScalaFile = {
    val d =  dataSource/corpus
    if (!d.exists) {
      mkdir(d)
    }
    d
  }

}
