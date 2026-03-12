package fr.xephi.authme.libs.org.mariadb.jdbc.util;

public final class StringUtils {
   private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

   public static String byteArrayToHexString(byte[] bytes) {
      return bytes != null ? getHex(bytes) : "";
   }

   private static String getHex(byte[] raw) {
      StringBuilder hex = new StringBuilder(2 * raw.length);
      byte[] var2 = raw;
      int var3 = raw.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte b = var2[var4];
         hex.append(hexArray[(b & 240) >> 4]).append(hexArray[b & 15]);
      }

      return hex.toString();
   }
}
