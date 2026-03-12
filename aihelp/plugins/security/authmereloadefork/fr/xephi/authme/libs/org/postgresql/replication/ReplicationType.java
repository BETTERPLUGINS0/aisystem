package fr.xephi.authme.libs.org.postgresql.replication;

public enum ReplicationType {
   LOGICAL,
   PHYSICAL;

   // $FF: synthetic method
   private static ReplicationType[] $values() {
      return new ReplicationType[]{LOGICAL, PHYSICAL};
   }
}
