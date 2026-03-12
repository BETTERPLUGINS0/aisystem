package fr.xephi.authme.libs.org.jboss.security;

import java.io.ByteArrayOutputStream;

public class Base64Utils {
   private static final String base64Str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./";
   private static final char[] base64Table = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./".toCharArray();
   public static final String BASE64_ENCODING = "BASE64";
   public static final String BASE16_ENCODING = "HEX";
   public static final char PAD = '_';
   public static final String REGEX = "^_{0,2}[0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./]*$";

   public static String tob64(byte[] buffer) {
      return tob64(buffer, false);
   }

   public static String tob64(byte[] buffer, boolean usePadding) {
      int len = buffer.length;
      int pos = len % 3;
      byte b0 = 0;
      byte b1 = 0;
      byte b2 = false;
      StringBuffer sb = new StringBuffer();
      int i = 0;
      if (usePadding) {
         for(i = pos; i != 0; i = (i + 1) % 3) {
            sb.append('_');
         }

         i = 0;
      }

      int c;
      byte b2;
      switch(pos) {
      case 2:
         b1 = buffer[i++];
         c = (b0 & 3) << 4 | (b1 & 240) >>> 4;
         sb.append(base64Table[c]);
      case 1:
         b2 = buffer[i++];
         c = (b1 & 15) << 2 | (b2 & 192) >>> 6;
         sb.append(base64Table[c]);
         c = b2 & 63;
         sb.append(base64Table[c]);
      }

      while(pos < len) {
         byte b0 = buffer[pos++];
         b1 = buffer[pos++];
         b2 = buffer[pos++];
         c = (b0 & 252) >>> 2;
         sb.append(base64Table[c]);
         c = (b0 & 3) << 4 | (b1 & 240) >>> 4;
         sb.append(base64Table[c]);
         c = (b1 & 15) << 2 | (b2 & 192) >>> 6;
         sb.append(base64Table[c]);
         c = b2 & 63;
         sb.append(base64Table[c]);
      }

      return sb.toString();
   }

   public static byte[] fromb64(String str) throws NumberFormatException {
      if (str.length() == 0) {
         return new byte[0];
      } else {
         while(str.length() % 4 != 0) {
            str = '_' + str;
         }

         if (!str.matches("^_{0,2}[0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./]*$")) {
            throw PicketBoxMessages.MESSAGES.invalidBase64String(str);
         } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(str.length() * 3 / 4);
            int i = 0;

            int pos2;
            int pos3;
            for(int n = str.length(); i < n; bos.write((pos2 & 3) << 6 | pos3)) {
               int pos0 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./".indexOf(str.charAt(i++));
               int pos1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./".indexOf(str.charAt(i++));
               pos2 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./".indexOf(str.charAt(i++));
               pos3 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz./".indexOf(str.charAt(i++));
               if (pos0 > -1) {
                  bos.write((pos1 & 48) >>> 4 | pos0 << 2);
               }

               if (pos1 > -1) {
                  bos.write((pos2 & 60) >>> 2 | (pos1 & 15) << 4);
               }
            }

            return bos.toByteArray();
         }
      }
   }
}
