package com.volmit.iris.util.slimjar.app.builder;

import com.volmit.iris.util.slimjar.app.Application;
import com.volmit.iris.util.slimjar.exceptions.SlimJarException;
import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.injector.loader.IsolatedInjectableClassLoader;
import com.volmit.iris.util.slimjar.util.Reflections;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collections;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IsolatedApplicationBuilder extends ModularApplicationBuilder<IsolatedApplicationBuilder> {
   @Nullable
   @NotNull
   private final Object[] arguments;
   @NotNull
   private final String applicationClass;
   @Nullable
   private ClassLoader parentClassloader;

   @Contract(
      pure = true
   )
   public IsolatedApplicationBuilder(@NotNull String var1, @NotNull String var2, @Nullable @NotNull Object... var3) {
      super(var1, (var0) -> {
         return new IsolatedInjectableClassLoader(new URL[0], Collections.singleton(Application.class), var0.getParentClassloader());
      });
      this.applicationClass = var2;
      this.arguments = (Object[])var3.clone();
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public IsolatedApplicationBuilder parentClassLoader(@NotNull ClassLoader var1) {
      this.parentClassloader = var1;
      return (IsolatedApplicationBuilder)this.self;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   private ClassLoader getParentClassloader() {
      if (this.parentClassloader == null) {
         this.parentClassloader = ClassLoader.getSystemClassLoader().getParent();
      }

      return this.parentClassloader;
   }

   protected Application buildApplication(@NotNull Injectable var1) {
      try {
         Class var2 = Class.forName(this.applicationClass, true, var1.getClassLoader());
         return (Application)Reflections.findConstructor(var2, this.arguments).newInstance(this.arguments);
      } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException var3) {
         throw new SlimJarException("Failed to reflectively create application class.", var3);
      }
   }
}
