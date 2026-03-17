package com.bergerkiller.bukkit.tc.attachments.control.effect;

import java.util.Collection;
import java.util.function.Predicate;

public interface EffectLoop {
   EffectLoop NONE = new EffectLoop() {
      public boolean advance(EffectLoop.Time dt, EffectLoop.Time duration, boolean loop) {
         return false;
      }

      public void resetToBeginning() {
      }
   };

   boolean advance(EffectLoop.Time var1, EffectLoop.Time var2, boolean var3);

   default void resetToBeginning() {
   }

   default EffectLoop withAdvance(EffectLoop.AdvanceModifier modifier) {
      return new EffectLoopAdvanceModifier(this, modifier);
   }

   default EffectLoop withConditionalAdvance(Predicate<EffectLoop> check) {
      return this.withAdvance((base, dt, duration, loop) -> {
         return check.test(base) && base.advance(dt, duration, loop);
      });
   }

   default EffectLoop withSpeed(double speed) {
      if (speed < 1.0E-8D) {
         return NONE;
      } else {
         return speed == 1.0D ? this : this.withAdvance((base, dt, duration, loop) -> {
            return base.advance(dt.multiply(speed), duration.multiply(speed), loop);
         });
      }
   }

   static EffectLoop group(Collection<EffectLoop> effectLoops) {
      return new EffectLoopGroup(effectLoops);
   }

   @FunctionalInterface
   public interface AdvanceModifier {
      boolean advance(EffectLoop var1, EffectLoop.Time var2, EffectLoop.Time var3, boolean var4);
   }

   public static class Time {
      public static final EffectLoop.Time ZERO = new EffectLoop.Time(0L) {
         public EffectLoop.Time multiply(double factor) {
            return this;
         }
      };
      public static final EffectLoop.Time ONE_TICK = nanos(50000000L);
      public static final EffectLoop.Time NEVER = new EffectLoop.Time(Double.MAX_VALUE, Long.MAX_VALUE);
      public final double seconds;
      public final long nanos;

      public static EffectLoop.Time seconds(double seconds) {
         return new EffectLoop.Time(seconds);
      }

      public static EffectLoop.Time nanos(long nanoSeconds) {
         return new EffectLoop.Time(nanoSeconds);
      }

      private Time(double seconds) {
         this(seconds, (long)(seconds * 1.0E9D));
      }

      private Time(long nanos) {
         this((double)nanos / 1.0E9D, nanos);
      }

      private Time(double seconds, long nanos) {
         this.seconds = seconds;
         this.nanos = nanos;
      }

      public boolean isZero() {
         return this.nanos == 0L;
      }

      public boolean isNever() {
         return this.nanos == Long.MAX_VALUE;
      }

      public EffectLoop.Time multiply(double factor) {
         return seconds(this.seconds * factor);
      }

      public EffectLoop.Time multiply(int factor) {
         return nanos(this.nanos * (long)factor);
      }

      public EffectLoop.Time add(EffectLoop.Time step, int count) {
         return count == 0 ? this : nanos(this.nanos + step.nanos * (long)count);
      }

      public static long secondsToNanos(double seconds) {
         return (long)(seconds * 1.0E9D);
      }

      public int roundDiv(EffectLoop.Time divisor) {
         if (divisor.isZero()) {
            throw new ArithmeticException("Divisor is zero");
         } else {
            long division = this.nanos / divisor.nanos;
            long remainder = Math.abs(this.nanos % divisor.nanos);
            if (2L * remainder >= Math.abs(divisor.nanos)) {
               division += this.nanos < 0L ^ divisor.nanos < 0L ? -1L : 1L;
            }

            return (int)division;
         }
      }

      public EffectLoop.Time adjustBPM(int fromBPM, int toBPM) {
         return fromBPM == toBPM ? this : nanos(this.nanos * (long)fromBPM / (long)toBPM);
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (o instanceof EffectLoop.Time) {
            return ((EffectLoop.Time)o).nanos == this.nanos;
         } else {
            return false;
         }
      }

      public String toString() {
         if (this.isNever()) {
            return "Time{NEVER}";
         } else {
            return this.isZero() ? "Time{ZERO}" : String.format("Time{%.3f seconds, %d nanos}", this.seconds, this.nanos);
         }
      }

      // $FF: synthetic method
      Time(long x0, Object x1) {
         this(x0);
      }
   }

   @FunctionalInterface
   public interface Player {
      void play(EffectLoop var1, EffectLoop.RunMode var2);

      default void play(EffectLoop loop) {
         this.play(loop, EffectLoop.RunMode.ASYNCHRONOUS);
      }

      default DelayedEffectTask scheduleTask(EffectLoop.Time delay, Runnable task) {
         return this.scheduleTask(delay, task, EffectLoop.RunMode.ASYNCHRONOUS);
      }

      default DelayedEffectTask scheduleTask(EffectLoop.Time delay, Runnable task, EffectLoop.RunMode runMode) {
         DelayedEffectTask delayedTask = new DelayedEffectTask(delay, task);
         this.play(delayedTask, runMode);
         return delayedTask;
      }
   }

   public static enum RunMode {
      SYNCHRONOUS,
      ASYNCHRONOUS;

      // $FF: synthetic method
      private static EffectLoop.RunMode[] $values() {
         return new EffectLoop.RunMode[]{SYNCHRONOUS, ASYNCHRONOUS};
      }
   }
}
