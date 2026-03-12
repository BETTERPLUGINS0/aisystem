package fr.xephi.authme.security;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {
   private HashUtils() {
   }

   public static String sha1(String message) {
      return hash(message, MessageDigestAlgorithm.SHA1);
   }

   public static String sha256(String message) {
      return hash(message, MessageDigestAlgorithm.SHA256);
   }

   public static String sha512(String message) {
      return hash(message, MessageDigestAlgorithm.SHA512);
   }

   public static String md5(String message) {
      return hash(message, MessageDigestAlgorithm.MD5);
   }

   public static MessageDigest getDigest(MessageDigestAlgorithm algorithm) {
      try {
         return MessageDigest.getInstance(algorithm.getKey());
      } catch (NoSuchAlgorithmException var2) {
         throw new UnsupportedOperationException("Your system seems not to support the hash algorithm '" + algorithm.getKey() + "'");
      }
   }

   public static boolean isValidBcryptHash(String hash) {
      return hash.length() == 60 && hash.substring(0, 2).equals("$2");
   }

   public static boolean isEqual(String string1, String string2) {
      return MessageDigest.isEqual(string1.getBytes(StandardCharsets.UTF_8), string2.getBytes(StandardCharsets.UTF_8));
   }

   public static String hash(String message, MessageDigest algorithm) {
      algorithm.reset();
      algorithm.update(message.getBytes());
      byte[] digest = algorithm.digest();
      return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
   }

   private static String hash(String message, MessageDigestAlgorithm algorithm) {
      return hash(message, getDigest(algorithm));
   }
}
