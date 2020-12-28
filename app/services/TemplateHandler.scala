package services

import utils.FileManager

import java.util.UUID
import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.util.Random


class TemplateHandler @Inject()(fileManager: FileManager) {

  def getPreviewOfFiles(templateId: String): String = {
    val variables = readVariables(templateId)
    val csvSeparator = getCsvSeparator(variables.head)
    val headers = variables.head.split(csvSeparator).map(_.trim).map(header => header.replaceAll("\uFEFF", ""))
    val template = readTemplate(templateId)

    var changedTemplates = new ListBuffer[String]()
    var substitutions = collection.mutable.Map[String, String]()

    variables.foreach(x => {
      x.split(csvSeparator).zipWithIndex.foreach { case (item, index) =>
        substitutions += (headers(index) -> item)
      }
      changedTemplates += "\n" + substitutions.foldLeft(template)((a, b) => a.replaceAllLiterally(b._1, b._2)) + "\n"
      changedTemplates += "********************************************************************\n"
    })
    changedTemplates.toList.mkString
  }

  def createFilesAndGetZipPath(templateId: String) = {
    val variables = readVariables(templateId)
    val csvSeparator = getCsvSeparator(variables.head)
    val headers = variables.head.split(csvSeparator).map(_.trim).map(header => header.replaceAll("\uFEFF", ""))
    val template = readTemplate(templateId)

    var substitutions = collection.mutable.Map[String, String]()

    var filesPath = new ListBuffer[String]()

    variables.foreach(variable => {
      variable.split(csvSeparator).zipWithIndex.foreach { case (item, index) => {
        substitutions += (headers(index) -> item)
      }
      }
      val emailWithName = substitutions.foldLeft(template)((a, b) => a.replaceAllLiterally(b._1, b._2))
      val emailUniqueId = "%s-%s"
        .format(substitutions.getOrElse(headers.head, ""), UUID.randomUUID())
      val createdEmailPath = s"./$emailUniqueId.html"
      filesPath += createdEmailPath
      fileManager.writeFile(createdEmailPath, emailWithName)
    })

    val zipPath = s"/tmp/$templateId.zip"
    fileManager.zip(zipPath, filesPath)
    zipPath
  }

  def readTemplate(templateId: String): String = {
    // TODO: depending of the deploy used different path, the one with target is to work on heroku
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
    fileManager.readCsvFileFromPath(path) match {
      case Some(lines) => lines
      case _ => fileManager.readCsvFileFromPath(s"./$templateId.csv") match {
        case Some(lines) => lines
        case _ => List()
      }
    }
  }

  def getCsvSeparator(headers: String) = {
    val nonAlphanumeric = "[^a-zA-Z\\d\\s\uFEFF]".r
    nonAlphanumeric.findFirstIn(headers).getOrElse(";")
  }

}
