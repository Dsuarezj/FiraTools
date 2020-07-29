package services

import java.io.{BufferedInputStream, FileInputStream}
import java.nio.file.{Files, Path, Paths}

import javax.inject.Inject

import scala.collection.mutable.ListBuffer


class TemplateHandler @Inject()(fileManager: FileManager) {

  // TODO: depending of the deploy used different path, the one with target is to work on heroku
  def getPreviewOfFiles(templateId: String): String = {
    val variables = readVariables(templateId)
    val headers = variables.head.split(",").map(_.trim).map(header => header.replaceAll("\uFEFF", ""))
    val template = readTemplate(templateId)
    val validHeaders: Array[String] = headers.filter(header => template.contains(header))

    var changedTemplates = new ListBuffer[String]()
    var substitutions = collection.mutable.Map[String, String]()

    variables.foreach( x => {
      x.split(",").zipWithIndex.foreach{ case (item, index) => {
        substitutions += (validHeaders(index) -> item)
      }
      }
      changedTemplates += "\n" + substitutions.foldLeft(template)((a, b) => a.replaceAllLiterally(b._1, b._2))+ "\n"
      changedTemplates += "********************************************************************\n"
    })
    changedTemplates.toList.mkString
  }

  def createFilesAndGetZipPath(templateId: String) = {
    val variables = readVariables(templateId)
    val headers = variables.head.split(",").map(_.trim).map(header => header.replaceAll("\uFEFF", ""))
    val template = readTemplate(templateId)
    val validHeaders: Array[String] = headers.filter(header => template.contains(header))

    var substitutions = collection.mutable.Map[String, String]()

    var filesPath = new ListBuffer[String]()

    variables.foreach(variable => {
        variable.split(",").zipWithIndex.foreach{ case (item, index) => {
          substitutions += (validHeaders(index) -> item)
        }
      }
      val emailWithName = substitutions.foldLeft(template)((a, b) => a.replaceAllLiterally(b._1, b._2))
      val emailId = substitutions.getOrElse(validHeaders.head, "")
      val createdEmailPath = s"./target/universal/stage/$emailId-$templateId.html"
      filesPath += createdEmailPath
      fileManager.writeFile(createdEmailPath, emailWithName)
    })

    val zipPath = s"/tmp/$templateId.zip"
    fileManager.zip(zipPath, filesPath)
    zipPath
  }

  def readTemplate(templateId: String): String = {
    val path = s"./target/universal/stage/$templateId.html"
    fileManager.readTextFile(path) match {
      case Some(lines) => lines
      case _ => fileManager.readTextFile(s"./$templateId.html") match {
        case Some(lines) => lines
        case _ => ""
      }
    }
  }

  def readVariables(templateId: String): List[String] = {
    val path = s"./target/universal/stage/$templateId.csv"
    fileManager.readCsvFile(path) match {
      case Some(lines) => lines
      case _ => fileManager.readCsvFile(s"./$templateId.csv") match {
        case Some(lines) => lines
        case _ => List()
      }
    }
  }

}
