package advancedplugins.pm2.cv.models.api.utils.reflections;

import advancedplugins.pm2.cv.models.api.ServerInfo;
import com.google.common.collect.Maps;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;

public class ReflectionUtils {
   private static final Map<Class<?>, Class<?>> PRIMITIVE_EQUIV = new ConcurrentHashMap<Class<?>, Class<?>>() {
      {
         this.put(Byte.TYPE, Byte.class);
         this.put(Short.TYPE, Short.class);
         this.put(Integer.TYPE, Integer.class);
         this.put(Long.TYPE, Long.class);
         this.put(Float.TYPE, Float.class);
         this.put(Double.TYPE, Double.class);
         this.put(Boolean.TYPE, Boolean.class);
         this.put(Character.TYPE, Character.class);
      }
   };
   private static final Map<Class<?>, ConcurrentHashMap<String, ReflectionUtils.ReflectionEnum>> DYNAMIC_FIELDS = Maps.newConcurrentMap();
   private static final Map<Class<?>, ConcurrentHashMap<ReflectionUtils.ReflectionEnum, Field>> FIELD_MAP = Maps.newConcurrentMap();
   private static final Map<Class<?>, ConcurrentHashMap<ReflectionUtils.MethodEnum, Method>> METHOD_MAP = Maps.newConcurrentMap();
   private static final boolean IS_MAPPED = ServerInfo.higherThanOrEqual("1.21.11");

   public static Field unlockField(ReflectionUtils.ReflectionEnum var0) {
      try {
         Field var1 = var0.target().getDeclaredField(var0.get(IS_MAPPED));
         var1.setAccessible(true);
         return var1;
      } catch (IllegalArgumentException | NoSuchFieldException | SecurityException var2) {
         throw new RuntimeException("An error occurred while unlocking field: " + var0.getMapped(), var2);
      }
   }

   public static Method unlockMethod(ReflectionUtils.MethodEnum var0) {
      try {
         Method var1 = var0.target().getDeclaredMethod(var0.get(IS_MAPPED), var0.getParameterClasses());
         var1.setAccessible(true);
         return var1;
      } catch (IllegalArgumentException | NoSuchMethodException | SecurityException var2) {
         var2.printStackTrace();
         throw new RuntimeException("An error occurred while unlocking method: " + var0.getMapped());
      }
   }

   public static Field getField(Class<?> var0, String var1) {
      return getField((ReflectionUtils.ReflectionEnum)((ConcurrentHashMap)DYNAMIC_FIELDS.computeIfAbsent(var0, (var0x) -> {
         return new ConcurrentHashMap();
      })).computeIfAbsent(var1, (var2) -> {
         return new ReflectionUtils.RuntimeReflection(var0, var1);
      }));
   }

   public static Field getField(ReflectionUtils.ReflectionEnum var0) {
      return (Field)((ConcurrentHashMap)FIELD_MAP.computeIfAbsent(var0.target(), (var0x) -> {
         return new ConcurrentHashMap();
      })).computeIfAbsent(var0, ReflectionUtils::unlockField);
   }

   public static Method getMethod(ReflectionUtils.MethodEnum var0) {
      return (Method)((ConcurrentHashMap)METHOD_MAP.computeIfAbsent(var0.target(), (var0x) -> {
         return new ConcurrentHashMap();
      })).computeIfAbsent(var0, ReflectionUtils::unlockMethod);
   }

   @Nullable
   public static <T> T get(Object var0, ReflectionUtils.ReflectionEnum var1) {
      try {
         return getField(var1).get(var0);
      } catch (IllegalAccessException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   @Nullable
   public static <T> T get(Object var0, ReflectionUtils.ReflectionEnum var1, T var2) {
      try {
         return getField(var1).get(var0);
      } catch (IllegalAccessException var4) {
         var4.printStackTrace();
         return var2;
      }
   }

   @Nullable
   public static <T> T get(ReflectionUtils.ReflectionEnum var0) {
      return get((Object)null, var0);
   }

   public static boolean set(Object var0, ReflectionUtils.ReflectionEnum var1, Object var2) {
      try {
         getField(var1).set(var0, var2);
         return true;
      } catch (IllegalAccessException var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public static boolean set(ReflectionUtils.ReflectionEnum var0, Object var1) {
      return set((Object)null, var0, var1);
   }

   public static <T> T call(Object var0, ReflectionUtils.MethodEnum var1, Object... var2) {
      try {
         Class[] var3 = var1.getParameterClasses();
         if (var3.length > var2.length) {
            throw new RuntimeException(String.format("Invalid method call: Missing parameters. Expected %s, got %s.", var3.length, var2.length));
         } else {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               Class var5 = var2[var4].getClass();
               Class var6 = var3[var4];
               if (!var6.isAssignableFrom(var5) && isDifferentPrimitive(var5, var6)) {
                  throw new RuntimeException(String.format("Invalid method call: Invalid parameter at position %s. Expected %s, got %s.", var4, var6.getSimpleName(), var5.getSimpleName()));
               }
            }

            Method var8 = getMethod(var1);
            return var8.invoke(var0, var2);
         }
      } catch (IllegalAccessException | InvocationTargetException var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public static <T> T call(ReflectionUtils.MethodEnum var0, Object... var1) {
      return call((Object)null, var0, var1);
   }

   private static boolean isDifferentPrimitive(Class<?> var0, Class<?> var1) {
      if (!var1.isPrimitive()) {
         return true;
      } else {
         Class var2 = (Class)PRIMITIVE_EQUIV.get(var1);
         return !var2.isAssignableFrom(var0);
      }
   }

   public interface ReflectionEnum {
      Class<?> target();

      String getObfuscated();

      String getMapped();

      default String get(boolean mapped) {
         return mapped ? this.getMapped() : this.getObfuscated();
      }
   }

   public interface MethodEnum extends ReflectionUtils.ReflectionEnum {
      Class<?>[] getParameterClasses();
   }

   static record RuntimeReflection(Class<?> target, String field) implements ReflectionUtils.ReflectionEnum {
      RuntimeReflection(Class<?> target, String field) {
         this.target = var1;
         this.field = var2;
      }

      public String getObfuscated() {
         return this.field;
      }

      public String getMapped() {
         return this.field;
      }

      public Class<?> target() {
         return this.target;
      }

      public String field() {
         return this.field;
      }
   }
}
