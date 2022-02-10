package controllers

import javax.inject._
import play.api.i18n.I18nSupport
import play.api.libs.Files
import play.api.libs.json.Json
import play.api.mvc._
import services.WhatsappHandler

import scala.concurrent.ExecutionContext

class WhatsappController @Inject()(cc: ControllerComponents,
                                   whatsappHandler: WhatsappHandler)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  def getView = Action {
    Ok(views.html.whatsappform())
  }

  def generateScript() = Action(parse.multipartFormData) { request =>

    val (messageTemplate: String, hasToFixNumbers: String, csvFile) = readForm(request)
    csvFile
      .map { csv =>
        Ok(whatsappHandler.generateScript(csv.ref, messageTemplate, hasToFixNumbers))
      }
      .getOrElse {
        BadRequest("Could not upload file")
      }
  }

  def generateJson() = Action(parse.multipartFormData) { request =>
    val (messageTemplate: String, hasToFixNumbers: String, csvFile) = readForm(request)
    csvFile
      .map { csvValues =>
        Ok(Json.toJson(whatsappHandler.generateJson(csvValues.ref, messageTemplate, hasToFixNumbers)))
      }
      .getOrElse {
        BadRequest("Request not valid")
      }
  }


  private def readForm(request: Request[MultipartFormData[Files.TemporaryFile]]) = {
    val messageTemplate = request.body.dataParts.getOrElse("messageTemplate", Seq("")).head
    val hasToFixNumbers = request.body.dataParts.getOrElse("fixNumber", Seq("")).head
    val csvFile = request.body.file("messageValues")
    (messageTemplate, hasToFixNumbers, csvFile)
  }
}
