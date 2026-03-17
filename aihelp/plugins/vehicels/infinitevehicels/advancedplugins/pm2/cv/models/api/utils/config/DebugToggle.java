package advancedplugins.pm2.cv.models.api.utils.config;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public enum DebugToggle {
   SHOW_OBB,
   SHOW_CULL_POINTS,
   OOM_TEST;

   private static final Set<DebugToggle> toggles = new HashSet();

   public static void setDebug(DebugToggle var0, boolean var1) {
      if (var1) {
         toggles.add(var0);
      } else {
         toggles.remove(var0);
      }

   }

   public static boolean isDebugging(DebugToggle var0) {
      return toggles.contains(var0);
   }

   @Nullable
   public static DebugToggle get(String var0) {
      try {
         return valueOf(var0);
      } catch (Throwable var2) {
         return null;
      }
   }

   private static DebugToggle[] $values() {
      return new DebugToggle[]{SHOW_OBB, SHOW_CULL_POINTS, OOM_TEST};
   }

   // $FF: synthetic method
   private static DebugToggle[] $values$() {
      return new DebugToggle[]{SHOW_OBB, SHOW_CULL_POINTS, OOM_TEST};
   }
}
