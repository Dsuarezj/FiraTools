package services

import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import utils.FileManager


class TemplateHandlerTest extends PlaySpec with MockitoSugar {
  "getCsvSeparator" should {
    "extract the first non alphanumeric number of the header" when {
     "given a header with coma return a coma" in {
       val fileManager = new FileManager
       val templateHandler = new TemplateHandler(fileManager)

       val csvSeparator = templateHandler.getCsvSeparator("ARCHIVO,LOGOSALON,WEBSALON,VISUAL")

       csvSeparator mustBe ","
      }
      "given a header with coma and Unicode character return a coma" in {
       val fileManager = new FileManager
       val templateHandler = new TemplateHandler(fileManager)

       val csvSeparator = templateHandler.getCsvSeparator("\uFEFFARCHIVO,LOGOSALON,WEBSALON,VISUAL")

       csvSeparator mustBe ","
      }
    }
  }

}
