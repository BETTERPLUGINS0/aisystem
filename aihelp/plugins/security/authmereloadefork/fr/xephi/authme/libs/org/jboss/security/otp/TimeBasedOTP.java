package fr.xephi.authme.libs.org.jboss.security.otp;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TimeBasedOTP {
   public static final String HMAC_SHA1 = "HmacSHA1";
   public static final String HMAC_SHA256 = "HmacSHA256";
   public static final String HMAC_SHA512 = "HmacSHA512";
   private static final int[] DIGITS_POWER = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
   private static int TIME_SLICE_X = 30000;
   private static int TIME_ZERO = 0;

   public static String generateTOTP(String key, int returnDigits) throws GeneralSecurityException {
      TimeZone utc = TimeZone.getTimeZone("UTC");
      Calendar currentDateTime = Calendar.getInstance(utc);
      long timeInMilis = currentDateTime.getTimeInMillis();
      String steps = "0";
      long T = (timeInMilis - (long)TIME_ZERO) / (long)TIME_SLICE_X;

      for(steps = Long.toHexString(T).toUpperCase(Locale.ENGLISH); steps.length() < 16; steps = "0" + steps) {
      }

      return generateTOTP(key, steps, returnDigits);
   }

   public static String generateTOTP256(String key, int returnDigits) throws GeneralSecurityException {
      TimeZone utc = TimeZone.getTimeZone("UTC");
      Calendar currentDateTime = Calendar.getInstance(utc);
      long timeInMilis = currentDateTime.getTimeInMillis();
      String steps = "0";
      long T = (timeInMilis - (long)TIME_ZERO) / (long)TIME_SLICE_X;

      for(steps = Long.toHexString(T).toUpperCase(Locale.ENGLISH); steps.length() < 16; steps = "0" + steps) {
      }

      return generateTOTP256(key, steps, returnDigits);
   }

   public static String generateTOTP512(String key, int returnDigits) throws GeneralSecurityException {
      TimeZone utc = TimeZone.getTimeZone("UTC");
      Calendar currentDateTime = Calendar.getInstance(utc);
      long timeInMilis = currentDateTime.getTimeInMillis();
      String steps = "0";
      long T = (timeInMilis - (long)TIME_ZERO) / (long)TIME_SLICE_X;

      for(steps = Long.toHexString(T).toUpperCase(Locale.ENGLISH); steps.length() < 16; steps = "0" + steps) {
      }

      return generateTOTP512(key, steps, returnDigits);
   }

   public static String generateTOTP(String key, String time, int returnDigits) throws GeneralSecurityException {
      return generateTOTP(key, time, returnDigits, "HmacSHA1");
   }

   public static String generateTOTP256(String key, String time, int returnDigits) throws GeneralSecurityException {
      return generateTOTP(key, time, returnDigits, "HmacSHA256");
   }

   public static String generateTOTP512(String key, String time, int returnDigits) throws GeneralSecurityException {
      return generateTOTP(key, time, returnDigits, "HmacSHA512");
   }

   public static String generateTOTP(String key, String time, int returnDigits, String crypto) throws GeneralSecurityException {
      String result;
      for(result = null; time.length() < 16; time = "0" + time) {
      }

      byte[] msg = hexStr2Bytes(time);
      byte[] k = hexStr2Bytes(key);
      byte[] hash = hmac_sha1(crypto, k, msg);
      int offset = hash[hash.length - 1] & 15;
      int binary = (hash[offset] & 127) << 24 | (hash[offset + 1] & 255) << 16 | (hash[offset + 2] & 255) << 8 | hash[offset + 3] & 255;
      int otp = binary % DIGITS_POWER[returnDigits];

      for(result = Integer.toString(otp); result.length() < returnDigits; result = "0" + result) {
      }

      return result;
   }

   private static byte[] hmac_sha1(String crypto, byte[] keyBytes, byte[] text) throws GeneralSecurityException {
      Mac hmac = Mac.getInstance(crypto);
      SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
      hmac.init(macKey);
      return hmac.doFinal(text);
   }

   private static byte[] hexStr2Bytes(String hex) {
      byte[] bArray = (new BigInteger("10" + hex, 16)).toByteArray();
      byte[] ret = new byte[bArray.length - 1];

      for(int i = 0; i < ret.length; ++i) {
         ret[i] = bArray[i + 1];
      }

      return ret;
   }
}
