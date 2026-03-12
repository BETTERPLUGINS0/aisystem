package fr.xephi.authme.data.limbo;

enum AuthGroupType {
   UNREGISTERED,
   REGISTERED_UNAUTHENTICATED,
   LOGGED_IN;

   // $FF: synthetic method
   private static AuthGroupType[] $values() {
      return new AuthGroupType[]{UNREGISTERED, REGISTERED_UNAUTHENTICATED, LOGGED_IN};
   }
}
