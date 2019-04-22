package edu.holycross.shot.tabulae

import scala.io.Source


/** Factory object to create full [[Form]] from a string of FST.
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


  /** Recursively pop off analyses for first lines in a Vector of FST strings until a new token
  * is encountered.
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

  def formsFromFile(f: String) = {

  }

}
