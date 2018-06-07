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
    //val inflDir = projectDir / "inflection"

    composeInflectionMake(projectDir, fstcompiler)
    composeVerbStemMake(projectDir, fstcompiler)

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

  def composeMainMake(projectDir: ScalaFile, fstcompiler: String): Unit = {
    val makeFileText = StringBuilder.newBuilder
    makeFileText.append(s"${projectDir.toString}/latin.a: ${projectDir.toString}/symbols.fst ${projectDir.toString}/symbols/phonology.fst ${projectDir.toString}/inflection.a ${projectDir.toString}/acceptor.a \n")

    val dotAs = dotAsForFst(projectDir/"acceptors").mkString(" ")
    makeFileText.append(s"${projectDir.toString}/verb.a: " + dotAs + "\n\n")

    makeFileText.append(s"${projectDir.toString}/acceptor.a: ${projectDir.toString}/verb.a\n\n")
    makeFileText.append(composeVerbStemMake(projectDir, fstcompiler))


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
  def composeVerbStemMake(projectDir: ScalaFile, fstcompiler: String) : String = {
    (s"\nWrite makefile for verb stem trandsducers in project ${projectDir}\n")
    val makeFileText = StringBuilder.newBuilder
    makeFileText.append(s"${projectDir.toString}/acceptors/verbstems.a: ")
    val inflDir = projectDir/"acceptors/verb"
    val inflFstFiles = inflDir.glob("*.fst").toVector
    val dotAs = inflFstFiles.map(_.toString().replaceFirst(".fst$", ".a"))

    makeFileText.append(dotAs.mkString(" ") + "\n")
    makeFileText.toString
    //makeFileText.append("%.a: %.fst\n\t" + fstcompiler + " $< $@\n")

  //  val makefile = projectDir / "acceptors/verb/makefile"
  //  new PrintWriter(makefile) { write(makeFileText.toString); close }
  }

  /** Compose makefile for inflection subdirectory.
  */
  def composeInflectionMake(projectDir: ScalaFile, fstcompiler: String) : Unit = {
      val inflDir = projectDir/"inflection"
      if (! inflDir.exists) {
        throw new Exception("MakefileComposer: no inflection rules installed.")
      } else {
        println (s"\nWrite makefile for inflection rules in project ${projectDir}\n")
      }

      val makeFileText = StringBuilder.newBuilder
      makeFileText.append(s"${projectDir.toString}/inflection.a: ")
      val inflFstFiles = inflDir.glob("*.fst").toVector

      val dotAs = inflFstFiles.filter(!_.isEmpty).map(_.toString().replaceFirst(".fst$", ".a"))

      makeFileText.append(dotAs.mkString(" ") + "\n")
      makeFileText.append("%.a: %.fst\n\t" + fstcompiler + " $< $@\n")

      val makefile = inflDir / "makefile"
      makefile.overwrite(makeFileText.toString)
  }


}
