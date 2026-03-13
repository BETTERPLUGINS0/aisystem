package com.volmit.iris.util.slimjar.injector.loader.factory;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.injector.loader.InstrumentationInjectable;
import com.volmit.iris.util.slimjar.injector.loader.UnsafeInjectable;
import com.volmit.iris.util.slimjar.injector.loader.WrappedInjectableClassLoader;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface InjectableFactory {
   @NotNull
   InjectableFactory INSTRUMENTATION = (downloadPath, repositories, loader) -> {
      return InstrumentationInjectable.create(downloadPath, repositories);
   };
   @NotNull
   InjectableFactory INJECTABLE = (downloadPath, repositories, loader) -> {
      if (loader instanceof Injectable) {
         Injectable injectable = (Injectable)loader;
         return injectable;
      } else {
         throw new InjectorException("Loader is not an instance of Injectable!");
      }
   };
   @NotNull
   InjectableFactory WRAPPED = (downloadPath, repositories, loader) -> {
      if (loader instanceof URLClassLoader) {
         URLClassLoader urlClassLoader = (URLClassLoader)loader;

         try {
            return new WrappedInjectableClassLoader(urlClassLoader);
         } catch (Throwable var5) {
            throw new InjectorException("Failed to create WrappedInjectableClassLoader!", var5);
         }
      } else {
         throw new InjectorException("Loader is not an instance of URLClassLoader!");
      }
   };
   @NotNull
   InjectableFactory UNSAFE = (downloadPath, repositories, loader) -> {
      try {
         return UnsafeInjectable.create(loader);
      } catch (Throwable var4) {
         throw new InjectorException("Failed to create UnsafeInjectable!", var4);
      }
   };
   @NotNull
   InjectableFactory ERROR = (downloadPath, repositories, loader) -> {
      throw new InjectorException("Failed to create injectable for class loader: " + String.valueOf(loader));
   };
   @NotNull
   InjectableFactory DEFAULT = selecting(INSTRUMENTATION, INJECTABLE, WRAPPED, UNSAFE);

   @NotNull
   Injectable create(@NotNull Path var1, @NotNull Collection<Repository> var2, @NotNull ClassLoader var3) throws InjectorException;

   @NotNull
   default Injectable create(@NotNull Path downloadPath, @NotNull Collection<Repository> repositories) throws InjectorException {
      return this.create(downloadPath, repositories, InjectableFactory.class.getClassLoader());
   }

   @NotNull
   static InjectableFactory selecting(@NotNull InjectableFactory fallback, @NotNull InjectableFactory... factories) {
      return new SelectingInjectableFactory(fallback, factories);
   }
}
