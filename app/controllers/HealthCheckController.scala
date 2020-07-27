package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class HealthCheckController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def state = Action {
    Ok("The service is up and running! 🥂")
  }

}
