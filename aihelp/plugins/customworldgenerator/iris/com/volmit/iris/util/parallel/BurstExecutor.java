package com.volmit.iris.util.parallel;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import lombok.Generated;

public class BurstExecutor {
   private final ExecutorService executor;
   private final KList<Future<?>> futures;
   private boolean multicore = true;

   public BurstExecutor(ExecutorService executor, int burstSizeEstimate) {
      this.executor = var1;
      this.futures = new KList(var2);
   }

   public Future<?> queue(Runnable r) {
      if (!this.multicore) {
         var1.run();
         return CompletableFuture.completedFuture((Object)null);
      } else {
         synchronized(this.futures) {
            Future var3 = this.executor.submit(var1);
            this.futures.add((Object)var3);
            return var3;
         }
      }
   }

   public BurstExecutor queue(List<Runnable> r) {
      if (!this.multicore) {
         Iterator var2 = (new KList(var1)).iterator();

         while(var2.hasNext()) {
            Runnable var7 = (Runnable)var2.next();
            var7.run();
         }

         return this;
      } else {
         synchronized(this.futures) {
            Iterator var3 = (new KList(var1)).iterator();

            while(var3.hasNext()) {
               Runnable var4 = (Runnable)var3.next();
               this.queue(var4);
            }

            return this;
         }
      }
   }

   public BurstExecutor queue(Runnable[] r) {
      if (!this.multicore) {
         Iterator var2 = (new KList(var1)).iterator();

         while(var2.hasNext()) {
            Runnable var9 = (Runnable)var2.next();
            var9.run();
         }

         return this;
      } else {
         synchronized(this.futures) {
            Runnable[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Runnable var6 = var3[var5];
               this.queue(var6);
            }

            return this;
         }
      }
   }

   public void complete() {
      if (this.multicore) {
         synchronized(this.futures) {
            if (!this.futures.isEmpty()) {
               try {
                  Iterator var2 = this.futures.iterator();

                  while(var2.hasNext()) {
                     Future var3 = (Future)var2.next();
                     var3.get();
                  }

                  this.futures.clear();
               } catch (ExecutionException | InterruptedException var5) {
                  Iris.reportError(var5);
               }

            }
         }
      }
   }

   @Generated
   public KList<Future<?>> getFutures() {
      return this.futures;
   }

   @Generated
   public void setMulticore(final boolean multicore) {
      this.multicore = var1;
   }
}
