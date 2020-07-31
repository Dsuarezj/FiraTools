package controllers

import javax.inject._
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.WhatsappHandler

import scala.concurrent.ExecutionContext

class WhatsappController @Inject()(cc: ControllerComponents,
                                   whatsappHandler: WhatsappHandler)(implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport {

  def getView = Action {
    Ok(views.html.whatsappform())
  }

  def generateScript() = Action(parse.multipartFormData) { request =>

    val messageTemplate = request.body.dataParts.head._2.head

    request.body
      .file("userInfo")
      .map { template =>
        Ok(whatsappHandler.generateScript(template.ref, messageTemplate))
      }
      .getOrElse {
        BadRequest("Could not upload file")
      }
  }
}
