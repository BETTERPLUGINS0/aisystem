package fr.xephi.authme.libs.waffle.windows.auth;

public enum PrincipalFormat {
   FQN,
   SID,
   BOTH,
   NONE;

   // $FF: synthetic method
   private static PrincipalFormat[] $values() {
      return new PrincipalFormat[]{FQN, SID, BOTH, NONE};
   }
}
