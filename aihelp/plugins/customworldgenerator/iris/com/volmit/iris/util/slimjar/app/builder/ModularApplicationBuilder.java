package com.volmit.iris.util.slimjar.app.builder;

import com.volmit.iris.util.slimjar.app.AppendingApplication;
import com.volmit.iris.util.slimjar.app.Application;
import com.volmit.iris.util.slimjar.app.module.ModuleExtractor;
import com.volmit.iris.util.slimjar.app.module.RelocatingModuleExtractor;
import com.volmit.iris.util.slimjar.injector.DependencyInjector;
import com.volmit.iris.util.slimjar.injector.loader.Injectable;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.DependencyDataProvider;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.DependencyDataProviderFactory;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.DependencyReader;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.ExternalDependencyDataProviderFactory;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.PreResolutionDataProvider;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.PreResolutionDataProviderFactory;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.PreResolutionDataReader;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.WrappingPreResolutionDataProviderFactory;
import com.volmit.iris.util.slimjar.util.Modules;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ModularApplicationBuilder<T extends ModularApplicationBuilder<T>> extends InjectingApplicationBuilder<T> {
   @NotNull
   private final Set<String> modules = ConcurrentHashMap.newKeySet();
   @Nullable
   private DependencyDataProviderFactory moduleDataProviderFactory;
   @Nullable
   private PreResolutionDataProviderFactory modulePreResolutionDataProviderFactory;
   @Nullable
   private ModuleExtractor moduleExtractor;

   @Contract(
      pure = true
   )
   public ModularApplicationBuilder(@NotNull String var1, @NotNull Function<T, Injectable> var2) {
      super(var1, var2);
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static ModularApplicationBuilder<?> create(@NotNull String var0) {
      ClassLoader var1 = ApplicationBuilder.class.getClassLoader();
      return create(var0, var1);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   public static ModularApplicationBuilder<?> create(@NotNull String var0, @NotNull ClassLoader var1) {
      return new ModularApplicationBuilder.Impl(var0, (var1x) -> {
         return var1x.getInjectableFactory().create(var1x.getDownloadDirectoryPath(), Collections.singleton(Repository.central()), var1);
      });
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   public static ModularApplicationBuilder<?> create(@NotNull String var0, @NotNull Injectable var1) {
      return new ModularApplicationBuilder.Impl(var0, (var1x) -> {
         return var1;
      });
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T moduleDataProviderFactory(@NotNull DependencyDataProviderFactory var1) {
      this.moduleDataProviderFactory = var1;
      return (ModularApplicationBuilder)this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public final T modulePreResolutionDataProviderFactory(@NotNull PreResolutionDataProviderFactory var1) {
      this.modulePreResolutionDataProviderFactory = var1;
      return (ModularApplicationBuilder)this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public T modules(@NotNull String... var1) {
      this.modules.addAll(List.of(var1));
      return (ModularApplicationBuilder)this.self;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   @NotNull
   public T moduleExtractor(@NotNull ModuleExtractor var1) {
      this.moduleExtractor = var1;
      return (ModularApplicationBuilder)this.self;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final Collection<String> getModules() {
      if (this.modules.isEmpty()) {
         this.modules.addAll(Modules.findLocalModules());
      }

      return this.modules;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final DependencyDataProviderFactory getModuleDataProviderFactory() {
      if (this.moduleDataProviderFactory == null) {
         this.moduleDataProviderFactory = new ExternalDependencyDataProviderFactory(DependencyReader.DEFAULT);
      }

      return this.moduleDataProviderFactory;
   }

   @Contract(
      mutates = "this"
   )
   @NotNull
   protected final PreResolutionDataProviderFactory getModulePreResolutionDataProviderFactory() {
      if (this.modulePreResolutionDataProviderFactory == null) {
         this.modulePreResolutionDataProviderFactory = new WrappingPreResolutionDataProviderFactory(PreResolutionDataReader.DEFAULT);
      }

      return this.modulePreResolutionDataProviderFactory;
   }

   @NotNull
   protected final Application buildApplication() {
      DependencyInjector var1 = this.createInjector();
      DependencyDataProvider var2 = this.getDataProviderFactory().create(this.getDependencyFileUrl());
      DependencyData var3 = var2.get();
      PreResolutionDataProvider var4 = this.getPreResolutionDataProviderFactory().create(this.getPreResolutionFileUrl());
      Map var5 = var4.get();
      URL[] var6 = Modules.extract((ModuleExtractor)(this.moduleExtractor == null ? new RelocatingModuleExtractor(this.getDownloadDirectoryPath().resolve("modules"), this.getRelocatorFactory().create(var3.relocations())) : this.moduleExtractor), this.getModules());
      Injectable var7 = (Injectable)this.injectableSupplier.apply((ModularApplicationBuilder)this.self);
      URL[] var8 = var6;
      int var9 = var6.length;

      int var10;
      URL var11;
      for(var10 = 0; var10 < var9; ++var10) {
         var11 = var8[var10];
         var7.inject(var11);
      }

      var1.inject(var7, var3, var5);
      var8 = var6;
      var9 = var6.length;

      for(var10 = 0; var10 < var9; ++var10) {
         var11 = var8[var10];
         DependencyDataProvider var12 = this.getModuleDataProviderFactory().create(var11);
         PreResolutionDataProvider var13 = this.getModulePreResolutionDataProviderFactory().create(var11);
         var1.inject(var7, var12.get(), var13.get());
      }

      return this.buildApplication(var7);
   }

   protected Application buildApplication(@NotNull Injectable var1) {
      return new AppendingApplication();
   }

   private static final class Impl extends ModularApplicationBuilder<ModularApplicationBuilder.Impl> {
      private Impl(@NotNull String var1, @NotNull Function<ModularApplicationBuilder.Impl, Injectable> var2) {
         super(var1, var2);
      }
   }
}
