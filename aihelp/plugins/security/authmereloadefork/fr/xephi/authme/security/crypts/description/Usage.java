package fr.xephi.authme.security.crypts.description;

public enum Usage {
   RECOMMENDED,
   ACCEPTABLE,
   DO_NOT_USE,
   DEPRECATED,
   DOES_NOT_WORK;

   // $FF: synthetic method
   private static Usage[] $values() {
      return new Usage[]{RECOMMENDED, ACCEPTABLE, DO_NOT_USE, DEPRECATED, DOES_NOT_WORK};
   }
}
