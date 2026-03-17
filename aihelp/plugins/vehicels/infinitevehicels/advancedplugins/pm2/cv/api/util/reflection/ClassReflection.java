package advancedplugins.pm2.cv.api.util.reflection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.jetbrains.annotations.Nullable;

public final class ClassReflection {
   public static <T> T getFieldValue(Object var0, String var1, Class<T> var2) {
      Field var3 = var0.getClass().getDeclaredField(var1);
      var3.setAccessible(true);
      return var2.cast(var3.get(var0));
   }

   public static <T> T getMethodValue(Object var0, String var1, Class<T> var2, Object... var3) {
      Class[] var4 = (Class[])Arrays.stream(var3).map(Object::getClass).toArray((var0x) -> {
         return new Class[var0x];
      });
      Method var5 = var0.getClass().getDeclaredMethod(var1, var4);
      var5.setAccessible(true);
      return var2.cast(var5.invoke(var0, var3));
   }

   public static <T> T getMethodValue(Object var0, String var1, Class<T> var2, List<Class<?>> var3, Object... var4) {
      Class[] var5 = (Class[])var3.toArray((var0x) -> {
         return new Class[var0x];
      });
      Method var6 = var0.getClass().getDeclaredMethod(var1, var5);
      var6.setAccessible(true);
      return var2.cast(var6.invoke(var0, var4));
   }

   public static boolean isPrimitiveType(Class<?> var0) {
      return ClassReflection.EnumPrimitives.match(var0) != null;
   }

   public static Class<?> getPrimitiveType(Class<?> var0) {
      return ((ClassReflection.EnumPrimitives)Objects.requireNonNull(ClassReflection.EnumPrimitives.match(var0), "not a primitive")).primitive;
   }

   public static Class<?> getPrimitiveWrapperType(Class<?> var0) {
      return ((ClassReflection.EnumPrimitives)Objects.requireNonNull(ClassReflection.EnumPrimitives.match(var0), "not a primitive")).wrapper;
   }

   public static Class<?>[] getClasses(String var0) {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();

      assert var1 != null;

      String var2 = var0.replace('.', '/');
      Enumeration var3 = var1.getResources(var2);
      ArrayList var4 = new ArrayList();

      while(var3.hasMoreElements()) {
         URL var5 = (URL)var3.nextElement();
         var4.add(new File(var5.getFile()));
      }

      ArrayList var8 = new ArrayList();
      Iterator var6 = var4.iterator();

      while(var6.hasNext()) {
         File var7 = (File)var6.next();
         var8.addAll(findClasses(var7, var0));
      }

      return (Class[])var8.toArray(new Class[var8.size()]);
   }

   public static List<Class<?>> findClasses(File var0, String var1) {
      ArrayList var2 = new ArrayList();
      if (!var0.exists()) {
         return var2;
      } else {
         File[] var3 = var0.listFiles();
         File[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            if (var7.isDirectory()) {
               assert !var7.getName().contains(".");

               var2.addAll(findClasses(var7, var1 + "." + var7.getName()));
            } else if (var7.getName().endsWith(".class")) {
               try {
                  var2.add(Class.forName(var1 + "." + var7.getName().substring(0, var7.getName().length() - 6)));
               } catch (ClassNotFoundException var9) {
                  var9.printStackTrace();
               }
            }
         }

         return var2;
      }
   }

   public static Set<String> getClassNames(File var0, @Nullable String var1) {
      HashSet var2 = new HashSet();

      try {
         JarFile var3 = new JarFile(var0);
         Enumeration var4 = var3.entries();

         while(true) {
            String var6;
            do {
               if (!var4.hasMoreElements()) {
                  var3.close();
                  return var2;
               }

               JarEntry var5 = (JarEntry)var4.nextElement();
               var6 = var5.getName().replace("/", ".");
            } while(var1 != null && !var1.trim().isEmpty() && !var6.startsWith(var1.trim()));

            if (var6.endsWith(".class")) {
               var2.add(var6.substring(0, var6.lastIndexOf(".class")));
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         return var2;
      }
   }

   public static Set<Class<?>> getClasses(File var0, String var1) {
      HashSet var2 = new HashSet();
      getClassNames(var0, var1).forEach((var1x) -> {
         try {
            var2.add(Class.forName(var1x));
         } catch (ClassNotFoundException var3) {
            var3.printStackTrace();
         }

      });
      return var2;
   }

   private static enum EnumPrimitives {
      INTEGER(Integer.TYPE, Integer.class),
      LONG(Long.TYPE, Long.class),
      DOUBLE(Double.TYPE, Double.class),
      FLOAT(Float.TYPE, Float.class),
      BYTE(Byte.TYPE, Byte.class),
      SHORT(Short.TYPE, Short.class),
      BOOLEAN(Boolean.TYPE, Boolean.class);

      private final Class<?> primitive;
      private final Class<?> wrapper;

      private static ClassReflection.EnumPrimitives match(Class<?> var0) {
         ClassReflection.EnumPrimitives[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ClassReflection.EnumPrimitives var4 = var1[var3];
            if (var4.primitive == var0 || var4.wrapper == var0) {
               return var4;
            }
         }

         return null;
      }

      private EnumPrimitives(Class<?> param3, Class<?> param4) {
         this.primitive = var3;
         this.wrapper = var4;
      }

      // $FF: synthetic method
      private static ClassReflection.EnumPrimitives[] $values() {
         return new ClassReflection.EnumPrimitives[]{INTEGER, LONG, DOUBLE, FLOAT, BYTE, SHORT, BOOLEAN};
      }
   }
}
