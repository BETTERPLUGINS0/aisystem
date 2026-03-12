package ac.grim.grimac.utils.viaversion;

import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import lombok.Generated;

public final class ViaVersionUtil {
   public static final boolean isAvailable = ReflectionUtils.hasClass("com.viaversion.viaversion.api.Via");

   public static void injectHooks() {
      if (isAvailable) {
         ViaVersionHooks.load();
      }

   }

   @Generated
   private ViaVersionUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      if (!isAvailable && ReflectionUtils.hasClass("us.myles.ViaVersion.api.Via")) {
         LogUtil.error("Using unsupported ViaVersion 4.0 API, update ViaVersion to 5.0");
      }

   }
}
