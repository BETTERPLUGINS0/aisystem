package com.bergerkiller.bukkit.tc.attachments.control.effect;

import com.bergerkiller.bukkit.tc.attachments.api.Attachment;

public abstract class ScheduledEffectLoopBase implements ScheduledEffectLoop {
   private Attachment.EffectSink effectSink;

   public ScheduledEffectLoopBase() {
      this.effectSink = Attachment.EffectSink.DISABLED_EFFECT_SINK;
   }

   public Attachment.EffectSink getEffectSink() {
      return this.effectSink;
   }

   public void setEffectSink(Attachment.EffectSink effectSink) {
      this.effectSink = effectSink;
   }
}
