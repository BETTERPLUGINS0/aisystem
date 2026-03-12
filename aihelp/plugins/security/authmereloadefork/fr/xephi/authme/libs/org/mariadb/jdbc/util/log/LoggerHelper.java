package fr.xephi.authme.libs.org.mariadb.jdbc.util.log;

public final class LoggerHelper {
   private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

   public static String hex(byte[] bytes, int offset, int dataLength) {
      return hex(bytes, offset, dataLength, Integer.MAX_VALUE);
   }

   public static String hex(byte[] bytes, int offset, int dataLength, int trunkLength) {
      if (bytes != null && bytes.length != 0) {
         char[] hexaValue = new char[16];
         hexaValue[8] = ' ';
         int pos = offset;
         int line = 1;
         int posHexa = 0;
         int logLength = Math.min(dataLength, trunkLength);
         StringBuilder sb = new StringBuilder(logLength * 3);
         sb.append("       +--------------------------------------------------+\n       |  0  1  2  3  4  5  6  7   8  9  a  b  c  d  e  f |\n+------+--------------------------------------------------+------------------+\n|000000| ");

         int remaining;
         for(; pos < logLength + offset; ++pos) {
            remaining = bytes[pos] & 255;
            sb.append(hexArray[remaining >>> 4]).append(hexArray[remaining & 15]).append(" ");
            hexaValue[posHexa++] = remaining > 31 && remaining < 127 ? (char)remaining : 46;
            if (posHexa == 8) {
               sb.append(" ");
            }

            if (posHexa == 16) {
               sb.append("| ").append(hexaValue).append(" |\n");
               if (pos + 1 != logLength + offset) {
                  sb.append("|").append(mediumIntTohexa(line++)).append("| ");
               }

               posHexa = 0;
            }
         }

         remaining = posHexa;
         if (posHexa > 0) {
            if (posHexa < 8) {
               while(remaining < 8) {
                  sb.append("   ");
                  ++remaining;
               }

               sb.append(" ");
            }

            while(remaining < 16) {
               sb.append("   ");
               ++remaining;
            }

            while(posHexa < 16) {
               hexaValue[posHexa] = ' ';
               ++posHexa;
            }

            sb.append("| ").append(hexaValue).append(" |\n");
         }

         if (dataLength > trunkLength) {
            sb.append("+------+-------------------truncated----------------------+------------------+\n");
         } else {
            sb.append("+------+--------------------------------------------------+------------------+\n");
         }

         return sb.toString();
      } else {
         return "";
      }
   }

   private static String mediumIntTohexa(int value) {
      StringBuilder st = new StringBuilder(Integer.toHexString(value * 16));

      while(st.length() < 6) {
         st.insert(0, "0");
      }

      return st.toString();
   }

   public static String hex(byte[] header, byte[] bytes, int offset, int dataLength, int trunkLength) {
      byte[] complete = new byte[dataLength + header.length];
      System.arraycopy(header, 0, complete, 0, header.length);
      System.arraycopy(bytes, offset, complete, header.length, dataLength);
      return hex(complete, 0, dataLength + header.length, trunkLength);
   }
}
