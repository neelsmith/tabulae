package edu.holycross.shot.tabulae

import scala.io.Source



import wvlet.log._
import wvlet.log.LogFormatter.SourceCodeLogFormatter


object LewisShort extends LogSupport {
  Logger.setDefaultLogLevel(LogLevel.WARN)

  val indexUrl = "https://raw.githubusercontent.com/Eumaeus/cex_lewis_and_short/master/ls_indexData.txt"
  lazy val lemmaIndex = Source.fromURL(indexUrl).getLines.toVector
  val lsIdMap = for (ln <- lemmaIndex) yield {
    val parts = ln.split("#")
    s"ls.${parts(0)}" -> parts(1)
  }
  val idMap = lsIdMap.toMap


  def label(s: String) = {
    try {
      s + ":" + idMap(s)
    } catch {
      case t: Throwable => s
    }
  }

  def guessId(s: String) : Option[String] = {
    val candidates = LewisShort.lsIdMap.filter(_._2 == s)
    candidates.size match {
      case 0 => None
      case 1 => Some(candidates(0)._1)
      case _ => {
        warn("LewisShort found multiple matches for lemma form " + s)
        None
      }
    }

  }

}
