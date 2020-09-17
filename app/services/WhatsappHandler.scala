package services

import java.io.File

import utils.{FileManager, PhoneNumberUtil}
import javax.inject.Inject

import scala.collection.mutable.ListBuffer


class WhatsappHandler @Inject()(fileManager: FileManager, phoneNumberUtil: PhoneNumberUtil) {
  def generateScript(file: File, message: String = "", hasToFixNumbers: String = ""): String = {
    fileManager.readCsvFile(file) match {
      case Some(lines) => getScript(lines, message, hasToFixNumbers)
      case _ => ""
    }
  }

  private def getScript(messagesData: List[String], messageTemplate: String = "", hasToFixNumbers: String = ""): String = {
    val whatsappLinks = createLinks(messagesData, messageTemplate, hasToFixNumbers)

    """
        |Your Whatsapp script is ready! 🥳🥂👌🏽
        |
        |
        |To use it follow these steps:
        |
        |1. Open https://web.whatsapp.com/
        |2. Login with the phone number that you want to send message
        |3. In whatsapp page, open the browser console (For Chrome Mac: Command+Option+J / Windows: Control+Shift+J)
        |4. Copy and paste the follow script on the console
        |5. Press Enter and let the magic do the work for you 🎩🐇
        |
        |Note: Whatsapp's tabs will open and start sending message, you don't have to click anything or press enter, just seat and watch.
        |You can find the result of the script on the console you open.
        |
        |The script will take random time before sending the message to avoid account lockout from whatsapp.
        |
        |
        |****************** Script ******************
        |Copy from here:
        |
        |let eventFire = (MyElement, ElementType) => {
        |    var MyEvent = document.createEvent("MouseEvents");
        |    MyEvent.initMouseEvent
        |     (ElementType, true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
        |    MyElement.dispatchEvent(MyEvent);
        |};
        |
        |
        |let urlList = [""".stripMargin + whatsappLinks.mkString + """]
        |
        |let win = window.open(urlList[0], "_blank")
        |console.log("Enviando a....." + urlList[0])
        |setTimeout(function(){
        |  eventFire(win.document.querySelector('span[data-icon="send"]'), 'click');
        |}, 10000);
        |let i = 1;
        |let int = setInterval(() => {
        |  console.log("Enviando a....." + urlList[i])
        |  win.location = urlList[i];
        |  setTimeout(function(){
        |    eventFire(win.document.querySelector('span[data-icon="send"]'), 'click');
        |  }, 10000 + Math.round(Math.random() * 10000));
        |  if (i++ >= urlList.length) clearInterval(int)
        |}, 22000 + Math.round(Math.random() * 20000))
        |
        |
        |
        |
        |
        |""".stripMargin
  }

  private def createLinks(messagesData: List[String], messageTemplate: String, hasToFixNumbers: String): List[String] = {
    val messagesBuffer = new ListBuffer[(String, String)]()
    val whatsappLinks = new ListBuffer[String]()
    val substitutions = collection.mutable.Map[String, String]()
    val PHONE_NUMBER = "PHONE_NUMBER"
    val acceptedKeys = List(PHONE_NUMBER, "NAME", "ANY_TEXT")
    messagesData.foreach(messageInfo => {
      messageInfo.split(",").zipWithIndex.foreach {
        case (item, index) => substitutions += (acceptedKeys(index) -> item)
      }
      val messageToBeSent = substitutions.foldLeft(messageTemplate)((a, b) => a.replaceAllLiterally(b._1, b._2)) + "\", "
      val userPhoneNumber = substitutions.getOrElse(PHONE_NUMBER, "")
      if (hasToFixNumbers.contains("on")){
        messagesBuffer.append((phoneNumberUtil.getEcuadorianNumber(userPhoneNumber),messageToBeSent))
      } else {
        messagesBuffer.append((userPhoneNumber, messageToBeSent))
      }
    })
    messagesBuffer.toList.map(message => {
      whatsappLinks += "\"" + s"https://web.whatsapp.com/send?phone=${message._1}&text=${message._2}"
    })
    whatsappLinks.toList
  }
}
