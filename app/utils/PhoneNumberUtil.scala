package utils

import scala.util.matching.Regex

class PhoneNumberUtil {
  def getEcuadorianNumber(userPhoneNumber: String): String = {
    val ecuadorianCountryCode = 593
    val cleanNumber = getNumberWithOutInitialZeroOrPlus(userPhoneNumber)
    addCountryCode(cleanNumber, ecuadorianCountryCode)
  }

  def getNumberWithOutInitialZeroOrPlus(userPhoneNumber: String): String = {
    val numberWithOutPlus = new Regex("^\\+").replaceFirstIn(userPhoneNumber, "")
    new Regex("^0").replaceFirstIn(numberWithOutPlus, "")
  }

  def addCountryCode(userPhoneNumber: String, countryCode: Int): String = {
    val countryCodePattern = new Regex(s"^$countryCode")
      countryCodePattern.findFirstIn(userPhoneNumber) match {
      case Some(_) => userPhoneNumber
      case _ => s"$countryCode$userPhoneNumber"
    }
  }

}
