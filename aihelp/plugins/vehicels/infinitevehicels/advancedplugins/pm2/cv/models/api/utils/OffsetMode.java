package advancedplugins.pm2.cv.models.api.utils;

public enum OffsetMode {
   LOCAL,
   MODEL,
   GLOBAL;

   public static OffsetMode get(String var0) {
      try {
         return valueOf(var0);
      } catch (IllegalArgumentException var2) {
         return LOCAL;
      }
   }

   private static OffsetMode[] $values() {
      return new OffsetMode[]{LOCAL, MODEL, GLOBAL};
   }

   // $FF: synthetic method
   private static OffsetMode[] $values$() {
      return new OffsetMode[]{LOCAL, MODEL, GLOBAL};
   }
}
