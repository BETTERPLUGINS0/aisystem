package com.bergerkiller.bukkit.tc.attachments.control.effect;

import com.bergerkiller.bukkit.tc.attachments.api.Attachment;

public class SimpleScheduledEffectLoop extends ScheduledEffectLoopBase {
   private long nanosDelay = 0L;

   public void setDelay(EffectLoop.Time delay) {
      this.nanosDelay = delay.nanos;
   }

   public boolean advance(long prevNanos, long currNanos) {
      if (currNanos < this.nanosDelay) {
         return true;
      } else {
         if (prevNanos <= this.nanosDelay) {
            this.getEffectSink().playEffect(Attachment.EffectAttachment.EffectOptions.DEFAULT);
         }

         return false;
      }
   }
}
