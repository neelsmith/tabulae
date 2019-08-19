package edu.holycross.shot.tabulae.builder

import better.files.{File => ScalaFile, _}
import better.files.Dsl._


/** Write makefiles to compile all FST source to binary transducers.
*/
object MakefileComposer {

  /** Write makefiles for Kanónes project in a given directory.
  *
  * @param projectDir Directory for corpus-specific parser.
  * @param fstcompiler Full path to FST compiler.
  */
  def apply(projectDir: ScalaFile, fstcompiler: String) : Unit = {
    if (! projectDir.exists) {
      throw new Exception("MakefileComposer: cannot compose main makefile for nonexistent directory " + projectDir)
    }
    if (! (projectDir / "symbols.fst").exists) {
      throw new Exception("MakefileComposer: cannot compose main makefile until symbols.fst is installed in " + projectDir)
    }
    if (! (projectDir / "symbols/phonology.fst").exists) {
      throw new Exception("MakefileComposer: cannot compose main makefile until symbols/phonology.fst is installed in " + projectDir)
    }
    if (! (projectDir / "inflection.fst").exists) {
      throw new Exception("MakefileComposer: cannot compose main makefile until inflection.fst is installed in " + projectDir)
    } else {
      //println("KFOUND inflection.fst in " + projectDir)
    }

    composeInflectionMake(projectDir, fstcompiler)
    composeMainMake(projectDir, fstcompiler)
  }


  /** Collect a list of file names in `.a` for `.fst` files
  * in a given directory.
  *
  * @param dir Directory with .fst files.
  */
  def dotAsForFst(dir: ScalaFile) : Vector[String] = {
    val fstFiles = dir.glob("*.fst").toVector
    fstFiles.map(_.toString().replaceFirst(".fst$", ".a")).toVector
  }



  /** Collect all subdirectories of a given directory.
  *
  * @param dir Directory to look in for subdirectories.
  */
  def subDirs(dir: ScalaFile) : Vector[ScalaFile] = {
    dir.children.filter(_.isDirectory).toVector
  }


  /** Compose main makefile for parser.
  *
  * @
  */
  def composeMainMake(projectDir: ScalaFile, fstcompiler: String): Unit = {
    //val acceptorDir = projectDir / "acceptors"
    val makeFileText = StringBuilder.newBuilder
    makeFileText.append(s"${projectDir.toString}/latin.a: ${projectDir.toString}/symbols.fst ${projectDir.toString}/symbols/phonology.fst ${projectDir.toString}/inflection.a ${projectDir.toString}/acceptor.a \n")


    /*
    if (acceptorDir.exists) {
      val dotAs = dotAsForFst(acceptorDir).mkString(" ")
      makeFileText.append(s"${projectDir.toString}/verb.a: " + dotAs + "\n\n")
    }*/

    makeFileText.append("%.a: %.fst\n\t" + fstcompiler + " $< $@\n")
     //later:  ${projectDir.toString}/generator.a ")
    //Utils.dir(projectDir)

    val makeFile = projectDir / "makefile"
    makeFile.overwrite(makeFileText.toString)
  }


  /** Compose makefile for inflection subdirectory.  This requires
  * that data already be installed in projectDir/inflection.
  *
  * @param projectDir  Directory for corpus-specific parser.
  * @param fstcompiler Path to binary FST compiler.
  */
  def composeInflectionMake(projectDir: ScalaFile, fstcompiler: String) : Unit = {
      val inflDir = projectDir / "inflection"
      if (! inflDir.exists) {
        throw new Exception("MakefileComposer: no inflection rules installed.")
      }

      val makeFileText = StringBuilder.newBuilder
      makeFileText.append(s"${projectDir.toString}/inflection.a: ")
      val inflFstFiles = inflDir.glob("*.fst").toVector

      val dotAs = inflFstFiles.filter(_.nonEmpty).map(_.toString().replaceFirst(".fst$", ".a"))

      makeFileText.append(dotAs.mkString(" ") + "\n")
      makeFileText.append("%.a: %.fst\n\t" + fstcompiler + " $< $@\n")

      val makefile = inflDir / "makefile"
      makefile.overwrite(makeFileText.toString)
  }


}
