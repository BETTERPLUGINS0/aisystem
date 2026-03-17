package com.bergerkiller.bukkit.tc.rails.logic;

import org.bukkit.block.BlockFace;

public abstract class RailLogicVerticalSlopeBase extends RailLogicSloped {
   public RailLogicVerticalSlopeBase(BlockFace direction, boolean upsideDown) {
      super(direction, upsideDown);
   }

   public boolean hasVerticalMovement() {
      return true;
   }

   protected boolean checkSlopeBlockCollisions() {
      return false;
   }
}
