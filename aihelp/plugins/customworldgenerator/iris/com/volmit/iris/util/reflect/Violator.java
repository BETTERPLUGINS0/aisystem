package com.volmit.iris.util.reflect;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentSkipListMap;

public class Violator {
   protected static final ConcurrentSkipListMap<String, Object> nodes = new ConcurrentSkipListMap();

   private static String id(Object o, Object h) {
      String var10000;
      if (var0 instanceof Field) {
         var10000 = id(((Field)var0).getDeclaringClass(), (Object)null);
         return var10000 + "." + ((Field)var0).getName();
      } else if (var0 instanceof String) {
         return (String)var0;
      } else if (var0 instanceof Class) {
         return ((Class)var0).getCanonicalName();
      } else {
         int var5;
         if (var0 instanceof Constructor) {
            Constructor var9 = (Constructor)var0;
            StringBuilder var10 = new StringBuilder();
            Class[] var11 = var9.getParameterTypes();
            var5 = var11.length;

            for(int var12 = 0; var12 < var5; ++var12) {
               Class var7 = var11[var12];
               var10.append(",").append(var7.getCanonicalName());
            }

            var10 = new StringBuilder(var10.length() >= 1 ? var10.substring(1) : var10.toString());
            var10000 = id(var9.getDeclaringClass(), (Object)null);
            return var10000 + "(" + String.valueOf(var10) + ")";
         } else if (!(var0 instanceof Method)) {
            if (var0 instanceof Annotation) {
               Annotation var8 = (Annotation)var0;
               var10000 = var8.annotationType().getCanonicalName();
               return "@" + var10000 + "[" + id(var1, (Object)null) + "]";
            } else {
               int var13 = var0.hashCode();
               return var13 + var0.toString();
            }
         } else {
            StringBuilder var2 = new StringBuilder();
            Class[] var3 = ((Method)var0).getParameterTypes();
            int var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
               Class var6 = var3[var5];
               var2.append(",").append(var6.getCanonicalName());
            }

            var2 = new StringBuilder(var2.length() >= 1 ? var2.substring(1) : var2.toString());
            var10000 = id(((Method)var0).getDeclaringClass(), (Object)null);
            return var10000 + "." + ((Method)var0).getName() + "(" + String.valueOf(var2) + ")";
         }
      }
   }

   private static void p(String n, Object o) {
      nodes.put(var0, var1);
   }

   private static boolean h(String n) {
      return nodes.containsKey(var0);
   }

   private static Object g(String n) {
      return nodes.get(var0);
   }

   public static Constructor<?> getConstructor(Class<?> c, Class<?>... params) {
      StringBuilder var2 = new StringBuilder();
      Class[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var3[var5];
         var2.append(",").append(var6.getCanonicalName());
      }

      var2 = new StringBuilder(var2.length() >= 1 ? var2.substring(1) : var2.toString());
      String var10000 = id(var0, (Object)null);
      if (!h(var10000 + "(" + String.valueOf(var2) + ")")) {
         Constructor var7 = var0.getConstructor(var1);
         var7.setAccessible(true);
         p(id(var7, (Object)null), var7);
      }

      var10000 = id(var0, (Object)null);
      return (Constructor)g(var10000 + "(" + String.valueOf(var2) + ")");
   }

   public static Field getField(Class<?> c, String name) {
      String var10000 = id(var0, (Object)null);
      if (!h(var10000 + "." + var1)) {
         try {
            Field var2 = var0.getField(var1);
            var2.setAccessible(true);
            p(id(var0, (Object)null) + "." + var1, var2);
         } catch (NoSuchFieldException var5) {
            Iris.reportError(var5);
            Class var3 = var0.getSuperclass();
            if (null == var3) {
               throw var5;
            }

            Field var4 = var3.getField(var1);
            var4.setAccessible(true);
            p(id(var0, (Object)null) + "." + var1, var4);
         }
      }

      var10000 = id(var0, (Object)null);
      return (Field)g(var10000 + "." + var1);
   }

   public static Field getDeclaredField(Class<?> c, String name) {
      String var10000 = id(var0, (Object)null);
      if (!h(var10000 + "." + var1)) {
         try {
            Field var2 = var0.getDeclaredField(var1);
            var2.setAccessible(true);
            p(id(var0, (Object)null) + "." + var1, var2);
         } catch (NoSuchFieldException var5) {
            Iris.reportError(var5);
            Class var3 = var0.getSuperclass();
            if (null == var3) {
               throw var5;
            }

            Field var4 = var3.getDeclaredField(var1);
            var4.setAccessible(true);
            p(id(var0, (Object)null) + "." + var1, var4);
         }
      }

      var10000 = id(var0, (Object)null);
      return (Field)g(var10000 + "." + var1);
   }

   public static Method getMethod(Class<?> c, String name, Class<?>... pars) {
      String var3 = "";
      StringBuilder var4 = new StringBuilder();
      Class[] var5 = var2;
      int var6 = var2.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Class var8 = var5[var7];
         var4.append(",").append(var8.getCanonicalName());
      }

      var4 = new StringBuilder(var4.length() >= 1 ? var4.substring(1) : var4.toString());
      var3 = id(var0, (Object)null) + "." + var1 + "(" + String.valueOf(var4) + ")";
      if (!h(var3)) {
         Method var9 = var0.getMethod(var1, var2);
         var9.setAccessible(true);
         p(var3, var9);
      }

      return (Method)g(var3);
   }

   public static <T> T construct(Class<?> c, Object... parameters) {
      KList var2 = new KList();
      Object[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         var2.add((Object)var6.getClass());
      }

      try {
         Constructor var8 = getConstructor(var0, (Class[])var2.toArray(new Class[0]));
         return var8.newInstance(var1);
      } catch (Exception var7) {
         Iris.reportError(var7);
         var7.printStackTrace();
         return null;
      }
   }

   public static Method getDeclaredMethod(Class<?> c, String name, Class<?>... pars) {
      String var3 = "";
      StringBuilder var4 = new StringBuilder();
      Class[] var5 = var2;
      int var6 = var2.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Class var8 = var5[var7];
         var4.append(",").append(var8.getCanonicalName());
      }

      var4 = new StringBuilder(var4.length() >= 1 ? var4.substring(1) : var4.toString());
      var3 = id(var0, (Object)null) + "." + var1 + "(" + String.valueOf(var4) + ")";
      if (!h(var3)) {
         Method var9 = var0.getDeclaredMethod(var1, var2);
         var9.setAccessible(true);
         p(var3, var9);
      }

      return (Method)g(var3);
   }

   public static <T extends Annotation> T getAnnotation(Class<?> c, Class<? extends T> a) {
      String var10000 = var1.getCanonicalName();
      if (!h("@" + var10000 + "[" + var0.getCanonicalName() + "]")) {
         Annotation var2 = var0.getAnnotation(var1);
         p(id(var2, var0), var2);
      }

      var10000 = var1.getCanonicalName();
      return (Annotation)g("@" + var10000 + "[" + var0.getCanonicalName() + "]");
   }

   public static <T extends Annotation> T getDeclaredAnnotation(Class<?> c, Class<? extends T> a) {
      String var10000 = var1.getCanonicalName();
      if (!h("@" + var10000 + "[" + var0.getCanonicalName() + "]")) {
         Annotation var2 = var0.getDeclaredAnnotation(var1);
         p(id(var2, var0), var2);
      }

      var10000 = var1.getCanonicalName();
      return (Annotation)g("@" + var10000 + "[" + var0.getCanonicalName() + "]");
   }

   public static <T extends Annotation> T getAnnotation(Field c, Class<? extends T> a) {
      String var10000 = var1.getCanonicalName();
      if (!h("@" + var10000 + "[" + id(var0, (Object)null) + "]")) {
         Annotation var2 = var0.getAnnotation(var1);
         p(id(var2, var0), var2);
      }

      var10000 = var1.getCanonicalName();
      return (Annotation)g("@" + var10000 + "[" + id(var0, (Object)null) + "]");
   }

   public static <T extends Annotation> T getDeclaredAnnotation(Field c, Class<? extends T> a) {
      String var10000 = var1.getCanonicalName();
      if (!h("@" + var10000 + "[" + id(var0, (Object)null) + "]")) {
         Annotation var2 = var0.getDeclaredAnnotation(var1);
         p(id(var2, var0), var2);
      }

      var10000 = var1.getCanonicalName();
      return (Annotation)g("@" + var10000 + "[" + id(var0, (Object)null) + "]");
   }

   public static <T extends Annotation> T getAnnotation(Method c, Class<? extends T> a) {
      String var10000 = var1.getCanonicalName();
      if (!h("@" + var10000 + "[" + id(var0, (Object)null) + "]")) {
         Annotation var2 = var0.getAnnotation(var1);
         p(id(var2, var0), var2);
      }

      var10000 = var1.getCanonicalName();
      return (Annotation)g("@" + var10000 + "[" + id(var0, (Object)null) + "]");
   }

   public static <T extends Annotation> T getDeclaredAnnotation(Method c, Class<? extends T> a) {
      String var10000 = var1.getCanonicalName();
      if (!h("@" + var10000 + "[" + id(var0, (Object)null) + "]")) {
         Annotation var2 = var0.getDeclaredAnnotation(var1);
         p(id(var2, var0), var2);
         var10000 = id(var2, var0);
         Iris.debug("Set as " + var10000 + " as @" + var1.getCanonicalName() + "[" + id(var0, (Object)null) + "]");
      }

      var10000 = var1.getCanonicalName();
      return (Annotation)g("@" + var10000 + "[" + id(var0, (Object)null) + "]");
   }
}
