package com.volmit.iris.util.slimjar.injector.loader;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.NotNull;

public final class WrappedInjectableClassLoader implements Injectable {
   @NotNull
   private final URLClassLoader urlClassLoader;
   @NotNull
   private final Method addURLMethod;

   public WrappedInjectableClassLoader(@NotNull URLClassLoader var1) {
      this.urlClassLoader = var1;
      this.addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      this.addURLMethod.setAccessible(true);
   }

   public void inject(@NotNull URL var1) {
      try {
         this.addURLMethod.invoke(this.urlClassLoader, var1);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         throw new InjectorException("Unable to invoke addURL method", var3);
      }
   }

   public boolean isThreadSafe() {
      return true;
   }

   @NotNull
   public ClassLoader getClassLoader() {
      return this.urlClassLoader;
   }
}
