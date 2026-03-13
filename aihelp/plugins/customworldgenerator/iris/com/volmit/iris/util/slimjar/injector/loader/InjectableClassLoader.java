package com.volmit.iris.util.slimjar.injector.loader;

import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class InjectableClassLoader extends URLClassLoader implements Injectable {
   protected InjectableClassLoader(@NotNull URL[] var1, @NotNull ClassLoader var2) {
      super(var1, var2);
   }

   public void inject(@NotNull URL var1) {
      this.addURL(var1);
   }

   public boolean isThreadSafe() {
      return true;
   }

   @Nullable
   public ClassLoader getClassLoader() {
      return this;
   }

   static {
      registerAsParallelCapable();
   }
}
