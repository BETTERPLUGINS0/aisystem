package fr.xephi.authme.libs.org.apache.commons.mail;

import fr.xephi.authme.libs.org.apache.commons.mail.util.MimeMessageUtils;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

final class EmailUtils {
   private static final Random RANDOM = new Random();
   private static final String US_ASCII = "US-ASCII";
   private static final int RADIX = 16;
   private static final char ESCAPE_CHAR = '%';
   private static final BitSet SAFE_URL = new BitSet(256);

   private EmailUtils() {
   }

   static boolean isEmpty(String str) {
      return str == null || str.length() == 0;
   }

   static boolean isNotEmpty(String str) {
      return str != null && str.length() > 0;
   }

   static void notNull(Object object, String message) {
      if (object == null) {
         throw new IllegalArgumentException(message);
      }
   }

   static String randomAlphabetic(int count) {
      return random(count, 0, 0, true, false, (char[])null, RANDOM);
   }

   private static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
      if (count == 0) {
         return "";
      } else if (count < 0) {
         throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
      } else {
         if (start == 0 && end == 0) {
            end = 123;
            start = 32;
            if (!letters && !numbers) {
               start = 0;
               end = Integer.MAX_VALUE;
            }
         }

         StringBuffer buffer = new StringBuffer();
         int gap = end - start;

         while(true) {
            while(count-- != 0) {
               char ch;
               if (chars == null) {
                  ch = (char)(random.nextInt(gap) + start);
               } else {
                  ch = chars[random.nextInt(gap) + start];
               }

               if (letters && numbers && Character.isLetterOrDigit(ch) || letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
                  buffer.append(ch);
               } else {
                  ++count;
               }
            }

            return buffer.toString();
         }
      }
   }

   static String replaceEndOfLineCharactersWithSpaces(String input) {
      return input == null ? null : input.replace('\n', ' ').replace('\r', ' ');
   }

   static String encodeUrl(String input) throws UnsupportedEncodingException {
      if (input == null) {
         return null;
      } else {
         StringBuilder builder = new StringBuilder();
         byte[] var2 = input.getBytes("US-ASCII");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte c = var2[var4];
            int b = c;
            if (c < 0) {
               b = 256 + c;
            }

            if (SAFE_URL.get(b)) {
               builder.append((char)b);
            } else {
               builder.append('%');
               char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 15, 16));
               char hex2 = Character.toUpperCase(Character.forDigit(b & 15, 16));
               builder.append(hex1);
               builder.append(hex2);
            }
         }

         return builder.toString();
      }
   }

   static void writeMimeMessage(File resultFile, MimeMessage mimeMessage) throws IOException, MessagingException {
      MimeMessageUtils.writeMimeMessage(mimeMessage, resultFile);
   }

   static {
      int i;
      for(i = 97; i <= 122; ++i) {
         SAFE_URL.set(i);
      }

      for(i = 65; i <= 90; ++i) {
         SAFE_URL.set(i);
      }

      for(i = 48; i <= 57; ++i) {
         SAFE_URL.set(i);
      }

      SAFE_URL.set(45);
      SAFE_URL.set(95);
      SAFE_URL.set(46);
      SAFE_URL.set(42);
      SAFE_URL.set(43);
      SAFE_URL.set(36);
      SAFE_URL.set(33);
      SAFE_URL.set(39);
      SAFE_URL.set(40);
      SAFE_URL.set(41);
      SAFE_URL.set(44);
      SAFE_URL.set(64);
   }
}
