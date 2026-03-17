package com.bergerkiller.bukkit.tc.attachments.control.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class EffectLoopGroup implements EffectLoop {
   private final List<EffectLoop> originalGroup;
   private final List<EffectLoop> group;

   public EffectLoopGroup(Collection<EffectLoop> group) {
      this.originalGroup = new ArrayList(group);
      this.group = new ArrayList(this.originalGroup);
   }

   public boolean advance(EffectLoop.Time dt, EffectLoop.Time duration, boolean loop) {
      this.group.removeIf((e) -> {
         return !e.advance(dt, duration, loop);
      });
      return !this.group.isEmpty();
   }

   public void resetToBeginning() {
      this.group.clear();
      this.originalGroup.forEach(EffectLoop::resetToBeginning);
      this.group.addAll(this.originalGroup);
   }
}
