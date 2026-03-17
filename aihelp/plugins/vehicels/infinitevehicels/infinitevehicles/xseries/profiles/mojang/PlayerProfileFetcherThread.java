package me.PM2.infinitevehicles.xseries.profiles.mojang;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import me.PM2.infinitevehicles.xseries.profiles.ProfileLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class PlayerProfileFetcherThread implements ThreadFactory {
   public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10, new PlayerProfileFetcherThread());
   private static final AtomicInteger COUNT = new AtomicInteger();

   public Thread newThread(@NotNull Runnable var1) {
      Thread var2 = new Thread(var1);
      var2.setName("Profile Lookup Executor #" + COUNT.getAndIncrement());
      var2.setUncaughtExceptionHandler((var0, var1x) -> {
         ProfileLogger.LOGGER.error("Uncaught exception in thread {}", var0.getName(), var1x);
      });
      return var2;
   }
}
