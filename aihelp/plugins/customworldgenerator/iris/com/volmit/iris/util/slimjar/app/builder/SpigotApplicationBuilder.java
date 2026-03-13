package com.volmit.iris.util.slimjar.app.builder;

import com.volmit.iris.util.slimjar.app.AppendingApplication;
import com.volmit.iris.util.slimjar.app.Application;
import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import com.volmit.iris.util.slimjar.injector.DependencyInjector;
import com.volmit.iris.util.slimjar.injector.loader.IsolatedInjectableClassLoader;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import com.volmit.iris.util.slimjar.resolver.reader.dependency.DependencyDataProvider;
import com.volmit.iris.util.slimjar.resolver.reader.resolution.PreResolutionDataProvider;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SpigotApplicationBuilder extends ApplicationBuilder<SpigotApplicationBuilder> {
   private final ClassLoader classLoader;
   private boolean debug = false;
   private boolean remap = false;

   public SpigotApplicationBuilder(@NotNull Plugin var1) {
      super(var1.getName());
      this.classLoader = var1.getClass().getClassLoader();
      final Logger var2 = var1.getLogger();
      this.downloadDirectoryPath((new File(var1.getDataFolder(), ".libs")).toPath());
      this.logger(new ProcessLogger() {
         public void info(@NotNull String var1, @Nullable Object... var2x) {
            if (SpigotApplicationBuilder.this.debug) {
               var2.info(var1.formatted(var2x));
            }
         }

         public void error(@NotNull String var1, @Nullable Object... var2x) {
            var2.severe(var1.formatted(var2x));
         }

         public void debug(@NotNull String var1, @Nullable Object... var2x) {
            if (SpigotApplicationBuilder.this.debug) {
               var2.info(var1.formatted(var2x));
            }
         }
      });
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   public SpigotApplicationBuilder debug(boolean var1) {
      this.debug = var1;
      return this;
   }

   @Contract(
      value = "_ -> this",
      mutates = "this"
   )
   public SpigotApplicationBuilder remap(boolean var1) {
      this.remap = var1;
      return this;
   }

   @NotNull
   protected Application buildApplication() {
      try {
         Field var1 = this.classLoader.getClass().getDeclaredField("libraryLoader");
         var1.setAccessible(true);
         ClassLoader var2 = (ClassLoader)var1.get(this.classLoader);
         Function var3 = null;
         BiFunction var4 = null;
         if (this.remap) {
            try {
               Class var5 = Class.forName("org.bukkit.plugin.java.LibraryLoader");
               Field var6 = var5.getDeclaredField("REMAPPER");
               Field var7 = var5.getDeclaredField("LIBRARY_LOADER_FACTORY");
               var6.setAccessible(true);
               var7.setAccessible(true);
               var3 = (Function)var6.get(var2);
               var4 = (BiFunction)var7.get(var2);
            } catch (Throwable var18) {
            }
         }

         if (var3 == null) {
            var3 = Function.identity();
         }

         if (var4 == null) {
            var4 = (var0, var1x) -> {
               return new IsolatedInjectableClassLoader(var0, Collections.emptyList(), var1x);
            };
         }

         ArrayList var20 = new ArrayList();
         DependencyDataProvider var21 = this.getDataProviderFactory().create(this.getDependencyFileUrl());
         DependencyData var22 = var21.get();
         DependencyInjector var8 = this.createInjector();
         PreResolutionDataProvider var9 = this.getPreResolutionDataProviderFactory().create(this.getPreResolutionFileUrl());
         Map var10 = var9.get();
         Objects.requireNonNull(var20);
         var8.inject(var20::add, var22, var10);
         ArrayList var11 = new ArrayList(var20.size());
         Iterator var12 = var20.iterator();

         while(var12.hasNext()) {
            URL var13 = (URL)var12.next();

            try {
               var11.add(Path.of(var13.toURI()));
            } catch (URISyntaxException var17) {
               throw new InjectorException("Failed to convert URL to path", var17);
            }
         }

         List var23 = (List)var3.apply(var11);
         URL[] var24 = new URL[var23.size()];

         for(int var14 = 0; var14 < var23.size(); ++var14) {
            try {
               var24[var14] = ((Path)var23.get(var14)).toUri().toURL();
            } catch (MalformedURLException var16) {
               throw new InjectorException("Failed to convert path to URL", var16);
            }
         }

         var1.set(this.classLoader, var4.apply(var24, var2 == null ? this.classLoader.getParent() : var2));
      } catch (Exception var19) {
         throw new InjectorException("Failed to build application", var19);
      }

      return new AppendingApplication();
   }
}
