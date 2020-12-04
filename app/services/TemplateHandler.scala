package services

import utils.FileManager
import javax.inject.Inject

import scala.collection.mutable.ListBuffer


class TemplateHandler @Inject()(fileManager: FileManager) {

  private var csvSeparator = ","

  def getPreviewOfFiles(templateId: String): String = {
    val variables = readVariables(templateId)
    verifyCsvSeparator(variables)
    val headers = variables.head.split(csvSeparator).map(_.trim).map(header => header.replaceAll("\uFEFF", ""))
    val template = readTemplate(templateId)

    var changedTemplates = new ListBuffer[String]()
    var substitutions = collection.mutable.Map[String, String]()

    variables.foreach( x => {
      x.split(csvSeparator).zipWithIndex.foreach{ case (item, index) =>
        substitutions += (headers(index) -> item)
      }
      changedTemplates += "\n" + substitutions.foldLeft(template)((a, b) => a.replaceAllLiterally(b._1, b._2))+ "\n"
      changedTemplates += "********************************************************************\n"
    })
    changedTemplates.toList.mkString
  }

  def createFilesAndGetZipPath(templateId: String) = {
    val variables = readVariables(templateId)
    verifyCsvSeparator(variables)
    val headers = variables.head.split(csvSeparator).map(_.trim).map(header => header.replaceAll("\uFEFF", ""))
    val template = readTemplate(templateId)

    var substitutions = collection.mutable.Map[String, String]()

    var filesPath = new ListBuffer[String]()

    variables.foreach(variable => {
        variable.split(csvSeparator).zipWithIndex.foreach{ case (item, index) => {
          substitutions += (headers(index) -> item)
        }
      }
      val emailWithName = substitutions.foldLeft(template)((a, b) => a.replaceAllLiterally(b._1, b._2))
      val emailId = substitutions.getOrElse(headers.head, "")
      val createdEmailPath = s"./target/universal/stage/$emailId.html"
      filesPath += createdEmailPath
      fileManager.writeFile(createdEmailPath, emailWithName)
    })

    val zipPath = s"${templateId}.zip"
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

  private def verifyCsvSeparator(variables: List[String]) = {
    if (!variables.head.contains(csvSeparator)) csvSeparator = ";"
  }

}
