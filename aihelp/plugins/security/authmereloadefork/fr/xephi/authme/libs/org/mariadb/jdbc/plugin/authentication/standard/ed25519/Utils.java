package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.ed25519;

public class Utils {
   public static int equal(int b, int c) {
      int result = 0;
      int xor = b ^ c;

      for(int i = 0; i < 8; ++i) {
         result |= xor >> i;
      }

      return (result ^ 1) & 1;
   }

   public static int equal(byte[] b, byte[] c) {
      int result = 0;

      for(int i = 0; i < 32; ++i) {
         result |= b[i] ^ c[i];
      }

      return equal(result, 0);
   }

   public static int negative(int b) {
      return b >> 8 & 1;
   }

   public static int bit(byte[] h, int i) {
      return h[i >> 3] >> (i & 7) & 1;
   }

   public static byte[] hexToBytes(String s) {
      int len = s.length();
      byte[] data = new byte[len / 2];

      for(int i = 0; i < len; i += 2) {
         data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
      }

      return data;
   }

   public static String bytesToHex(byte[] raw) {
      if (raw == null) {
         return null;
      } else {
         StringBuilder hex = new StringBuilder(2 * raw.length);
         byte[] var2 = raw;
         int var3 = raw.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            hex.append(Character.forDigit((b & 240) >> 4, 16)).append(Character.forDigit(b & 15, 16));
         }

         return hex.toString();
      }
   }
}
