package com.volmit.iris.util.scheduling;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.function.NastyRunnable;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;

public class GroupedExecutor {
   private final ExecutorService service;
   private final KMap<String, Integer> mirror = new KMap();
   private int xc = 1;

   public GroupedExecutor(int threadLimit, int priority, String name) {
      if (var1 == 1) {
         this.service = Executors.newSingleThreadExecutor((var2x) -> {
            Thread var3x = new Thread(var2x);
            var3x.setName(var3);
            var3x.setPriority(var2);
            return var3x;
         });
      } else if (var1 > 1) {
         ForkJoinWorkerThreadFactory var4 = (var3x) -> {
            ForkJoinWorkerThread var4 = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(var3x);
            var4.setName(var3 + " " + this.xc++);
            var4.setPriority(var2);
            return var4;
         };
         this.service = new ForkJoinPool(var1, var4, (UncaughtExceptionHandler)null, false);
      } else {
         this.service = Executors.newCachedThreadPool((var3x) -> {
            Thread var4 = new Thread(var3x);
            var4.setName(var3 + " " + this.xc++);
            var4.setPriority(var2);
            return var4;
         });
      }

   }

   public void waitFor(String g) {
      if (var1 != null) {
         if (this.mirror.containsKey(var1)) {
            while((Integer)this.mirror.get(var1) != 0) {
            }

         }
      }
   }

   public void queue(String q, NastyRunnable r) {
      this.mirror.compute(var1, (var0, var1x) -> {
         return var0 != null && var1x != null ? var1x + 1 : 1;
      });
      this.service.execute(() -> {
         try {
            var2.run();
         } catch (Throwable var4) {
            Iris.reportError(var4);
            var4.printStackTrace();
         }

         this.mirror.computeIfPresent(var1, (var0, var1x) -> {
            return var1x - 1;
         });
      });
   }

   public void close() {
      J.a(() -> {
         J.sleep(100L);
         this.service.shutdown();
      });
   }

   public void closeNow() {
      this.service.shutdown();
   }
}
