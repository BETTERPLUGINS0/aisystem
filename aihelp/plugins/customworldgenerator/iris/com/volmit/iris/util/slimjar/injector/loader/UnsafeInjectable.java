package com.volmit.iris.util.slimjar.injector.loader;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

public final class UnsafeInjectable implements Injectable {
   @NotNull
   private final ArrayDeque<URL> unopenedURLs;
   @NotNull
   private final ArrayList<URL> pathURLs;
   @NotNull
   private final ClassLoader classLoader;

   public UnsafeInjectable(@NotNull ArrayDeque<URL> var1, @NotNull ArrayList<URL> var2, @NotNull ClassLoader var3) {
      this.unopenedURLs = var1;
      this.pathURLs = var2;
      this.classLoader = var3;
   }

   public boolean isThreadSafe() {
      return true;
   }

   @NotNull
   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public void inject(@NotNull URL var1) {
      synchronized(this.unopenedURLs) {
         if (!this.pathURLs.contains(var1)) {
            this.unopenedURLs.addLast(var1);
            this.pathURLs.add(var1);
         }

      }
   }

   @Contract("_ -> new")
   public static Injectable create(ClassLoader var0) {
      Field var1 = Unsafe.class.getDeclaredField("theUnsafe");
      var1.setAccessible(true);
      Unsafe var2 = (Unsafe)var1.get((Object)null);
      Field var3 = Lookup.class.getDeclaredField("IMPL_LOOKUP");
      Lookup var4 = (Lookup)var2.getObject(var2.staticFieldBase(var3), var2.staticFieldOffset(var3));

      try {
         Object var5 = var4.findGetter(var0.getClass(), "ucp", var4.findClass("jdk.internal.loader.URLClassPath")).invoke(var0);
         return new UnsafeInjectable((ArrayDeque)fetchField(var2, var5, "unopenedUrls"), (ArrayList)fetchField(var2, var5, "path"), var0);
      } catch (Throwable var7) {
         if (var7 instanceof ReflectiveOperationException) {
            ReflectiveOperationException var6 = (ReflectiveOperationException)var7;
            throw var6;
         } else {
            throw new InvocationTargetException(var7);
         }
      }
   }

   private static Object fetchField(Unsafe var0, Object var1, String var2) {
      return fetchField(var0, var1.getClass(), var1, var2);
   }

   @NotNull
   private static Object fetchField(@NotNull Unsafe var0, @NotNull Class<?> var1, @NotNull Object var2, @NotNull String var3) {
      Field var4 = var1.getDeclaredField(var3);
      long var5 = var0.objectFieldOffset(var4);
      return var0.getObject(var2, var5);
   }
}
