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


  def formsFromFile(f: String) = {

  }

}
