package services
import java.io.{BufferedWriter, File, FileWriter}

import configurations.Control._

import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileManager {

  def readCsvFile(file: String): Option[List[String]] = {
    safeReadCsvFile(file) match {
      case Success(lines) => lines
      case Failure(s) => {
        // TODO: Create a logger class
        println(s"Error || to read file: $file, message is: $s")
        None
      }
    }
  }

  def readTextFile(file: String): Option[String] = {
    safeReadTextFile(file) match {
      case Success(lines) => lines
      case Failure(s) => {
        // TODO: Create a logger class
        println(s"Error || to read file: $file, message is: $s")
        None
      }
    }
  }

  def writeFile(path: String, data: String): Unit =
    using(new FileWriter(path))(_.write(data))

  def zip(out: String, files: Iterable[String]) = {
    import java.io.{ BufferedInputStream, FileInputStream, FileOutputStream }
    import java.util.zip.{ ZipEntry, ZipOutputStream }

    val zip = new ZipOutputStream(new FileOutputStream(out))

    files.foreach { name =>
      zip.putNextEntry(new ZipEntry(name))
      val in = new BufferedInputStream(new FileInputStream(name))
      var b = in.read()
      while (b > -1) {
        zip.write(b)
        b = in.read()
      }
      in.close()
      zip.closeEntry()
    }
    zip.close()
  }

  private def safeReadCsvFile(file: String): Try[Some[List[String]]] = {
    Try{
      val lines = using(Source.fromFile(file)) { source =>
        (for (line <- source.getLines) yield line).toList
      }
      Some(lines)
    }
  }

  private def safeReadTextFile(file: String): Try[Some[String]] = {
    Try{
      val lines = using(Source.fromFile(file)) { source =>
        source.mkString
      }
      Some(lines)
    }
  }
}
