import better.files.{File => ScalaFile, _}
import better.files.Dsl._


case class Corpus(dataSource: ScalaFile, repo: ScalaFile, corpus: String) {

  /** Directory for corpus. */
  def dir : ScalaFile = {
    val d =  dataSource/corpus
    if (!d.exists) {
      mkdir(d)
    }
    d
  }

}
