package fr.xephi.authme.security.crypts.description;

public enum SaltType {
   TEXT,
   USERNAME,
   NONE;

   // $FF: synthetic method
   private static SaltType[] $values() {
      return new SaltType[]{TEXT, USERNAME, NONE};
   }
}
