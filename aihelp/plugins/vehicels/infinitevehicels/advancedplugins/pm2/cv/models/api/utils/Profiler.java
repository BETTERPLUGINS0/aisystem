package advancedplugins.pm2.cv.models.api.utils;

import java.util.LinkedList;
import java.util.Queue;

public class Profiler {
   private final Queue<Long> times;
   private final int maxSize;
   private long startTime;

   public Profiler(int var1) {
      this.maxSize = var1;
      this.times = new LinkedList();
   }

   public void startProfiling() {
      this.startTime = System.nanoTime();
   }

   public void stopProfiling() {
      if (this.startTime == 0L) {
         throw new IllegalStateException("Profiling has not been started.");
      } else {
         long var1 = System.nanoTime();
         long var3 = var1 - this.startTime;
         this.addTime(var3);
         this.startTime = 0L;
      }
   }

   private void addTime(long var1) {
      if (this.times.size() >= this.maxSize) {
         this.times.poll();
      }

      this.times.offer(var1);
   }

   public long getMinTime() {
      return (Long)this.times.stream().min(Long::compare).orElse(0L);
   }

   public long getMaxTime() {
      return (Long)this.times.stream().max(Long::compare).orElse(0L);
   }

   public double getAverageTime() {
      return this.times.stream().mapToLong(Long::longValue).average().orElse(0.0D);
   }

   public int getSampleSize() {
      return this.times.size();
   }
}
