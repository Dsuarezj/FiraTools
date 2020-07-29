package controllers

import java.nio.file.Paths
import java.util.UUID

import javax.inject._
import play.api.i18n.I18nSupport
import play.api.libs.Files
import play.api.mvc._
import services.{FileReader, TemplateHandler}

class TemplateController @Inject()(cc: ControllerComponents,
                                   templateHandler: TemplateHandler) extends AbstractController(cc) with I18nSupport {

  def uploadTemplate = Action(parse.multipartFormData) { request =>

    request.body
      .file("template")
      .map { template =>
        val templateId = UUID.randomUUID().toString
        // TODO: modify this to something more scalable like a s3 bucket
        //  and move path to constant o config and use a template id
        template.ref.copyTo(
          Paths.get(s"./$templateId.html"),
          replace = true)

        Ok(views.html.variablesupload(templateId))
      }
      .getOrElse {
        BadRequest("Could not upload file")
      }
  }

  // TODO: refactor to use only one method to upload file, this will be more easy to manage when we have the FE using React
  def uploadVariables(templateId: String): Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { request =>

    request.body
      .file("variables")
      .map { template =>
        template.ref.copyTo(Paths.get(s"./$templateId.csv"), replace = true)
        Ok(views.html.generateFiles(templateId))
      }
      .getOrElse {
        BadRequest("Could not upload file")
      }
  }

  def getView = Action {
    Ok(views.html.templateupload())
  }

  def generateFiles(templateId: String) = Action {
    templateHandler.getFilesUsingVariable(templateId) match {
      case lines => Ok(s"$lines")
      case _ => BadRequest("empty file")
    }
  }

}
