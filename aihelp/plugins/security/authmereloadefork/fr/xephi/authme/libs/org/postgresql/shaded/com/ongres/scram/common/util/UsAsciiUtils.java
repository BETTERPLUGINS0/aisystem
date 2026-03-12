package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util;

public class UsAsciiUtils {
   public static String toPrintable(String value) throws IllegalArgumentException {
      Preconditions.checkNotNull(value, "value");
      char[] printable = new char[value.length()];
      int i = 0;
      char[] var3 = value.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char chr = var3[var5];
         if (chr < 0 || chr >= 127) {
            throw new IllegalArgumentException("value contains character '" + chr + "' which is non US-ASCII");
         }

         if (chr > ' ') {
            printable[i++] = chr;
         }
      }

      return i == value.length() ? value : new String(printable, 0, i);
   }
}
