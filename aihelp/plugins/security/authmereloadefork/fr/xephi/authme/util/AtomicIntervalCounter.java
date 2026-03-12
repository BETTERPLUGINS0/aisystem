package fr.xephi.authme.util;

public class AtomicIntervalCounter {
   private final int threshold;
   private final int interval;
   private int count;
   private long lastInsert;

   public AtomicIntervalCounter(int threshold, int interval) {
      this.threshold = threshold;
      this.interval = interval;
      this.reset();
   }

   public synchronized void reset() {
      this.count = 0;
      this.lastInsert = 0L;
   }

   public synchronized boolean handle() {
      long now = System.currentTimeMillis();
      if (now - this.lastInsert > (long)this.interval) {
         this.count = 1;
      } else {
         ++this.count;
      }

      if (this.count > this.threshold) {
         this.reset();
         return true;
      } else {
         this.lastInsert = now;
         return false;
      }
   }
}
