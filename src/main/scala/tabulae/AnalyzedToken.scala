package edu.holycross.shot.tabulae




/** Association of [[Form]]s with a surface string (token).
*
* @param token A morphologically analyzed surface form.
* @param analyses [[Form]]s associated with this token.
*/
case class AnalyzedToken(token: String, analyses: Vector[LemmatizedForm])
