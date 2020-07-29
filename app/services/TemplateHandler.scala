package services

import javax.inject.Inject

import scala.collection.mutable.ListBuffer

class TemplateHandler @Inject()(fileReader: FileReader) {

  // TODO: depending of the deploy used different path, the one with target is to work on heroku
  def getFilesUsingVariable(templateId: String): String = {
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

  def readTemplate(templateId: String): String = {
    val path = s"./target/universal/stage/share/$templateId.html"
    fileReader.readTextFile(path) match {
      case Some(lines) => lines
      case _ => fileReader.readTextFile(s"./share/$templateId.html") match {
        case Some(lines) => lines
        case _ => ""
      }
    }
  }

  def readVariables(templateId: String): List[String] = {
    val path = s"./target/universal/stage/$templateId.csv"
    fileReader.readCsvFile(path) match {
      case Some(lines) => lines
      case _ => fileReader.readCsvFile(s"./$templateId.csv") match {
        case Some(lines) => lines
        case _ => List()
      }
    }
  }

}
