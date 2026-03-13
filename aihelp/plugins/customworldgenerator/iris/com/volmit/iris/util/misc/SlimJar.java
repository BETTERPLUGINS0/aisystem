package com.volmit.iris.util.misc;

import com.volmit.iris.Iris;
import com.volmit.iris.util.slimjar.app.builder.ApplicationBuilder;
import com.volmit.iris.util.slimjar.app.builder.InjectingApplicationBuilder;
import com.volmit.iris.util.slimjar.app.builder.SpigotApplicationBuilder;
import com.volmit.iris.util.slimjar.injector.loader.factory.InjectableFactory;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlimJar {
   private static final boolean DEBUG = Boolean.getBoolean("iris.debug-slimjar");
   private static final boolean DISABLE_REMAPPER = Boolean.getBoolean("iris.disable-remapper");
   private static final ReentrantLock lock = new ReentrantLock();
   private static final AtomicBoolean loaded = new AtomicBoolean();

   public static void load() {
      if (!loaded.get()) {
         lock.lock();

         try {
            if (loaded.getAndSet(true)) {
               return;
            }

            Path var0 = Iris.instance.getDataFolder(new String[]{"cache", "libraries"}).toPath();
            Logger var1 = Iris.instance.getLogger();
            var1.info("Loading libraries...");

            try {
               ((SpigotApplicationBuilder)(new SpigotApplicationBuilder(Iris.instance)).downloadDirectoryPath(var0)).debug(DEBUG).remap(!DISABLE_REMAPPER).build();
            } catch (Throwable var6) {
               Iris.warn("Failed to inject the library loader, falling back to application builder");
               ((InjectingApplicationBuilder)((InjectingApplicationBuilder)ApplicationBuilder.appending(Iris.instance.getName()).injectableFactory(InjectableFactory.selecting(InjectableFactory.ERROR, InjectableFactory.INJECTABLE, InjectableFactory.WRAPPED, InjectableFactory.UNSAFE)).downloadDirectoryPath(var0)).logger(new ProcessLogger() {
                  public void info(@NotNull String message, @Nullable Object... args) {
                     if (SlimJar.DEBUG) {
                        Iris.instance.getLogger().info(var1.formatted(var2));
                     }
                  }

                  public void error(@NotNull String message, @Nullable Object... args) {
                     Iris.instance.getLogger().severe(var1.formatted(var2));
                  }

                  public void debug(@NotNull String message, @Nullable Object... args) {
                     if (SlimJar.DEBUG) {
                        Iris.instance.getLogger().info(var1.formatted(var2));
                     }
                  }
               })).build();
            }

            var1.info("Libraries loaded successfully!");
         } finally {
            lock.unlock();
         }

      }
   }
}
