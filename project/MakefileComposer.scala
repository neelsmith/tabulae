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
    composeInflectionMake(projectDir, fstcompiler)
    //composeVerbStemMake(projectDir, fstcompiler)

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
    if (! projectDir.exists) {
      throw new Exception("Cannot compose main makefile for nonexistent directory " + projectDir)
    }

    val acceptorDir = projectDir/"acceptors"
    val makeFileText = StringBuilder.newBuilder
    makeFileText.append(s"${projectDir.toString}/latin.a: ${projectDir.toString}/symbols.fst ${projectDir.toString}/symbols/phonology.fst ${projectDir.toString}/inflection.a ${projectDir.toString}/acceptor.a \n")


    if (acceptorDir.exists) {
      val dotAs = dotAsForFst(acceptorDir).mkString(" ")
      makeFileText.append(s"${projectDir.toString}/verb.a: " + dotAs + "\n\n")
    }

    //makeFileText.append(s"${projectDir.toString}/acceptor.a: ")
    // collect dotAs?
    //${projectDir.toString}/verb.a\n\n")


/*
    for (d <- subDirs(projectDir / "acceptors")) {
      val subDotAs = dotAsForFst(d)
      makeFileText.append(d.toString() + ".a: " + subDotAs + "\n\n")
    }
*/


    //("DOT AS WERE " + dotAs.mkString("\n"))
    //val acceptorsFst = (projectDir / "acceptors") ** "*fst"
    //val acceptorsFstFiles = acceptorsFst.get
    //val dotAs = acceptorsFst.map(_.toString().replaceFirst(".fst$", ".a"))
//    makeFileText.append(dotAs.mkString(" ") + "\n")



    makeFileText.append("%.a: %.fst\n\t" + fstcompiler + " $< $@\n")
     //later:  ${projectDir.toString}/generator.a ")
    //Utils.dir(projectDir)

    val makeFile = projectDir/"makefile"
    makeFile.overwrite(makeFileText.toString)
  }


  /** Compose makefile for verb subdirectory.
  * This will generate invalid make unless there is at
  * least one file with verb rules in acceptors/verb.
  */
  /*
  def composeVerbStemMake(projectDir: ScalaFile, fstcompiler: String) : String = {
    val makeFileText = StringBuilder.newBuilder
    makeFileText.append(s"${projectDir.toString}/acceptors/verbstems.a: ")
    val inflDir = projectDir/"acceptors/verb"

    if (inflDir.exists) {
      val inflFstFiles = inflDir.glob("*.fst")
      val dotAs = inflFstFiles.map(_.toString().replaceFirst(".fst$", ".a"))
      makeFileText.append(dotAs.mkString(" ") + "\n")
      makeFileText.toString
    } else {
      ""
    }

  }
  */

  /** Compose makefile for inflection subdirectory.  This requires
  * that data already be installed in projectDir/inflection.
  *
  * @param projectDir  Directory for corpus-specific parser.
  * @param fstcompiler Path to binary FST compiler.
  */
  def composeInflectionMake(projectDir: ScalaFile, fstcompiler: String) : Unit = {
      val inflDir = projectDir/"inflection"
      if (! inflDir.exists) {
        throw new Exception("MakefileComposer: no inflection rules installed.")
      }

      val makeFileText = StringBuilder.newBuilder
      makeFileText.append(s"${projectDir.toString}/inflection.a: ")
      val inflFstFiles = inflDir.glob("*.fst").toVector

      //this works correctly:
      val dotAs = inflFstFiles.filter(_.nonEmpty).map(_.toString().replaceFirst(".fst$", ".a"))

      makeFileText.append(dotAs.mkString(" ") + "\n")
      makeFileText.append("%.a: %.fst\n\t" + fstcompiler + " $< $@\n")

      val makefile = inflDir / "makefile"
      makefile.overwrite(makeFileText.toString)
  }


}
