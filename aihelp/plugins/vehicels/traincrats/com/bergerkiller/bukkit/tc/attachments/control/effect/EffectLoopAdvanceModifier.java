package com.bergerkiller.bukkit.tc.attachments.control.effect;

class EffectLoopAdvanceModifier implements EffectLoop {
   private final EffectLoop base;
   private final EffectLoop.AdvanceModifier modifier;

   public EffectLoopAdvanceModifier(EffectLoop base, EffectLoop.AdvanceModifier modifier) {
      this.base = base;
      this.modifier = modifier;
   }

   public boolean advance(EffectLoop.Time dt, EffectLoop.Time duration, boolean loop) {
      return this.modifier.advance(this.base, dt, duration, loop);
   }

   public void resetToBeginning() {
      this.base.resetToBeginning();
   }
}
