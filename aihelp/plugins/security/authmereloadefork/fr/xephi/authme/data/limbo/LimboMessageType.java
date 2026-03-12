package fr.xephi.authme.data.limbo;

public enum LimboMessageType {
   REGISTER,
   LOG_IN,
   TOTP_CODE;

   // $FF: synthetic method
   private static LimboMessageType[] $values() {
      return new LimboMessageType[]{REGISTER, LOG_IN, TOTP_CODE};
   }
}
