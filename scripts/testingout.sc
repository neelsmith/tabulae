import sys.process._
import scala.language.postfixOps

val parsing = "fst-infl parsers/a-g/latin.a verbs.txt" !!

val parseLines =  parsing.split("\n")

for (p <- 0 until parseLines.size by 2) {
  print( "Form: " + parseLines(p).replaceFirst("> ", ""))
  println(" analyzed as " + parseLines(p + 1))
}
