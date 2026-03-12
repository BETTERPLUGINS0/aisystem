package ac.grim.grimac.utils.reflection;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.Generated;

public final class ReflectionUtils {
   public static boolean hasClass(String className) {
      return getClass(className) != null;
   }

   public static boolean hasMethod(@NotNull Class<?> clazz, String methodName, Class<?>... parameterTypes) {
      return getMethod(clazz, methodName, parameterTypes) != null;
   }

   @Nullable
   public static Method getMethod(@NotNull Class<?> clazz, @NotNull String methodName, Class<?>... parameterTypes) {
      try {
         return clazz.getMethod(methodName, parameterTypes);
      } catch (NoSuchMethodException var6) {
         while(clazz != null) {
            try {
               return clazz.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException var5) {
               clazz = clazz.getSuperclass();
            }
         }

         return null;
      }
   }

   @Nullable
   public static Class<?> getClass(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException var2) {
         return null;
      }
   }

   public static Field getField(Class<?> clazz, String fieldName) {
      while(clazz != null) {
         try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
         } catch (NoSuchFieldException var3) {
            clazz = clazz.getSuperclass();
         }
      }

      return null;
   }

   @Generated
   private ReflectionUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
