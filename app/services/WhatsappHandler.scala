package services

import java.io.File

import Utils.FileManager
import javax.inject.Inject

import scala.collection.mutable.ListBuffer


class WhatsappHandler @Inject()(fileManager: FileManager) {
  def generateScript(file: File, message: String = ""): String = {
    fileManager.readCsvFile(file) match {
      case Some(lines) => getScript(lines, message)
      case _ => ""
    }
  }

  private def getScript(userInfo: List[String], message: String = "") = {
    val whatsappLinks = createLinks(userInfo, message)

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
        |You can find the result of the script on the console you open
        |
        |
        |****************** Script ******************
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
        |setTimeout(function(){
        |  eventFire(win.document.querySelector('span[data-icon="send"]'), 'click');
        |}, 4000);
        |let i = 1;
        |let int = setInterval(() => {
        |  console.log("Enviando a....." + urlList[i])
        |  win.location = urlList[i];
        |  setTimeout(function(){
        |    eventFire(win.document.querySelector('span[data-icon="send"]'), 'click');
        |  }, 6000);
        |  if (i++ >= urlList.length) clearInterval(int)
        |}, 10000)
        |
        |
        |
        |
        |
        |""".stripMargin
  }

  private def createLinks(userInfo: List[String], message: String) = {
    val messagesWithUserInfo = new ListBuffer[(String, String)]()
    val whatsappLinks = new ListBuffer[String]()
    val substitutions = collection.mutable.Map[String, String]()
    val PHONE_NUMBER = "PHONE_NUMBER"
    val acceptedKeys = List(PHONE_NUMBER, "NAME", "FREE_TEXT")
    userInfo.foreach(userInfo => {
      userInfo.split(",").zipWithIndex.foreach {
        case (item, index) => substitutions += (acceptedKeys(index) -> item)
      }
      val messageToBeSent = substitutions.foldLeft(message)((a, b) => a.replaceAllLiterally(b._1, b._2)) + "\", "
      messagesWithUserInfo.append((substitutions.getOrElse(PHONE_NUMBER, ""), messageToBeSent))
    })
    messagesWithUserInfo.toList.map(message => {
      whatsappLinks += "\"" + s"https://web.whatsapp.com/send?phone=${message._1}&text=${message._2}"
    })
    whatsappLinks.toList
  }
}
