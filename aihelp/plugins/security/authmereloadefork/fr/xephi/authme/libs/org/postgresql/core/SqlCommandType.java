package fr.xephi.authme.libs.org.postgresql.core;

public enum SqlCommandType {
   BLANK,
   INSERT,
   UPDATE,
   DELETE,
   MOVE,
   SELECT,
   WITH,
   CREATE,
   ALTER;

   // $FF: synthetic method
   private static SqlCommandType[] $values() {
      return new SqlCommandType[]{BLANK, INSERT, UPDATE, DELETE, MOVE, SELECT, WITH, CREATE, ALTER};
   }
}
