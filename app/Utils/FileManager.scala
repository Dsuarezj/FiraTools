package Utils

import java.io.{File, FileWriter}

import configurations.Control._

import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileManager {

  def readCsvFile(file: File): Option[List[String]] = {
    safeReadCsvFromFile(file) match {
      case Success(lines) => lines
      case Failure(s) => {
        // TODO: Create a logger class
        println(s"Error || to read file: $file, message is: $s")
        None
      }
    }
  }

  def readCsvFileFromPath(file: String): Option[List[String]] = {
    safeReadCsvFromPath(file) match {
      case Success(lines) => lines
      case Failure(s) => {
        // TODO: Create a logger class
        println(s"Error || to read file: $file, message is: $s")
        None
      }
    }
  }

  def readTextFile(file: String): Option[String] = {
    safeReadTextFromPath(file) match {
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
    import java.io.{BufferedInputStream, FileInputStream, FileOutputStream}
    import java.util.zip.{ZipEntry, ZipOutputStream}

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

  private def safeReadCsvFromFile(path: File): Try[Some[List[String]]] = {
    Try{
      val lines = using(Source.fromFile(path)) { source =>
        (for (line <- source.getLines) yield line).toList
      }
      Some(lines)
    }
  }

  private def safeReadCsvFromPath(path: String): Try[Some[List[String]]] = {
    Try{
      val lines = using(Source.fromFile(path)) { source =>
        (for (line <- source.getLines) yield line).toList
      }
      Some(lines)
    }
  }

  private def safeReadTextFromPath(path: String): Try[Some[String]] = {
    Try{
      val lines = using(Source.fromFile(path)) { source =>
        source.mkString
      }
      Some(lines)
    }
  }
}
