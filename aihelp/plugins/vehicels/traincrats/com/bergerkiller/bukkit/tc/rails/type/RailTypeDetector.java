package com.bergerkiller.bukkit.tc.rails.type;

import com.bergerkiller.bukkit.common.wrappers.BlockData;
import org.bukkit.block.Block;

public class RailTypeDetector extends RailTypeRegular {
   public boolean isRail(BlockData blockData) {
      return blockData.isType(RailTypeRegular.RailMaterials.DETECTOR);
   }

   public boolean hasBlockActivation(Block railBlock) {
      return true;
   }
}
