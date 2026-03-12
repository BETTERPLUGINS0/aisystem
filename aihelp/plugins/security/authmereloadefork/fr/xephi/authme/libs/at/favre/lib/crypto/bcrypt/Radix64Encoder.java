package fr.xephi.authme.libs.at.favre.lib.crypto.bcrypt;

public interface Radix64Encoder {
   byte[] encode(byte[] var1);

   byte[] decode(byte[] var1);

   public static class Default implements Radix64Encoder {
      private static final byte[] DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1, -1, -2, -1, -1, -1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, -1, -1, -1, -1, -1, -1, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
      private static final byte[] MAP = new byte[]{46, 47, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};

      public byte[] encode(byte[] in) {
         return encode(in, MAP);
      }

      public byte[] decode(byte[] in) {
         int limit;
         for(limit = in.length; limit > 0; --limit) {
            byte c = in[limit - 1];
            if (c != 61 && c != 10 && c != 13 && c != 32 && c != 9) {
               break;
            }
         }

         byte[] out = new byte[(int)((long)limit * 6L / 8L)];
         int outCount = 0;
         int inCount = 0;
         int word = 0;

         int lastWordChars;
         for(lastWordChars = 0; lastWordChars < limit; ++lastWordChars) {
            byte c = in[lastWordChars];
            if (c == 46 || c == 47 || c >= 65 && c <= 122 || c >= 48 && c <= 57) {
               int bits = DECODE_TABLE[c];
               word = word << 6 | (byte)bits;
               ++inCount;
               if (inCount % 4 == 0) {
                  out[outCount++] = (byte)(word >> 16);
                  out[outCount++] = (byte)(word >> 8);
                  out[outCount++] = (byte)word;
               }
            } else if (c != 10 && c != 13 && c != 32 && c != 9) {
               throw new IllegalArgumentException("invalid character to decode: " + c);
            }
         }

         lastWordChars = inCount % 4;
         if (lastWordChars == 1) {
            return new byte[0];
         } else {
            if (lastWordChars == 2) {
               word <<= 12;
               out[outCount++] = (byte)(word >> 16);
            } else if (lastWordChars == 3) {
               word <<= 6;
               out[outCount++] = (byte)(word >> 16);
               out[outCount++] = (byte)(word >> 8);
            }

            if (outCount == out.length) {
               return out;
            } else {
               byte[] prefix = new byte[outCount];
               System.arraycopy(out, 0, prefix, 0, outCount);
               return prefix;
            }
         }
      }

      private static byte[] encode(byte[] in, byte[] map) {
         int length = 4 * (in.length / 3) + (in.length % 3 == 0 ? 0 : in.length % 3 + 1);
         byte[] out = new byte[length];
         int index = 0;
         int end = in.length - in.length % 3;

         for(int i = 0; i < end; i += 3) {
            out[index++] = map[(in[i] & 255) >> 2];
            out[index++] = map[(in[i] & 3) << 4 | (in[i + 1] & 255) >> 4];
            out[index++] = map[(in[i + 1] & 15) << 2 | (in[i + 2] & 255) >> 6];
            out[index++] = map[in[i + 2] & 63];
         }

         switch(in.length % 3) {
         case 1:
            out[index++] = map[(in[end] & 255) >> 2];
            out[index] = map[(in[end] & 3) << 4];
            break;
         case 2:
            out[index++] = map[(in[end] & 255) >> 2];
            out[index++] = map[(in[end] & 3) << 4 | (in[end + 1] & 255) >> 4];
            out[index] = map[(in[end + 1] & 15) << 2];
         }

         return out;
      }
   }
}
