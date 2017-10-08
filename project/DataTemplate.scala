import sbt._
 import Path.rebase

object DataTemplate {

  def apply(srcDir: File, targetDir: File): Unit = {
    copyMarkdown(srcDir, targetDir)
    copyCex(srcDir,targetDir)
    copyFst(srcDir,targetDir)

  }


  def copyFst(srcDir: File, targetDir: File): Unit = {
     val fst = (srcDir) ** "*.fst"
     val fstFiles = fst.get

     val mappings: Seq[(File,File)] = fstFiles pair rebase(srcDir, targetDir)

     println("\ncopying data files...")
     for (m <- mappings) {
       println("  ..copy " + m._1 + " -> " + m._2)
       IO.copyFile(m._1, m._2)
     }
  }

  def copyCex(srcDir: File, targetDir: File): Unit = {
     val cex = (srcDir) ** "*.cex"
     val cexFiles = cex.get

     val mappings: Seq[(File,File)] = cexFiles pair rebase(srcDir, targetDir)

     println("\ncopying data files...")
     for (m <- mappings) {
       println("  ..copy " + m._1 + " -> " + m._2)
       IO.copyFile(m._1, m._2)
     }
  }

  def copyMarkdown(srcDir: File, targetDir: File): Unit = {
     val readmes = (srcDir) ** "*.md"
     val readmeFiles = readmes.get

     val mappings: Seq[(File,File)] = readmeFiles pair rebase(srcDir, targetDir)

     println("\ncopying data files...")
     for (m <- mappings) {
       println("  ..copy " + m._1 + " -> " + m._2)
       IO.copyFile(m._1, m._2)
     }
  }
}
