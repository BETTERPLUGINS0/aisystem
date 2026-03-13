package com.volmit.iris.util.slimjar.injector.loader;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class IsolatedInjectableClassLoader extends InjectableClassLoader {
   @NotNull
   private final Map<String, Class<?>> delegatesMap;

   public IsolatedInjectableClassLoader() {
      this();
   }

   public IsolatedInjectableClassLoader(@NotNull URL... var1) {
      this(var1, Collections.emptySet());
   }

   public IsolatedInjectableClassLoader(@NotNull URL[] var1, @NotNull Collection<Class<?>> var2) {
      this(var1, var2, getSystemClassLoader().getParent());
   }

   public IsolatedInjectableClassLoader(@NotNull URL[] var1, @NotNull Collection<Class<?>> var2, @NotNull ClassLoader var3) {
      super(var1, var3);
      this.delegatesMap = new HashMap();
      var2.forEach((var1x) -> {
         this.delegatesMap.put(var1x.getName(), var1x);
      });
   }

   protected Class<?> loadClass(@NotNull String var1, boolean var2) {
      Class var3 = this.findLoadedClass(var1);
      if (var3 != null) {
         return var3;
      } else {
         Class var4 = (Class)this.delegatesMap.get(var1);
         return var4 != null ? var4 : super.loadClass(var1, var2);
      }
   }

   static {
      registerAsParallelCapable();
   }
}
