package advancedplugins.pm2.cv.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionUtil {
   public static void callMethod(Object object, String methodName, List<Class<?>> parameterTypes, Object... arguments) {
      try {
         Class var4 = var0.getClass();
         Method var5 = var4.getDeclaredMethod(var1, (Class[])var2.toArray(new Class[0]));
         var5.setAccessible(true);
         var5.invoke(var0, var3);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void callMethod(Class<?> aClass, Object object, String methodName, List<Class<?>> parameterTypes, Object... arguments) {
      try {
         Method var5 = var0.getDeclaredMethod(var2, (Class[])var3.toArray(new Class[0]));
         var5.setAccessible(true);
         var5.invoke(var1, var4);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public static void setField(Object object, String fieldName, Object value) {
      try {
         Class var3 = var0.getClass();
         Field var4 = var3.getDeclaredField(var1);
         var4.setAccessible(true);
         var4.set(var0, var2);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void setField(Class<?> aClass, Object object, String fieldName, Object value) {
      try {
         Field var4 = var0.getDeclaredField(var2);
         var4.setAccessible(true);
         var4.set(var1, var3);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static Object getStaticField(Class<?> aClass, String fieldName, String... alternateFieldNames) {
      if (getStaticField(var0, var1) != null) {
         return getStaticField(var0, var1);
      } else {
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (getStaticField(var0, var6) != null) {
               return getStaticField(var0, var6);
            }
         }

         return null;
      }
   }

   public static Object getStaticField(Class<?> aClass, String fieldName) {
      try {
         Field var2 = var0.getDeclaredField(var1);
         var2.setAccessible(true);
         return var2.get((Object)null);
      } catch (NoSuchFieldException var3) {
         return null;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public static Object getField(Class<?> aClass, String... fieldNames) {
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];

         try {
            Field var6 = var0.getDeclaredField(var5);
            var6.setAccessible(true);
            Object var7 = var6.get((Object)null);
            if (var7 != null) {
               return var7;
            }
         } catch (IllegalAccessException | NoSuchFieldException var8) {
         }
      }

      return null;
   }
}
