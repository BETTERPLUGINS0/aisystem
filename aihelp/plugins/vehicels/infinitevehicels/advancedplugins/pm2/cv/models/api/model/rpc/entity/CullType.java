package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import java.util.Locale;

public enum CullType {
   NO_CULL,
   MOVEMENT_ONLY,
   CULLED;

   public static CullType get(String var0) {
      try {
         return valueOf(var0.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var2) {
         return NO_CULL;
      }
   }

   private static CullType[] $values() {
      return new CullType[]{NO_CULL, MOVEMENT_ONLY, CULLED};
   }

   // $FF: synthetic method
   private static CullType[] $values$() {
      return new CullType[]{NO_CULL, MOVEMENT_ONLY, CULLED};
   }
}
