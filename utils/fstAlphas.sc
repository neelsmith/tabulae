import edu.holycross.shot.latin._
import java.io.File

val agDir = new File("datasets/a-g")

val alphalist = Vector(
  (Latin23Alphabet, "latin23alphabet.fst"),
  (Latin24Alphabet, "latin24alphabet.fst"),
  (Latin25Alphabet, "latin25alphabet.fst")
)

import java.io.PrintWriter
def writeFst(dir: File) = {
  for (a <- alphalist) {
    val outFile = new File(dir, a._2)
    new PrintWriter(outFile){write(a._1.fstString); close;}
    println("Wrote alphabet for " + a._1.label)
  }
}


println("\n\nWrite out FST files for a list of alphabets:\n")

println("\twriteFst(OUTPUT_DIRECTORY)\n")


println("(OUTPUT_DIRECTORY is an existing direcdtory.)")
