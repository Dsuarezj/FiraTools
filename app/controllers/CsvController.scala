package controllers

import javax.inject._
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.CsvReader

class CsvController @Inject()(cc: ControllerComponents,
                              csvReader: CsvReader) extends AbstractController(cc) with I18nSupport {

  def getCsvTemplate: Action[AnyContent] = Action {
    // TODO: move this to global file with constants or config
    val path = "./public/templates/csvTemplate.csv"
    csvReader.readFromFile(path) match {
      case Some(lines) => Ok(s"all good: ${lines}")
      case _ => BadRequest("couldn't read the file")
    }
  }
}
