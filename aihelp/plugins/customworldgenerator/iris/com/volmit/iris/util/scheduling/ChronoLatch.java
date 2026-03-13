package com.volmit.iris.util.scheduling;

public class ChronoLatch {
   private final long interval;
   private long since;

   public ChronoLatch(long interval, boolean openedAtStart) {
      this.interval = var1;
      this.since = System.currentTimeMillis() - (var3 ? var1 * 2L : 0L);
   }

   public ChronoLatch(long interval) {
      this(var1, true);
   }

   public void flipDown() {
      this.since = System.currentTimeMillis();
   }

   public boolean couldFlip() {
      return System.currentTimeMillis() - this.since > this.interval;
   }

   public boolean flip() {
      if (System.currentTimeMillis() - this.since > this.interval) {
         this.since = System.currentTimeMillis();
         return true;
      } else {
         return false;
      }
   }
}
