package org.apache.commons.lang3.time;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class StopWatch {
   private static final long NANO_2_MILLIS = 1000000L;
   private final String message;
   private StopWatch.State runningState;
   private StopWatch.SplitState splitState;
   private long startTimeNanos;
   private long startTimeMillis;
   private long stopTimeMillis;
   private long stopTimeNanos;

   public static StopWatch create() {
      return new StopWatch();
   }

   public static StopWatch createStarted() {
      StopWatch var0 = new StopWatch();
      var0.start();
      return var0;
   }

   public StopWatch() {
      this((String)null);
   }

   public StopWatch(String var1) {
      this.runningState = StopWatch.State.UNSTARTED;
      this.splitState = StopWatch.SplitState.UNSPLIT;
      this.message = var1;
   }

   public String formatSplitTime() {
      return DurationFormatUtils.formatDurationHMS(this.getSplitTime());
   }

   public String formatTime() {
      return DurationFormatUtils.formatDurationHMS(this.getTime());
   }

   public String getMessage() {
      return this.message;
   }

   public long getNanoTime() {
      if (this.runningState != StopWatch.State.STOPPED && this.runningState != StopWatch.State.SUSPENDED) {
         if (this.runningState == StopWatch.State.UNSTARTED) {
            return 0L;
         } else if (this.runningState == StopWatch.State.RUNNING) {
            return System.nanoTime() - this.startTimeNanos;
         } else {
            throw new IllegalStateException("Illegal running state has occurred.");
         }
      } else {
         return this.stopTimeNanos - this.startTimeNanos;
      }
   }

   public long getSplitNanoTime() {
      if (this.splitState != StopWatch.SplitState.SPLIT) {
         throw new IllegalStateException("Stopwatch must be split to get the split time.");
      } else {
         return this.stopTimeNanos - this.startTimeNanos;
      }
   }

   public long getSplitTime() {
      return this.getSplitNanoTime() / 1000000L;
   }

   public long getStartTime() {
      if (this.runningState == StopWatch.State.UNSTARTED) {
         throw new IllegalStateException("Stopwatch has not been started");
      } else {
         return this.startTimeMillis;
      }
   }

   public long getStopTime() {
      if (this.runningState == StopWatch.State.UNSTARTED) {
         throw new IllegalStateException("Stopwatch has not been started");
      } else {
         return this.stopTimeMillis;
      }
   }

   public long getTime() {
      return this.getNanoTime() / 1000000L;
   }

   public long getTime(TimeUnit var1) {
      return var1.convert(this.getNanoTime(), TimeUnit.NANOSECONDS);
   }

   public boolean isStarted() {
      return this.runningState.isStarted();
   }

   public boolean isStopped() {
      return this.runningState.isStopped();
   }

   public boolean isSuspended() {
      return this.runningState.isSuspended();
   }

   public void reset() {
      this.runningState = StopWatch.State.UNSTARTED;
      this.splitState = StopWatch.SplitState.UNSPLIT;
   }

   public void resume() {
      if (this.runningState != StopWatch.State.SUSPENDED) {
         throw new IllegalStateException("Stopwatch must be suspended to resume. ");
      } else {
         this.startTimeNanos += System.nanoTime() - this.stopTimeNanos;
         this.runningState = StopWatch.State.RUNNING;
      }
   }

   public void split() {
      if (this.runningState != StopWatch.State.RUNNING) {
         throw new IllegalStateException("Stopwatch is not running. ");
      } else {
         this.stopTimeNanos = System.nanoTime();
         this.splitState = StopWatch.SplitState.SPLIT;
      }
   }

   public void start() {
      if (this.runningState == StopWatch.State.STOPPED) {
         throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
      } else if (this.runningState != StopWatch.State.UNSTARTED) {
         throw new IllegalStateException("Stopwatch already started. ");
      } else {
         this.startTimeNanos = System.nanoTime();
         this.startTimeMillis = System.currentTimeMillis();
         this.runningState = StopWatch.State.RUNNING;
      }
   }

   public void stop() {
      if (this.runningState != StopWatch.State.RUNNING && this.runningState != StopWatch.State.SUSPENDED) {
         throw new IllegalStateException("Stopwatch is not running. ");
      } else {
         if (this.runningState == StopWatch.State.RUNNING) {
            this.stopTimeNanos = System.nanoTime();
            this.stopTimeMillis = System.currentTimeMillis();
         }

         this.runningState = StopWatch.State.STOPPED;
      }
   }

   public void suspend() {
      if (this.runningState != StopWatch.State.RUNNING) {
         throw new IllegalStateException("Stopwatch must be running to suspend. ");
      } else {
         this.stopTimeNanos = System.nanoTime();
         this.stopTimeMillis = System.currentTimeMillis();
         this.runningState = StopWatch.State.SUSPENDED;
      }
   }

   public String toSplitString() {
      String var1 = Objects.toString(this.message, "");
      String var2 = this.formatSplitTime();
      return var1.isEmpty() ? var2 : var1 + " " + var2;
   }

   public String toString() {
      String var1 = Objects.toString(this.message, "");
      String var2 = this.formatTime();
      return var1.isEmpty() ? var2 : var1 + " " + var2;
   }

   public void unsplit() {
      if (this.splitState != StopWatch.SplitState.SPLIT) {
         throw new IllegalStateException("Stopwatch has not been split. ");
      } else {
         this.splitState = StopWatch.SplitState.UNSPLIT;
      }
   }

   private static enum State {
      RUNNING {
         boolean isStarted() {
            return true;
         }

         boolean isStopped() {
            return false;
         }

         boolean isSuspended() {
            return false;
         }
      },
      STOPPED {
         boolean isStarted() {
            return false;
         }

         boolean isStopped() {
            return true;
         }

         boolean isSuspended() {
            return false;
         }
      },
      SUSPENDED {
         boolean isStarted() {
            return true;
         }

         boolean isStopped() {
            return false;
         }

         boolean isSuspended() {
            return true;
         }
      },
      UNSTARTED {
         boolean isStarted() {
            return false;
         }

         boolean isStopped() {
            return true;
         }

         boolean isSuspended() {
            return false;
         }
      };

      private State() {
      }

      abstract boolean isStarted();

      abstract boolean isStopped();

      abstract boolean isSuspended();

      // $FF: synthetic method
      State(Object var3) {
         this();
      }
   }

   private static enum SplitState {
      SPLIT,
      UNSPLIT;
   }
}
