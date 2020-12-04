import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent._

class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful({
      print("A client error occurred: " + message)
      Status(statusCode)("Lo sentimos! un error... reportalo! 🔥")
    })
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful({
      print("A client error occurred: " + request.uri + request.method)
      InternalServerError("Lo sentimos! un error ... reportalo! 🔥")
    })
  }
}
