package utils

import scala.util.matching.Regex

class PhoneNumberUtil {
  def getEcuadorianNumber(userPhoneNumber: String): String = {
    val ecuadorianCountryCode = 593
    val cleanNumber = getCleanNumberAndRemoveZero(userPhoneNumber)
    addCountryCode(cleanNumber, ecuadorianCountryCode)
  }

  def getCleanNumberAndRemoveZero(userPhoneNumber: String): String = {
    val getOnlyNumbers = userPhoneNumber.replaceAll("[^\\d]", "")
    new Regex("^0").replaceFirstIn(getOnlyNumbers, "")
  }

  def addCountryCode(userPhoneNumber: String, countryCode: Int): String = {
    val countryCodePattern = new Regex(s"^$countryCode")
    countryCodePattern.findFirstIn(userPhoneNumber) match {
      case Some(_) => userPhoneNumber
      case _ => s"$countryCode$userPhoneNumber"
    }
  }

}
