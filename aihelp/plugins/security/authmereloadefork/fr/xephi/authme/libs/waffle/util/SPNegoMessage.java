package fr.xephi.authme.libs.waffle.util;

public final class SPNegoMessage {
   private static final byte[] SPENGO_OID = new byte[]{6, 6, 43, 6, 1, 5, 5, 2};

   public static boolean isNegTokenInit(byte[] message) {
      if (message != null && message.length >= 2) {
         if (message[0] != 96) {
            return false;
         } else {
            int lenBytes = 1;
            if ((message[1] & 128) != 0) {
               lenBytes = 1 + (message[1] & 127);
            }

            if (message.length < SPENGO_OID.length + 1 + lenBytes) {
               return false;
            } else {
               for(int i = 0; i < SPENGO_OID.length; ++i) {
                  if (SPENGO_OID[i] != message[i + 1 + lenBytes]) {
                     return false;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   public static boolean isNegTokenArg(byte[] message) {
      if (message != null && message.length >= 2) {
         if ((message[0] & 255) != 161) {
            return false;
         } else {
            int len;
            if ((message[1] & 128) == 0) {
               len = message[1];
            } else {
               int lenBytes = message[1] & 127;
               len = 0;

               for(boolean var3 = true; lenBytes > 0; --lenBytes) {
                  len <<= 8;
                  len |= message[2] & 255;
               }
            }

            return len + 2 == message.length;
         }
      } else {
         return false;
      }
   }

   private SPNegoMessage() {
   }
}
