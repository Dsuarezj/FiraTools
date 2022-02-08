package models
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes}

case class WhatsappMessage(phone: String, message: String)

object WhatsappMessage {

  implicit val whatsappMessageWrites: Writes[WhatsappMessage] = (
    (JsPath \ "phone").writeNullable[String] and
      (JsPath \ "message").writeNullable[String]
    )(unlift(WhatsappMessage.unapply))
}