package advancedplugins.pm2.cv.api.util.reflection;

import java.lang.reflect.Method;
import org.bukkit.util.OldEnum;

public class EnumReflection {
   public static <T extends Enum<T>> T getEnumConstant(Class<T> var0, String var1) {
      try {
         return Enum.valueOf(var0, var1);
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   public static <T extends OldEnum<T>> T getOldEnumConstant(Class<T> var0, String var1) {
      try {
         Method var2 = var0.getMethod("valueOf", String.class);
         return (OldEnum)var2.invoke((Object)null, var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
