package com.volmit.iris.util.parallel;

import com.volmit.iris.util.math.M;
import com.volmit.iris.util.scheduling.SR;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;

public class SyncExecutor implements Executor, AutoCloseable {
   private final CountDownLatch latch = new CountDownLatch(1);
   private final Queue<Runnable> queue = new ConcurrentLinkedQueue();
   private final AtomicBoolean closed = new AtomicBoolean(false);

   public SyncExecutor(int msPerTick) {
      SR var10001 = new SR() {
         public void run() {
            long var1x = M.ms() + (long)var1;

            while(var1x > M.ms()) {
               Runnable var3 = (Runnable)SyncExecutor.this.queue.poll();
               if (var3 == null) {
                  break;
               }

               var3.run();
            }

            if (SyncExecutor.this.closed.get() && SyncExecutor.this.queue.isEmpty()) {
               this.cancel();
               SyncExecutor.this.latch.countDown();
            }

         }
      };
   }

   public void execute(@NotNull Runnable command) {
      if (this.closed.get()) {
         throw new IllegalStateException("Executor is closed!");
      } else {
         this.queue.add(var1);
      }
   }

   public void close() {
      this.closed.set(true);
      this.latch.await();
   }
}
