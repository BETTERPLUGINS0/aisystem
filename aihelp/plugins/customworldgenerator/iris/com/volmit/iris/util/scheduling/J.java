package com.volmit.iris.util.scheduling;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.function.NastyFunction;
import com.volmit.iris.util.function.NastyFuture;
import com.volmit.iris.util.function.NastyRunnable;
import com.volmit.iris.util.function.NastySupplier;
import com.volmit.iris.util.math.FinalInteger;
import com.volmit.iris.util.parallel.MultiBurst;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.bukkit.Bukkit;

public class J {
   private static int tid = 0;
   private static KList<Runnable> afterStartup = new KList();
   private static KList<Runnable> afterStartupAsync = new KList();
   private static boolean started = false;

   public static void dofor(int a, Function<Integer, Boolean> c, int ch, Consumer<Integer> d) {
      for(int var4 = var0; (Boolean)var1.apply(var4); var4 += var2) {
         var1.apply(var4);
      }

   }

   public static boolean doif(Supplier<Boolean> c, Runnable g) {
      try {
         if ((Boolean)var0.get()) {
            var1.run();
            return true;
         } else {
            return false;
         }
      } catch (NullPointerException var3) {
         Iris.reportError(var3);
         return false;
      }
   }

   public static void arun(Runnable a) {
      MultiBurst.burst.lazy(() -> {
         try {
            var0.run();
         } catch (Throwable var2) {
            Iris.reportError(var2);
            Iris.error("Failed to run async task");
            var2.printStackTrace();
         }

      });
   }

   public static void a(Runnable a) {
      MultiBurst.burst.lazy(() -> {
         try {
            var0.run();
         } catch (Throwable var2) {
            Iris.reportError(var2);
            Iris.error("Failed to run async task");
            var2.printStackTrace();
         }

      });
   }

   public static void aBukkit(Runnable a) {
      if (Bukkit.getPluginManager().isPluginEnabled(Iris.instance)) {
         Bukkit.getScheduler().scheduleAsyncDelayedTask(Iris.instance, var0);
      }
   }

   public static <T> Future<T> a(Callable<T> a) {
      return MultiBurst.burst.lazySubmit(var0);
   }

   public static void attemptAsync(NastyRunnable r) {
      a(() -> {
         return attempt(var0);
      });
   }

   public static <R> R attemptResult(NastyFuture<R> r, R onError) {
      try {
         return var0.run();
      } catch (Throwable var3) {
         Iris.reportError(var3);
         return var1;
      }
   }

   public static <T, R> R attemptFunction(NastyFunction<T, R> r, T param, R onError) {
      try {
         return var0.run(var1);
      } catch (Throwable var4) {
         Iris.reportError(var4);
         return var2;
      }
   }

   public static boolean sleep(long ms) {
      return attempt(() -> {
         Thread.sleep(var0);
      });
   }

   public static boolean attempt(NastyRunnable r) {
      return attemptCatch(var0) == null;
   }

   public static <T> T attemptResult(NastySupplier<T> r) {
      try {
         return var0.get();
      } catch (Throwable var2) {
         return null;
      }
   }

   public static Throwable attemptCatch(NastyRunnable r) {
      try {
         var0.run();
         return null;
      } catch (Throwable var2) {
         return var2;
      }
   }

   public static <T> T attempt(Supplier<T> t, T i) {
      try {
         return var0.get();
      } catch (Throwable var3) {
         Iris.reportError(var3);
         return var1;
      }
   }

   public static void executeAfterStartupQueue() {
      if (!started) {
         started = true;
         Iterator var0 = afterStartup.iterator();

         Runnable var1;
         while(var0.hasNext()) {
            var1 = (Runnable)var0.next();
            s(var1);
         }

         var0 = afterStartupAsync.iterator();

         while(var0.hasNext()) {
            var1 = (Runnable)var0.next();
            a(var1);
         }

         afterStartup = null;
         afterStartupAsync = null;
      }
   }

   public static void ass(Runnable r) {
      if (started) {
         s(var0);
      } else {
         afterStartup.add((Object)var0);
      }

   }

   public static void asa(Runnable r) {
      if (started) {
         a(var0);
      } else {
         afterStartupAsync.add((Object)var0);
      }

   }

   public static void s(Runnable r) {
      if (Bukkit.getPluginManager().isPluginEnabled(Iris.instance)) {
         Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, var0);
      }
   }

   public static CompletableFuture sfut(Runnable r) {
      CompletableFuture var1 = new CompletableFuture();
      if (!Bukkit.getPluginManager().isPluginEnabled(Iris.instance)) {
         return null;
      } else {
         Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, () -> {
            var0.run();
            var1.complete((Object)null);
         });
         return var1;
      }
   }

   public static <T> CompletableFuture<T> sfut(Supplier<T> r) {
      CompletableFuture var1 = new CompletableFuture();
      if (!Bukkit.getPluginManager().isPluginEnabled(Iris.instance)) {
         return null;
      } else {
         Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, () -> {
            try {
               var1.complete(var0.get());
            } catch (Throwable var3) {
               var1.completeExceptionally(var3);
            }

         });
         return var1;
      }
   }

   public static CompletableFuture sfut(Runnable r, int delay) {
      CompletableFuture var2 = new CompletableFuture();
      if (!Bukkit.getPluginManager().isPluginEnabled(Iris.instance)) {
         return null;
      } else {
         Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, () -> {
            var0.run();
            var2.complete((Object)null);
         }, (long)var1);
         return var2;
      }
   }

   public static CompletableFuture afut(Runnable r) {
      CompletableFuture var1 = new CompletableFuture();
      a(() -> {
         var0.run();
         var1.complete((Object)null);
      });
      return var1;
   }

   public static void s(Runnable r, int delay) {
      try {
         if (!Bukkit.getPluginManager().isPluginEnabled(Iris.instance)) {
            return;
         }

         Bukkit.getScheduler().scheduleSyncDelayedTask(Iris.instance, var0, (long)var1);
      } catch (Throwable var3) {
         Iris.reportError(var3);
      }

   }

   public static void csr(int id) {
      Bukkit.getScheduler().cancelTask(var0);
   }

   public static int sr(Runnable r, int interval) {
      return !Bukkit.getPluginManager().isPluginEnabled(Iris.instance) ? -1 : Bukkit.getScheduler().scheduleSyncRepeatingTask(Iris.instance, var0, 0L, (long)var1);
   }

   public static void sr(Runnable r, int interval, int intervals) {
      final FinalInteger var3 = new FinalInteger(0);
      SR var10001 = new SR() {
         public void run() {
            var3.add(1);
            var0.run();
            if ((Integer)var3.get() >= var2) {
               this.cancel();
            }

         }
      };
   }

   public static void a(Runnable r, int delay) {
      if (Bukkit.getPluginManager().isPluginEnabled(Iris.instance)) {
         Bukkit.getScheduler().scheduleAsyncDelayedTask(Iris.instance, var0, (long)var1);
      }

   }

   public static void car(int id) {
      Bukkit.getScheduler().cancelTask(var0);
   }

   public static int ar(Runnable r, int interval) {
      return !Bukkit.getPluginManager().isPluginEnabled(Iris.instance) ? -1 : Bukkit.getScheduler().scheduleAsyncRepeatingTask(Iris.instance, var0, 0L, (long)var1);
   }

   public static void ar(Runnable r, int interval, int intervals) {
      final FinalInteger var3 = new FinalInteger(0);
      AR var10001 = new AR() {
         public void run() {
            var3.add(1);
            var0.run();
            if ((Integer)var3.get() >= var2) {
               this.cancel();
            }

         }
      };
   }
}
