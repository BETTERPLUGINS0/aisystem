package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.collections.InterpolatedMap;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartFurnace;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;

public class PoweredCartSoundLoop extends SoundLoop<MinecartMemberFurnace> {
   private static InterpolatedMap nodes = new InterpolatedMap();
   private int swooshSoundCounter = 0;

   public PoweredCartSoundLoop(MinecartMemberFurnace member) {
      super(member);
   }

   public void onTick() {
      if (((CommonMinecartFurnace)((MinecartMemberFurnace)this.member).getEntity()).hasFuel()) {
         ++this.swooshSoundCounter;
         int interval = (int)nodes.get(((CommonMinecartFurnace)((MinecartMemberFurnace)this.member).getEntity()).getMovedDistance());
         if (this.swooshSoundCounter >= interval) {
            this.swooshSoundCounter = 0;
            this.play(SoundEffect.WALK_CLOTH, 0.6F + 0.2F * this.random.nextFloat(), 0.2F);
            this.play(SoundEffect.EXTINGUISH, 1.5F + 0.3F * this.random.nextFloat(), 0.05F + 0.1F * this.random.nextFloat());
         }

      }
   }

   static {
      nodes.put(0.0D, 2.147483647E9D);
      nodes.put(0.001D, 50.0D);
      nodes.put(0.005D, 23.0D);
      nodes.put(0.01D, 18.0D);
      nodes.put(0.05D, 16.0D);
      nodes.put(0.1D, 14.0D);
      nodes.put(0.2D, 8.0D);
      nodes.put(0.4D, 5.0D);
   }
}
