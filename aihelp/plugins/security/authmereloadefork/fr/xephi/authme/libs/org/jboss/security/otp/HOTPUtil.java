package fr.xephi.authme.libs.org.jboss.security.otp;

import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.TimeZone;

public class HOTPUtil {
   private static final int MILISECOND_BUFFER = 50;

   public static boolean validate(String submittedOTP, byte[] secret, int timeValueInMins) throws GeneralSecurityException {
      int codeDigits = 6;
      boolean addChecksum = false;
      int truncationOffset = 0;
      TimeZone utc = TimeZone.getTimeZone("UTC");
      Calendar currentDateTime = Calendar.getInstance(utc);
      long timeInMilis = currentDateTime.getTimeInMillis();
      long movingFactor = timeInMilis;
      String otp = HOTP.generateOTP(secret, timeInMilis, codeDigits, addChecksum, truncationOffset);
      if (otp.equals(submittedOTP)) {
         return true;
      } else {
         int endLimit = timeValueInMins * 60 * 1000 + 50;

         for(int i = 1; i < endLimit; ++i) {
            --movingFactor;
            otp = HOTP.generateOTP(secret, movingFactor, codeDigits, addChecksum, truncationOffset);
            if (otp.equals(submittedOTP)) {
               return true;
            }
         }

         return false;
      }
   }
}
