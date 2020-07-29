package services
import configurations.Control._

import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileReader {

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
