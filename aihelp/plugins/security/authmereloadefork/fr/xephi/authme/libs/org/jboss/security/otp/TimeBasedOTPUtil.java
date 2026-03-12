package fr.xephi.authme.libs.org.jboss.security.otp;

import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeBasedOTPUtil {
   private static long TIME_INTERVAL = 30000L;

   public static boolean validate(String submittedOTP, byte[] secret, int numDigits) throws GeneralSecurityException {
      TimeZone utc = TimeZone.getTimeZone("UTC");
      Calendar currentDateTime = Calendar.getInstance(utc);
      String generatedTOTP = TimeBasedOTP.generateTOTP(new String(secret), numDigits);
      boolean result = generatedTOTP.equals(submittedOTP);
      long timeInMilis = currentDateTime.getTimeInMillis();
      if (!result) {
         timeInMilis -= TIME_INTERVAL;
         generatedTOTP = TimeBasedOTP.generateTOTP(new String(secret), "" + timeInMilis, numDigits);
         result = generatedTOTP.equals(submittedOTP);
      }

      if (!result) {
         timeInMilis += TIME_INTERVAL;
         generatedTOTP = TimeBasedOTP.generateTOTP(new String(secret), "" + timeInMilis, numDigits);
         result = generatedTOTP.equals(submittedOTP);
      }

      return result;
   }

   public static boolean validate256(String submittedOTP, byte[] secret, int numDigits) throws GeneralSecurityException {
      TimeZone utc = TimeZone.getTimeZone("UTC");
      Calendar currentDateTime = Calendar.getInstance(utc);
      String generatedTOTP = TimeBasedOTP.generateTOTP256(new String(secret), numDigits);
      boolean result = generatedTOTP.equals(submittedOTP);
      long timeInMilis;
      if (!result) {
         timeInMilis = currentDateTime.getTimeInMillis();
         timeInMilis -= TIME_INTERVAL;
         generatedTOTP = TimeBasedOTP.generateTOTP256(new String(secret), "" + timeInMilis, numDigits);
         result = generatedTOTP.equals(submittedOTP);
      }

      if (!result) {
         timeInMilis = currentDateTime.getTimeInMillis();
         timeInMilis += TIME_INTERVAL;
         generatedTOTP = TimeBasedOTP.generateTOTP256(new String(secret), "" + timeInMilis, numDigits);
         result = generatedTOTP.equals(submittedOTP);
      }

      return result;
   }

   public static boolean validate512(String submittedOTP, byte[] secret, int numDigits) throws GeneralSecurityException {
      TimeZone utc = TimeZone.getTimeZone("UTC");
      Calendar currentDateTime = Calendar.getInstance(utc);
      String generatedTOTP = TimeBasedOTP.generateTOTP512(new String(secret), numDigits);
      boolean result = generatedTOTP.equals(submittedOTP);
      long timeInMilis;
      if (!result) {
         timeInMilis = currentDateTime.getTimeInMillis();
         timeInMilis -= TIME_INTERVAL;
         generatedTOTP = TimeBasedOTP.generateTOTP512(new String(secret), "" + timeInMilis, numDigits);
         result = generatedTOTP.equals(submittedOTP);
      }

      if (!result) {
         timeInMilis = currentDateTime.getTimeInMillis();
         timeInMilis += TIME_INTERVAL;
         generatedTOTP = TimeBasedOTP.generateTOTP512(new String(secret), "" + timeInMilis, numDigits);
         result = generatedTOTP.equals(submittedOTP);
      }

      return result;
   }
}
