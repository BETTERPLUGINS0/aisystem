package advancedplugins.pm2.cv.models.core.java21;

import advancedplugins.pm2.cv.models.api.utils.CompatibilityManager;
import advancedplugins.pm2.cv.models.api.utils.Utils;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import org.bukkit.plugin.java.JavaPlugin;

public enum Java21Access implements ReflectionUtils.MethodEnum {
   registerCompatibility(new Class[]{CompatibilityManager.class, JavaPlugin.class}),
   createNMSHandler(new Class[]{String.class}),
   getMythicPackModelFiles(new Class[0]);

   private static Class<?> HELPER;
   private final Class<?>[] parameterClasses;

   private Java21Access(Class<?>... param3) {
      this.parameterClasses = var3;
   }

   private static Class<?> getHelper() {
      return null;
   }

   public Class<?> target() {
      return getHelper();
   }

   public String getObfuscated() {
      return this.toString();
   }

   public String getMapped() {
      return this.toString();
   }

   public <T> T call(Object... var1) {
      return !Utils.isJava21OrHigher() ? null : ReflectionUtils.call((Object)null, this, var1);
   }

   public Class<?>[] getParameterClasses() {
      return this.parameterClasses;
   }

   // $FF: synthetic method
   private static Java21Access[] $values() {
      return new Java21Access[]{registerCompatibility, createNMSHandler, getMythicPackModelFiles};
   }
}
