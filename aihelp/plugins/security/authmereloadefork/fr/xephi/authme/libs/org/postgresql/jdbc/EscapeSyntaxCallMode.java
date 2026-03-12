package fr.xephi.authme.libs.org.postgresql.jdbc;

public enum EscapeSyntaxCallMode {
   SELECT("select"),
   CALL_IF_NO_RETURN("callIfNoReturn"),
   CALL("call");

   private final String value;

   private EscapeSyntaxCallMode(String value) {
      this.value = value;
   }

   public static EscapeSyntaxCallMode of(String mode) {
      EscapeSyntaxCallMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EscapeSyntaxCallMode escapeSyntaxCallMode = var1[var3];
         if (escapeSyntaxCallMode.value.equals(mode)) {
            return escapeSyntaxCallMode;
         }
      }

      return SELECT;
   }

   public String value() {
      return this.value;
   }

   // $FF: synthetic method
   private static EscapeSyntaxCallMode[] $values() {
      return new EscapeSyntaxCallMode[]{SELECT, CALL_IF_NO_RETURN, CALL};
   }
}
