package com.bergerkiller.bukkit.tc.rails.type;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogic;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicHorizontal;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class RailTypeCrossing extends RailTypeHorizontal {
   public boolean isRail(BlockData blockData) {
      return MaterialUtil.ISPRESSUREPLATE.get(blockData);
   }

   public boolean hasBlockActivation(Block railBlock) {
      return true;
   }

   public BlockFace getDirection(Block railBlock) {
      return Util.getPlateDirection(railBlock);
   }

   public BlockFace[] getPossibleDirections(Block trackBlock) {
      BlockFace dir = this.getDirection(trackBlock);
      return dir == BlockFace.SELF ? FaceUtil.RADIAL : RailTypeRegular.getPossibleDirections(dir);
   }

   public RailLogic getLogic(RailState state) {
      BlockFace dir = Util.getPlateDirection(state.railBlock());
      if (dir == BlockFace.SELF) {
         dir = FaceUtil.toRailsDirection(state.enterFace());
      }

      return RailLogicHorizontal.get(dir);
   }

   public void onPostMove(MinecartMember<?> member) {
      super.onPostMove(member);
   }

   public Location getSpawnLocation(Block railsBlock, BlockFace orientation) {
      BlockFace dir = Util.getPlateDirection(railsBlock);
      if (dir == BlockFace.SELF) {
         dir = orientation;
      }

      Location result = super.getSpawnLocation(railsBlock, dir);
      if (FaceUtil.isAlongX(dir)) {
         result.setYaw(0.0F);
      } else if (FaceUtil.isAlongZ(dir)) {
         result.setYaw(-90.0F);
      }

      return result;
   }
}
