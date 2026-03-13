package com.volmit.iris.util.scheduling;

public class PrecisionStopwatch {
   private long nanos;
   private long startNano;
   private long millis;
   private long startMillis;
   private double time;
   private boolean profiling;

   public PrecisionStopwatch() {
      this.reset();
      this.profiling = false;
   }

   public static PrecisionStopwatch start() {
      PrecisionStopwatch var0 = new PrecisionStopwatch();
      var0.begin();
      return var0;
   }

   public void begin() {
      this.profiling = true;
      this.startNano = System.nanoTime();
      this.startMillis = System.currentTimeMillis();
   }

   public void end() {
      if (this.profiling) {
         this.profiling = false;
         this.nanos = System.nanoTime() - this.startNano;
         this.millis = System.currentTimeMillis() - this.startMillis;
         this.time = (double)this.nanos / 1000000.0D;
         this.time = (double)this.millis - this.time > 1.01D ? (double)this.millis : this.time;
      }
   }

   public void reset() {
      this.nanos = -1L;
      this.millis = -1L;
      this.startNano = -1L;
      this.startMillis = -1L;
      this.time = 0.0D;
      this.profiling = false;
   }

   public double getTicks() {
      return this.getMilliseconds() / 50.0D;
   }

   public double getSeconds() {
      return this.getMilliseconds() / 1000.0D;
   }

   public double getMinutes() {
      return this.getSeconds() / 60.0D;
   }

   public double getHours() {
      return this.getMinutes() / 60.0D;
   }

   public double getMilliseconds() {
      this.nanos = System.nanoTime() - this.startNano;
      this.millis = System.currentTimeMillis() - this.startMillis;
      this.time = (double)this.nanos / 1000000.0D;
      this.time = (double)this.millis - this.time > 1.01D ? (double)this.millis : this.time;
      return this.time;
   }

   public long getNanoseconds() {
      return (long)(this.time * 1000000.0D);
   }

   public long getNanos() {
      return this.nanos;
   }

   public long getStartNano() {
      return this.startNano;
   }

   public long getMillis() {
      return this.millis;
   }

   public long getStartMillis() {
      return this.startMillis;
   }

   public double getTime() {
      return this.time;
   }

   public boolean isProfiling() {
      return this.profiling;
   }

   public void rewind(long l) {
      this.startMillis -= var1;
   }
}
