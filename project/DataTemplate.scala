import better.files.{File => ScalaFile, _}
import better.files.Dsl._

object DataTemplate {

  def apply(srcDir: ScalaFile, targetDir: ScalaFile): Unit = {
    if (! targetDir.exists){ mkdirs(targetDir)} else {}
    println("Data template: now " + targetDir + " exists.")
    copyMarkdown(srcDir, targetDir)
    copyCex(srcDir,targetDir)
    copyFst(srcDir,targetDir)
  }


  def copyFst(srcDir: ScalaFile, targetDir: ScalaFile): Unit = {
     val fstFiles = srcDir.glob("*.fst").toVector
     for (f <- fstFiles) {
       f.copyToDirectory(targetDir)
     }
  }

  def copyCex(srcDir: ScalaFile, targetDir: ScalaFile): Unit = {
     val fstFiles = srcDir.glob("*.cex").toVector
     for (f <- fstFiles) {
       f.copyToDirectory(targetDir)
     }
  }

  def copyMarkdown(srcDir: ScalaFile, targetDir: ScalaFile): Unit = {
     val fstFiles = srcDir.glob("*.fmdst").toVector
     for (f <- fstFiles) {
       f.copyToDirectory(targetDir)
     }
  }
}
