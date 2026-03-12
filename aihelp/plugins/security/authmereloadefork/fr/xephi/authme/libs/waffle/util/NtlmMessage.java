package fr.xephi.authme.libs.waffle.util;

public final class NtlmMessage {
   private static final byte[] NTLM_SSP_SIGNATURE = new byte[]{78, 84, 76, 77, 83, 83, 80, 0};

   public static boolean isNtlmMessage(byte[] message) {
      if (message != null && message.length >= NTLM_SSP_SIGNATURE.length) {
         for(int i = 0; i < NTLM_SSP_SIGNATURE.length; ++i) {
            if (NTLM_SSP_SIGNATURE[i] != message[i]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static int getMessageType(byte[] message) {
      return message[NTLM_SSP_SIGNATURE.length];
   }

   private NtlmMessage() {
   }
}
