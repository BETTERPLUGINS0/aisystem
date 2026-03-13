package com.nisovin.shopkeepers.util.timer;

import com.nisovin.shopkeepers.util.java.TimeUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.concurrent.TimeUnit;

public class Timer implements Timings {
   private long counter = 0L;
   private long totalTimeNanos = 0L;
   private long maxTimeNanos = 0L;
   private boolean started = false;
   private boolean paused = false;
   private long startTimeNanos;
   private long elapsedTimeNanos;
   private boolean stateErrorEncountered = false;

   private void validateState(boolean expectedStated) {
      if (!this.stateErrorEncountered) {
         if (!expectedStated) {
            this.stateErrorEncountered = true;
            Log.severe((String)("Unexpected timer state! Timings might be wrong. started=" + this.started + ", paused=" + this.paused), (Throwable)(new IllegalStateException()));
         }

      }
   }

   public void start() {
      this.validateState(!this.started && !this.paused);
      this.started = true;
      this.elapsedTimeNanos = 0L;
      this.startTimeNanos = System.nanoTime();
   }

   public void startPaused() {
      this.start();
      this.pause();
   }

   public void pause() {
      this.validateState(this.started && !this.paused);
      this.paused = true;
      this.elapsedTimeNanos += System.nanoTime() - this.startTimeNanos;
   }

   public void resume() {
      this.validateState(this.started && this.paused);
      this.paused = false;
      this.startTimeNanos = System.nanoTime();
   }

   public void stop() {
      this.validateState(this.started);
      if (!this.paused) {
         this.pause();
      }

      assert this.paused;

      this.started = false;
      this.paused = false;
      ++this.counter;
      this.totalTimeNanos += this.elapsedTimeNanos;
      if (this.elapsedTimeNanos > this.maxTimeNanos) {
         this.maxTimeNanos = this.elapsedTimeNanos;
      }

   }

   public void reset() {
      this.counter = 0L;
      this.totalTimeNanos = 0L;
      this.maxTimeNanos = 0L;
   }

   public long getCounter() {
      return this.counter;
   }

   public double getAverageTimeMillis() {
      double avgTimeNanos = (double)this.totalTimeNanos / (double)(this.counter == 0L ? 1L : this.counter);
      return TimeUtils.convert(avgTimeNanos, TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
   }

   public double getMaxTimeMillis() {
      return TimeUtils.convert((double)this.maxTimeNanos, TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
   }
}
