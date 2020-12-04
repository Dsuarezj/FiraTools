import java.io.{PrintWriter, StringWriter}

import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent._
import scala.util.Try

class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful({
      print("A client error occurred: %s | Request uri: %s | Method: %s".format(message, request.uri, request.method))
      Status(statusCode)("Lo sentimos! un error... reportalo! 🔥")
    })
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful({
      val sw = new StringWriter
      Try{
        exception.printStackTrace(new PrintWriter(sw))
      }
      print(
        "A server error occurred: %s | Message: %s | Request uri: %s | Method: %s"
        .format(sw.toString, exception.toString, request.uri, request.method)
      )
      InternalServerError("Lo sentimos! un error ... reportalo! 🔥")
    })
  }
}
