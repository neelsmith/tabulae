package edu.holycross.shot.tabulae.builder


import better.files.{File => ScalaFile, _}
import better.files.Dsl._


/** In tabulae, a [[TabulaeDataSet]] is a morphological corpus: that is,
* a dataset comprising rules tables, stem tables, and tables of
* irregular forms for a specific set of texts.
*
* @param dataSource Directory hosting one or more subdirectories
* with tabulae datasets corresponding to specific corpora.
* @param repo Root of tabulae directory where the core parsing
* logic in FST notation can be found.
* @param corpus A string identifying the name a directory with
* dataSource.  In compiling a parser for this corpus, the same name
*  will be used for a new directory in the repository's "parsers" * *  *  subdirectory .
*/
case class TabulaeDataSet(dataSource: ScalaFile, repo: ScalaFile, corpus: String) {

  /** Directory for corpus. */
  def dir : ScalaFile = {
    val d =  dataSource / corpus
    if (!d.exists) {
      mkdir(d)
    }
    d
  }

}
