package com.volmit.iris.util.slimjar.injector.loader;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import java.net.URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface Injectable {
   void inject(@NotNull URL var1) throws InjectorException;

   default boolean isThreadSafe() {
      return false;
   }

   @Nullable
   default ClassLoader getClassLoader() {
      return null;
   }
}
