package fr.xephi.authme.service;

public enum SessionState {
   VALID,
   NOT_VALID,
   OUTDATED,
   IP_CHANGED;

   // $FF: synthetic method
   private static SessionState[] $values() {
      return new SessionState[]{VALID, NOT_VALID, OUTDATED, IP_CHANGED};
   }
}
