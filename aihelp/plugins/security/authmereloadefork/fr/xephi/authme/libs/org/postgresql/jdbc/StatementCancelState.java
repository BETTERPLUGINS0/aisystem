package fr.xephi.authme.libs.org.postgresql.jdbc;

enum StatementCancelState {
   IDLE,
   IN_QUERY,
   CANCELING,
   CANCELLED;

   // $FF: synthetic method
   private static StatementCancelState[] $values() {
      return new StatementCancelState[]{IDLE, IN_QUERY, CANCELING, CANCELLED};
   }
}
