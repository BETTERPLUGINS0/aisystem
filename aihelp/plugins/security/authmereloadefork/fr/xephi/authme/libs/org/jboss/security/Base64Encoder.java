package fr.xephi.authme.libs.org.jboss.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public final class Base64Encoder {
   private static final int BUFFER_SIZE = 1024;
   private static final byte[] encoding = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47, 61};

   public static void encode(InputStream in, OutputStream out) throws IOException {
      process(in, out);
   }

   public static void encode(byte[] input, OutputStream out) throws IOException {
      ByteArrayInputStream in = new ByteArrayInputStream(input);
      process(in, out);
   }

   public static String encode(String input) throws IOException {
      byte[] bytes = input.getBytes("ISO-8859-1");
      return encode(bytes);
   }

   public static String encode(byte[] bytes) throws IOException {
      ByteArrayInputStream in = new ByteArrayInputStream(bytes);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      process(in, out);
      return out.toString("ISO-8859-1");
   }

   public static void main(String[] args) throws Exception {
      if (args.length == 1) {
         System.out.println("[" + encode(args[0]) + "]");
      } else if (args.length == 2) {
         byte[] hash = MessageDigest.getInstance(args[1]).digest(args[0].getBytes());
         System.out.println("[" + encode(hash) + "]");
      } else {
         System.out.println(PicketBoxMessages.MESSAGES.base64EncoderMessage());
      }

   }

   private static int get1(byte[] buf, int off) {
      return (buf[off] & 252) >> 2;
   }

   private static int get2(byte[] buf, int off) {
      return (buf[off] & 3) << 4 | (buf[off + 1] & 240) >>> 4;
   }

   private static int get3(byte[] buf, int off) {
      return (buf[off + 1] & 15) << 2 | (buf[off + 2] & 192) >>> 6;
   }

   private static int get4(byte[] buf, int off) {
      return buf[off + 2] & 63;
   }

   private static void process(InputStream in, OutputStream out) throws IOException {
      byte[] buffer = new byte[1024];
      int got = true;
      int off = 0;
      int count = 0;

      while(true) {
         int got;
         while((got = in.read(buffer, off, 1024 - off)) > 0) {
            if (got >= 3) {
               got += off;

               int i;
               for(off = 0; off + 3 <= got; off += 3) {
                  i = get1(buffer, off);
                  int c2 = get2(buffer, off);
                  int c3 = get3(buffer, off);
                  int c4 = get4(buffer, off);
                  switch(count) {
                  case 73:
                     out.write(encoding[i]);
                     out.write(encoding[c2]);
                     out.write(encoding[c3]);
                     out.write(10);
                     out.write(encoding[c4]);
                     count = 1;
                     break;
                  case 74:
                     out.write(encoding[i]);
                     out.write(encoding[c2]);
                     out.write(10);
                     out.write(encoding[c3]);
                     out.write(encoding[c4]);
                     count = 2;
                     break;
                  case 75:
                     out.write(encoding[i]);
                     out.write(10);
                     out.write(encoding[c2]);
                     out.write(encoding[c3]);
                     out.write(encoding[c4]);
                     count = 3;
                     break;
                  case 76:
                     out.write(10);
                     out.write(encoding[i]);
                     out.write(encoding[c2]);
                     out.write(encoding[c3]);
                     out.write(encoding[c4]);
                     count = 4;
                     break;
                  default:
                     out.write(encoding[i]);
                     out.write(encoding[c2]);
                     out.write(encoding[c3]);
                     out.write(encoding[c4]);
                     count += 4;
                  }
               }

               for(i = 0; i < 3; ++i) {
                  buffer[i] = i < got - off ? buffer[off + i] : 0;
               }

               off = got - off;
            } else {
               off += got;
            }
         }

         switch(off) {
         case 1:
            out.write(encoding[get1(buffer, 0)]);
            out.write(encoding[get2(buffer, 0)]);
            out.write(61);
            out.write(61);
            break;
         case 2:
            out.write(encoding[get1(buffer, 0)]);
            out.write(encoding[get2(buffer, 0)]);
            out.write(encoding[get3(buffer, 0)]);
            out.write(61);
         }

         return;
      }
   }
}
