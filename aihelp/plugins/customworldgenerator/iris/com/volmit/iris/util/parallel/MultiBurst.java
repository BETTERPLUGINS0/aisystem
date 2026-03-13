package com.volmit.iris.util.parallel;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.ExecutorsKt;
import org.jetbrains.annotations.NotNull;

public class MultiBurst implements ExecutorService {
   private static final long TIMEOUT = Long.getLong("iris.burst.timeout", 15000L);
   public static final MultiBurst burst = new MultiBurst();
   public static final MultiBurst ioBurst = new MultiBurst("Iris IO", () -> {
      return IrisSettings.get().getConcurrency().getIoParallelism();
   });
   private final AtomicLong last;
   private final String name;
   private final int priority;
   private final IntSupplier parallelism;
   private final Object lock;
   private volatile ExecutorService service;
   private volatile CoroutineDispatcher dispatcher;

   public MultiBurst() {
      this("Iris");
   }

   public MultiBurst(String name) {
      this(var1, 1, () -> {
         return IrisSettings.get().getConcurrency().getParallelism();
      });
   }

   public MultiBurst(String name, IntSupplier parallelism) {
      this(var1, 1, var2);
   }

   public MultiBurst(String name, int priority, IntSupplier parallelism) {
      this.lock = new Object();
      this.name = var1;
      this.priority = var2;
      this.parallelism = var3;
      this.last = new AtomicLong(M.ms());
   }

   private ExecutorService getService() {
      this.last.set(M.ms());
      if (this.service != null && !this.service.isShutdown()) {
         return this.service;
      } else {
         synchronized(this.lock) {
            if (this.service != null && !this.service.isShutdown()) {
               return this.service;
            } else {
               this.service = new ForkJoinPool(IrisSettings.getThreadCount(this.parallelism.getAsInt()), new ForkJoinWorkerThreadFactory() {
                  int m = 0;

                  public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                     ForkJoinWorkerThread var2 = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(var1);
                     var2.setPriority(MultiBurst.this.priority);
                     var2.setName(MultiBurst.this.name + " " + ++this.m);
                     return var2;
                  }
               }, (var0, var1) -> {
                  var1.printStackTrace();
               }, true);
               this.dispatcher = ExecutorsKt.from(this.service);
               return this.service;
            }
         }
      }
   }

   public CoroutineDispatcher getDispatcher() {
      this.getService();
      return this.dispatcher;
   }

   public void burst(Runnable... r) {
      this.burst(var1.length).queue(var1).complete();
   }

   public void burst(boolean multicore, Runnable... r) {
      if (var1) {
         this.burst(var2);
      } else {
         this.sync(var2);
      }

   }

   public void burst(List<Runnable> r) {
      this.burst(var1.size()).queue(var1).complete();
   }

   public void burst(boolean multicore, List<Runnable> r) {
      if (var1) {
         this.burst(var2);
      } else {
         this.sync(var2);
      }

   }

   private void sync(List<Runnable> r) {
      Iterator var2 = (new KList(var1)).iterator();

      while(var2.hasNext()) {
         Runnable var3 = (Runnable)var2.next();
         var3.run();
      }

   }

   public void sync(Runnable... r) {
      Runnable[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Runnable var5 = var2[var4];
         var5.run();
      }

   }

   public void sync(KList<Runnable> r) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Runnable var3 = (Runnable)var2.next();
         var3.run();
      }

   }

   public BurstExecutor burst(int estimate) {
      return new BurstExecutor(this.getService(), var1);
   }

   public BurstExecutor burst() {
      return this.burst(16);
   }

   public BurstExecutor burst(boolean multicore) {
      BurstExecutor var2 = this.burst();
      var2.setMulticore(var1);
      return var2;
   }

   public <T> Future<T> lazySubmit(Callable<T> o) {
      return this.getService().submit(var1);
   }

   public void lazy(Runnable o) {
      this.getService().execute(var1);
   }

   public Future<?> future(Runnable o) {
      return this.getService().submit(var1);
   }

   public Future<?> complete(Runnable o) {
      return this.getService().submit(var1);
   }

   public <T> Future<T> completeValue(Callable<T> o) {
      return this.getService().submit(var1);
   }

   public <T> CompletableFuture<T> completableFuture(Callable<T> o) {
      CompletableFuture var2 = new CompletableFuture();
      this.getService().submit(() -> {
         try {
            var2.complete(var1.call());
         } catch (Exception var3) {
            var2.completeExceptionally(var3);
         }

      });
      return var2;
   }

   public void shutdown() {
      this.close();
   }

   @NotNull
   public List<Runnable> shutdownNow() {
      this.close();
      return List.of();
   }

   public boolean isShutdown() {
      return this.service == null || this.service.isShutdown();
   }

   public boolean isTerminated() {
      return this.service == null || this.service.isTerminated();
   }

   public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) {
      return this.service == null || this.service.awaitTermination(var1, var3);
   }

   public void execute(@NotNull Runnable command) {
      this.getService().execute(var1);
   }

   @NotNull
   public <T> Future<T> submit(@NotNull Callable<T> task) {
      return this.getService().submit(var1);
   }

   @NotNull
   public <T> Future<T> submit(@NotNull Runnable task, T result) {
      return this.getService().submit(var1, var2);
   }

   @NotNull
   public Future<?> submit(@NotNull Runnable task) {
      return this.getService().submit(var1);
   }

   @NotNull
   public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) {
      return this.getService().invokeAll(var1);
   }

   @NotNull
   public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) {
      return this.getService().invokeAll(var1, var2, var4);
   }

   @NotNull
   public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) {
      return this.getService().invokeAny(var1);
   }

   public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) {
      return this.getService().invokeAny(var1, var2, var4);
   }

   public void close() {
      if (this.service != null) {
         close(this.service);
      }

   }

   public static void close(ExecutorService service) {
      var0.shutdown();
      PrecisionStopwatch var1 = PrecisionStopwatch.start();

      try {
         while(!var0.awaitTermination(1L, TimeUnit.SECONDS)) {
            Iris.info("Still waiting to shutdown burster...");
            if (var1.getMilliseconds() > (double)TIMEOUT) {
               Iris.warn("Forcing Shutdown...");

               try {
                  var0.shutdownNow();
               } catch (Throwable var3) {
               }
               break;
            }
         }
      } catch (Throwable var4) {
         var4.printStackTrace();
         Iris.reportError(var4);
      }

   }
}
