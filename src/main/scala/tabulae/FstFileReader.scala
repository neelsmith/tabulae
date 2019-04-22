package edu.holycross.shot.tabulae

import scala.io.Source


/**
*
*
*/
object FstFileReader {


  /** True if s represents a token.
  *
  * @param s Line of fst output.
  */
  def isToken(s: String): Boolean = {
    val tokenPattern = "^> .+"
    s.matches(tokenPattern)
  }


  /** Recursively pop off analyses for first lines in a Vector of FST strings
  * until a new token  is encountered.
  *
  * @param fstLines Vector of FST output strings.
  * @param analysisVector Previously seen analyses.
  */
  def popAnalyses(fstLines: Vector[String], analysisVector: Vector[Form] =  Vector.empty): Vector[Form] = {
    if (fstLines.isEmpty) {
      analysisVector
    } else {
      if (isToken(fstLines.head)) {
        analysisVector
      } else {
        popAnalyses(fstLines.tail, analysisVector :+ Form(fstLines.head) )
      }
    }
  }

  /** Pop one token + analyses as an [[AnalyzedToken]] from the stack of FST lines.
  * Since this is a pure function that does not affect the initial Vector of
  * Strings, you can use the complementary [[dropAnalyses]] function to
  * find the Vector of FST Strings remaining when the analyses captured here
  * are popped off.
  *
  * @param fstLines Vector of FST output strings.  It should begin with
  * a token string, and be followed by one or more anlaysis strings.
  */
  def popAnalyzedToken(fstLines: Vector[String]) : AnalyzedToken  = {
    isToken(fstLines.head) match {
      case false => throw new Exception("FST does not begin with a token value.")
      case true => {
        val surfaceForm = fstLines.head.replaceFirst("^> ","")
        val analyses =  popAnalyses(fstLines.tail)
        AnalyzedToken(surfaceForm, analyses)
      }
    }
  }


  /** Recursively pop off lines from top of stack until a token is encountered.
  *
  * @param fstLines Lines of FST output.
  */
  def dropAnalyses(fstLines: Vector[String]):  Vector[String] = {
    if (fstLines.isEmpty) {
      fstLines
    } else {
      isToken(fstLines.head) match {
        case true => fstLines
        case false => dropAnalyses(fstLines.tail)
      }
    }
  }


  /** Analyze a Vector of FST Strings as [[AnalyzedToken]]s.
  *
  * @param fstLines FST Strings to analyze.
  * @param analyzed Previously seen [[AnalyzedToken]] objects.
  */
  def parseFstLines(fstLines: Vector[String], analyzed : Vector[AnalyzedToken] = Vector.empty[AnalyzedToken]): Vector[AnalyzedToken] = {
    if (fstLines.isEmpty) {
      analyzed
    } else {
      //println("\npopping token for " + fstLines.head)
      val analyzedToken = popAnalyzedToken(fstLines)
      //println("Got " + analyzedToken + "\n")
      val remainingFst = dropAnalyses(fstLines.tail)
      parseFstLines(remainingFst, analyzed :+ analyzedToken)
    }
  }


  def formsFromFile(fileName: String) : Vector[AnalyzedToken]= {
    val lines = Source.fromFile(fileName).getLines.toVector
    parseFstLines(lines)
  }

}
