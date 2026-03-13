package com.volmit.iris.util.scheduling;

import com.volmit.iris.Iris;

public class QueueExecutor extends Looper {
   private final Queue<Runnable> queue = new ShurikenQueue();
   private boolean shutdown = false;

   public Queue<Runnable> queue() {
      return this.queue;
   }

   protected long loop() {
      while(this.queue.hasNext()) {
         try {
            ((Runnable)this.queue.next()).run();
         } catch (Throwable var2) {
            Iris.reportError(var2);
            var2.printStackTrace();
         }
      }

      if (this.shutdown && !this.queue.hasNext()) {
         this.interrupt();
         return -1L;
      } else {
         return Math.max(500L, (long)this.getRunTime() * 10L);
      }
   }

   public double getRunTime() {
      return 0.0D;
   }

   public void shutdown() {
      this.shutdown = true;
   }
}
