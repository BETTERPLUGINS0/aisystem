package com.volmit.iris.util.slimjar.app.builder;

import com.volmit.iris.util.slimjar.app.AppendingApplication;
import com.volmit.iris.util.slimjar.app.Application;
import com.volmit.iris.util.slimjar.injector.DependencyInjector;
import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.injector.loader.factory.InjectableFactory;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.DependencyDataProvider;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.PreResolutionDataProvider;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class InjectingApplicationBuilder<T extends InjectingApplicationBuilder<T>> extends ApplicationBuilder<T> {
   @NotNull
   protected final Function<T, Injectable> injectableSupplier;
   @Nullable
   protected InjectableFactory injectableFactory;

   @Contract(
      pure = true
   )
   public InjectingApplicationBuilder(@NotNull String var1, @NotNull Function<T, Injectable> var2) {
      super(var1);
      this.injectableSupplier = var2;
   }

   @Contract(
      value = "-> new",
      mutates = "this"
   )
   @NotNull
   protected Application buildApplication() {
      DependencyDataProvider var1 = this.getDataProviderFactory().create(this.getDependencyFileUrl());
      DependencyData var2 = var1.get();
      DependencyInjector var3 = this.createInjector();
      PreResolutionDataProvider var4 = this.getPreResolutionDataProviderFactory().create(this.getPreResolutionFileUrl());
      Map var5 = var4.get();
      var3.inject((Injectable)this.injectableSupplier.apply((InjectingApplicationBuilder)this.self), var2, var5);
      return new AppendingApplication();
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static InjectingApplicationBuilder<?> create(@NotNull String var0) {
      ClassLoader var1 = ApplicationBuilder.class.getClassLoader();
      return create(var0, var1);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   public static InjectingApplicationBuilder<?> create(@NotNull String var0, @NotNull ClassLoader var1) {
      return new InjectingApplicationBuilder.Impl(var0, (var1x) -> {
         return var1x.getInjectableFactory().create(var1x.getDownloadDirectoryPath(), Collections.singleton(Repository.central()), var1);
      });
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   public static InjectingApplicationBuilder<?> create(@NotNull String var0, @NotNull Injectable var1) {
      return new InjectingApplicationBuilder.Impl(var0, (var1x) -> {
         return var1;
      });
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public T injectableFactory(@NotNull InjectableFactory var1) {
      this.injectableFactory = var1;
      return (InjectingApplicationBuilder)this.self;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final InjectableFactory getInjectableFactory() {
      if (this.injectableFactory == null) {
         this.injectableFactory = InjectableFactory.DEFAULT;
      }

      return this.injectableFactory;
   }

   private static final class Impl extends InjectingApplicationBuilder<InjectingApplicationBuilder.Impl> {
      private Impl(@NotNull String var1, @NotNull Function<InjectingApplicationBuilder.Impl, Injectable> var2) {
         super(var1, var2);
      }
   }
}
