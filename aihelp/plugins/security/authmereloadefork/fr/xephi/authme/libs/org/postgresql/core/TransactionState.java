package fr.xephi.authme.libs.org.postgresql.core;

public enum TransactionState {
   IDLE,
   OPEN,
   FAILED;

   // $FF: synthetic method
   private static TransactionState[] $values() {
      return new TransactionState[]{IDLE, OPEN, FAILED};
   }
}
