package fr.xephi.authme.data.limbo;

public enum LimboPlayerState {
   PASSWORD_REQUIRED,
   TOTP_REQUIRED;

   // $FF: synthetic method
   private static LimboPlayerState[] $values() {
      return new LimboPlayerState[]{PASSWORD_REQUIRED, TOTP_REQUIRED};
   }
}
