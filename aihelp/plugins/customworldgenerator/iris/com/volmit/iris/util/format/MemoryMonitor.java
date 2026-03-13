package com.volmit.iris.util.format;

import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.Looper;

public class MemoryMonitor {
   private final ChronoLatch cl;
   private final RollingSequence pressureAvg;
   private final Runtime runtime = Runtime.getRuntime();
   private Looper looper;
   private long usedMemory = -1L;
   private long garbageMemory;
   private long garbageLast;
   private long garbageBin;
   private long pressure;

   public MemoryMonitor(int sampleDelay) {
      this.pressureAvg = new RollingSequence(Math.max(Math.min(100, 1000 / var1), 3));
      this.garbageBin = 0L;
      this.garbageMemory = -1L;
      this.cl = new ChronoLatch(1000L);
      this.garbageLast = 0L;
      this.pressure = 0L;
      this.looper = new Looper() {
         protected long loop() {
            MemoryMonitor.this.sample();
            return (long)var1;
         }
      };
      this.looper.setPriority(1);
      this.looper.setName("Memory Monitor");
      this.looper.start();
   }

   public long getGarbageBytes() {
      return this.garbageMemory;
   }

   public long getUsedBytes() {
      return this.usedMemory;
   }

   public long getMaxBytes() {
      return this.runtime.maxMemory();
   }

   public long getPressure() {
      return (long)this.pressureAvg.getAverage();
   }

   public double getUsagePercent() {
      return (double)this.usedMemory / (double)this.getMaxBytes();
   }

   private void sample() {
      long var1 = this.getVMUse();
      if (this.usedMemory == -1L) {
         this.usedMemory = var1;
         this.garbageMemory = 0L;
      } else {
         if (var1 < this.usedMemory) {
            this.usedMemory = var1;
         } else {
            this.garbageMemory = var1 - this.usedMemory;
         }

         long var3 = this.garbageMemory - this.garbageLast;
         if (var3 >= 0L) {
            this.garbageBin += var3;
            this.garbageLast = this.garbageMemory;
         } else {
            this.garbageMemory = 0L;
            this.garbageLast = 0L;
         }

         if (this.cl.flip()) {
            if (this.garbageMemory > 0L) {
               this.pressure = this.garbageBin;
               this.garbageBin = 0L;
            } else {
               this.pressure = 0L;
               this.garbageBin = 0L;
            }
         }

         this.pressureAvg.put((double)this.pressure);
      }
   }

   private long getVMUse() {
      return this.runtime.totalMemory() - this.runtime.freeMemory();
   }

   public void close() {
      if (this.looper != null) {
         this.looper.interrupt();
         this.looper = null;
      }

   }
}
