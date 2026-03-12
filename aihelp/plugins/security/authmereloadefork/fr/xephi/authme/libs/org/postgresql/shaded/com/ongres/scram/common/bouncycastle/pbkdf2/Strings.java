package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class Strings {
   public static byte[] toUTF8ByteArray(char[] string) {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();

      try {
         toUTF8ByteArray(string, bOut);
      } catch (IOException var3) {
         throw new IllegalStateException("cannot encode string to byte array!");
      }

      return bOut.toByteArray();
   }

   public static void toUTF8ByteArray(char[] string, OutputStream sOut) throws IOException {
      char[] c = string;

      for(int i = 0; i < c.length; ++i) {
         char ch = c[i];
         if (ch < 128) {
            sOut.write(ch);
         } else if (ch < 2048) {
            sOut.write(192 | ch >> 6);
            sOut.write(128 | ch & 63);
         } else if (ch >= '\ud800' && ch <= '\udfff') {
            if (i + 1 >= c.length) {
               throw new IllegalStateException("invalid UTF-16 codepoint");
            }

            char W1 = ch;
            ++i;
            ch = c[i];
            if (W1 > '\udbff') {
               throw new IllegalStateException("invalid UTF-16 codepoint");
            }

            int codePoint = ((W1 & 1023) << 10 | ch & 1023) + 65536;
            sOut.write(240 | codePoint >> 18);
            sOut.write(128 | codePoint >> 12 & 63);
            sOut.write(128 | codePoint >> 6 & 63);
            sOut.write(128 | codePoint & 63);
         } else {
            sOut.write(224 | ch >> 12);
            sOut.write(128 | ch >> 6 & 63);
            sOut.write(128 | ch & 63);
         }
      }

   }

   public static String fromByteArray(byte[] bytes) {
      return new String(asCharArray(bytes));
   }

   public static char[] asCharArray(byte[] bytes) {
      char[] chars = new char[bytes.length];

      for(int i = 0; i != chars.length; ++i) {
         chars[i] = (char)(bytes[i] & 255);
      }

      return chars;
   }
}
