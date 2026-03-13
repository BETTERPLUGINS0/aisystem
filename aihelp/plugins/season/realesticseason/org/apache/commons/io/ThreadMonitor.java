package org.apache.commons.io;

import java.time.Duration;

class ThreadMonitor implements Runnable {
   private final Thread thread;
   private final Duration timeout;

   static Thread start(Duration var0) {
      return start(Thread.currentThread(), var0);
   }

   static Thread start(Thread var0, Duration var1) {
      if (!var1.isZero() && !var1.isNegative()) {
         ThreadMonitor var2 = new ThreadMonitor(var0, var1);
         Thread var3 = new Thread(var2, ThreadMonitor.class.getSimpleName());
         var3.setDaemon(true);
         var3.start();
         return var3;
      } else {
         return null;
      }
   }

   static void stop(Thread var0) {
      if (var0 != null) {
         var0.interrupt();
      }

   }

   private ThreadMonitor(Thread var1, Duration var2) {
      this.thread = var1;
      this.timeout = var2;
   }

   public void run() {
      try {
         sleep(this.timeout);
         this.thread.interrupt();
      } catch (InterruptedException var2) {
      }

   }

   private static void sleep(Duration var0) {
      long var1 = var0.toMillis();
      long var3 = System.currentTimeMillis() + var1;
      long var5 = var1;

      do {
         Thread.sleep(var5);
         var5 = var3 - System.currentTimeMillis();
      } while(var5 > 0L);

   }
}
