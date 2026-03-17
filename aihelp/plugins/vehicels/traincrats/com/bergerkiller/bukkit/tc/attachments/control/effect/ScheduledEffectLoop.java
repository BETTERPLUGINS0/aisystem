package com.bergerkiller.bukkit.tc.attachments.control.effect;

public interface ScheduledEffectLoop {
   ScheduledEffectLoop NONE = (prevNanos, currNanos) -> {
      return false;
   };

   boolean advance(long var1, long var3);

   default ScheduledEffectLoop.SequentialEffectLoop asEffectLoop() {
      return this.asEffectLoop((EffectLoop.Time)null);
   }

   default ScheduledEffectLoop.SequentialEffectLoop asEffectLoop(final EffectLoop.Time overrideDuration) {
      return new ScheduledEffectLoop.SequentialEffectLoop() {
         private long nanosElapsed = 0L;

         public boolean advance(EffectLoop.Time dt, EffectLoop.Time duration, boolean loop) {
            if (overrideDuration != null) {
               duration = overrideDuration;
            }

            long prev_time_nanos = this.nanosElapsed;
            long curr_time_nanos = prev_time_nanos + dt.nanos;
            if (duration.isZero()) {
               this.nanosElapsed = curr_time_nanos;
               return ScheduledEffectLoop.this.advance(prev_time_nanos, curr_time_nanos);
            } else if (curr_time_nanos > duration.nanos) {
               if (loop) {
                  long remainder = curr_time_nanos - duration.nanos;
                  this.nanosElapsed = remainder;
                  ScheduledEffectLoop.this.advance(prev_time_nanos, duration.nanos);
                  ScheduledEffectLoop.this.advance(0L, remainder);
                  return true;
               } else {
                  this.nanosElapsed = duration.nanos;
                  ScheduledEffectLoop.this.advance(prev_time_nanos, duration.nanos);
                  return false;
               }
            } else {
               this.nanosElapsed = curr_time_nanos;
               return ScheduledEffectLoop.this.advance(prev_time_nanos, curr_time_nanos) || loop;
            }
         }

         public long nanosElapsed() {
            return this.nanosElapsed;
         }

         public void resetToBeginning() {
            this.nanosElapsed = 0L;
         }
      };
   }

   public interface SequentialEffectLoop extends EffectLoop {
      long nanosElapsed();
   }
}
