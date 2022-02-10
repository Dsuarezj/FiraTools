package models
import play.api.libs.json.Json
import play.api.libs.json._

case class WhatsappMessage(phone: String, message: String)

object WhatsappMessage {

  implicit object WhatsappMessageFormat extends Format[WhatsappMessage] {

    // convert from Tweet object to JSON (serializing to JSON)
    def writes(whatsappMessage: WhatsappMessage): JsValue = {
      val tweetSeq = Seq(
        "phone" -> JsString(whatsappMessage.phone),
        "message" -> JsString(whatsappMessage.message)
      )
      JsObject(tweetSeq)
    }

    def reads(json: JsValue): JsResult[WhatsappMessage] = {
      JsSuccess(WhatsappMessage("", ""))
    }

  }

}