package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.Base64Utils;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PBEUtils {
   public static byte[] encode(byte[] secret, String cipherAlgorithm, SecretKey cipherKey, PBEParameterSpec cipherSpec) throws Exception {
      Cipher cipher = Cipher.getInstance(cipherAlgorithm);
      cipher.init(1, cipherKey, cipherSpec);
      byte[] encoding = cipher.doFinal(secret);
      return encoding;
   }

   public static String encode64(byte[] secret, String cipherAlgorithm, SecretKey cipherKey, PBEParameterSpec cipherSpec) throws Exception {
      byte[] encoding = encode(secret, cipherAlgorithm, cipherKey, cipherSpec);
      String b64 = Base64Utils.tob64(encoding);
      return b64;
   }

   public static byte[] decode(byte[] secret, String cipherAlgorithm, SecretKey cipherKey, PBEParameterSpec cipherSpec) throws Exception {
      Cipher cipher = Cipher.getInstance(cipherAlgorithm);
      cipher.init(2, cipherKey, cipherSpec);
      byte[] decode = cipher.doFinal(secret);
      return decode;
   }

   public static String decode64(String secret, String cipherAlgorithm, SecretKey cipherKey, PBEParameterSpec cipherSpec) throws Exception {
      byte[] encoding;
      try {
         encoding = Base64Utils.fromb64(secret);
      } catch (IllegalArgumentException var6) {
         encoding = Base64Utils.fromb64("0" + secret);
         PicketBoxLogger.LOGGER.wrongBase64StringUsed("0" + secret);
      }

      byte[] decode = decode(encoding, cipherAlgorithm, cipherKey, cipherSpec);
      return new String(decode, "UTF-8");
   }

   public static void main(String[] args) throws Exception {
      if (args.length != 4) {
         System.err.println(PicketBoxMessages.MESSAGES.pbeUtilsMessage());
      }

      byte[] salt = args[0].substring(0, 8).getBytes();
      int count = Integer.parseInt(args[1]);
      char[] password = args[2].toCharArray();
      byte[] passwordToEncode = args[3].getBytes("UTF-8");
      PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
      PBEKeySpec keySpec = new PBEKeySpec(password);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEwithMD5andDES");
      SecretKey cipherKey = factory.generateSecret(keySpec);
      String encodedPassword = encode64(passwordToEncode, "PBEwithMD5andDES", cipherKey, cipherSpec);
      System.err.println("Encoded password: " + encodedPassword);
   }
}
