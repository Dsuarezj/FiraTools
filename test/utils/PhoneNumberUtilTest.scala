package utils

import org.scalatestplus.play.PlaySpec

import scala.util.Random

class PhoneNumberUtilTest extends PlaySpec {
  "Add country code" must {
    "return the same number if starts with the same country code" in {
      val countryCode = Random.nextInt(999)
      val number = Random.nextInt(999999999)
      val userPhoneNumber = s"$countryCode$number"
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.addCountryCode(userPhoneNumber, countryCode)

      formattedNumber mustBe userPhoneNumber
    }

    "return the number with the country code if not start with it" in {
      val countryCode = Random.nextInt(999)
      val number = Random.nextInt(999999999)
      val userPhoneNumber = s"$number"
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.addCountryCode(userPhoneNumber, countryCode)

      formattedNumber mustBe s"$countryCode$number"
    }
    "return the number with the country code if not start with it but is part of number" in {
      val countryCode = Random.nextInt(999)
      val number = Random.nextInt(999999999)
      val userPhoneNumber = s"$number$countryCode"
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.addCountryCode(userPhoneNumber, countryCode)

      formattedNumber mustBe s"$countryCode$userPhoneNumber"
    }
  }

  "Get number with out initial zero" must {
    "remove zero if number start with 0" in {
      val number = Random.nextInt(999999999)
      val userPhoneNumber = s"0$number"
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.getCleanNumberAndRemoveZero(userPhoneNumber)

      formattedNumber mustBe s"$number"
    }
    "remove non digit characters from number" in {
      val number = Random.nextInt(999999999)
      val userPhoneNumber = s"+$number -$number."
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.getCleanNumberAndRemoveZero(userPhoneNumber)

      formattedNumber mustBe s"$number$number"
    }
    "maintain the zero if they are not at the begin" in {
      val number = Random.nextInt(9999)
      val userPhoneNumber = number + "0"
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.getCleanNumberAndRemoveZero(userPhoneNumber)

      formattedNumber mustBe s"$userPhoneNumber"
    }
  }

  "Get ecuadorian number" must {
    "remove initial zero and add country code" in {
      val number = Random.nextInt(999999999)
      val userPhoneNumber = s"0$number"
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.getEcuadorianNumber(userPhoneNumber)

      formattedNumber mustBe s"593$number"
    }
    "remove plus and keep country code" in {
      val number = Random.nextInt(999999999)
      val userPhoneNumber = s"+593$number"
      val phoneNumberUtil = new PhoneNumberUtil()

      val formattedNumber = phoneNumberUtil.getEcuadorianNumber(userPhoneNumber)

      formattedNumber mustBe s"593$number"
    }
  }

}
