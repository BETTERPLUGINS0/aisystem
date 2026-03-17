package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TrainCarts;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.Rails;

public class PoweredTrackLogic {
   private final Material railType;

   public PoweredTrackLogic(Material railType) {
      this.railType = railType;
   }

   public void updateRedstone(Block railsBlock) {
      BlockData railsBlockData = WorldUtil.getBlockData(railsBlock);
      if (railsBlockData.getMaterialData() instanceof PoweredRail) {
         PoweredRail rails = (PoweredRail)railsBlockData.newMaterialData();
         boolean oldPowered = rails.isPowered();
         boolean newPowered = this.checkPowered(railsBlock);
         if (oldPowered != newPowered) {
            BlockRedstoneEvent redstoneEvent = new BlockRedstoneEvent(railsBlock, oldPowered ? 15 : 0, newPowered ? 15 : 0);
            CommonUtil.callEvent(redstoneEvent);
            if (redstoneEvent.getNewCurrent() > 0 == oldPowered) {
               return;
            }

            rails.setPowered(newPowered);
            WorldUtil.setBlockDataFast(railsBlock, BlockData.fromMaterialData(rails));
            WorldUtil.queueBlockSend(railsBlock);
            TrainCarts.plugin.applyBlockPhysics(railsBlock.getRelative(rails.getDirection()), railsBlockData);
            TrainCarts.plugin.applyBlockPhysics(railsBlock.getRelative(rails.getDirection().getOppositeFace()), railsBlockData);
         }

      }
   }

   public boolean checkPowered(Block railsBlock) {
      World world = railsBlock.getWorld();
      IntVector3 blockposition = new IntVector3(railsBlock);
      BlockData iblockdata = WorldUtil.getBlockData(railsBlock);
      return railsBlock.isBlockIndirectlyPowered() || this.checkEnd(world, blockposition, iblockdata, true, 0) || this.checkEnd(world, blockposition, iblockdata, false, 0);
   }

   public boolean checkEnd(World world, IntVector3 blockposition, BlockData iblockdata, boolean directionMode, int iterCtr) {
      MaterialData data = iblockdata.getMaterialData();
      if (!(data instanceof Rails)) {
         return false;
      } else {
         Rails rails = (Rails)data;
         BlockFace railDirection = rails.getDirection();
         BlockFace checkDirection = FaceUtil.isAlongX(railDirection) ? BlockFace.EAST : BlockFace.SOUTH;
         BlockFace walkDirection = directionMode ? checkDirection.getOppositeFace() : checkDirection;
         IntVector3 nextPos = blockposition.add(walkDirection);
         boolean isSlopeUp = rails.isOnSlope() && railDirection == walkDirection;
         if (isSlopeUp) {
            nextPos = nextPos.add(BlockFace.UP);
         }

         if (this.checkStep(world, nextPos, directionMode, iterCtr, checkDirection)) {
            return true;
         } else {
            return !isSlopeUp && this.checkStep(world, nextPos.add(BlockFace.DOWN), directionMode, iterCtr, checkDirection);
         }
      }
   }

   public boolean checkStep(World world, IntVector3 blockposition, boolean directionMode, int iterCtr, BlockFace walkDirection) {
      BlockData iblockdata = WorldUtil.getBlockData(world, blockposition);
      if (!iblockdata.isType(this.railType)) {
         return false;
      } else {
         MaterialData blockData = iblockdata.getMaterialData();
         if (!(blockData instanceof Rails)) {
            return false;
         } else {
            Rails rails = (Rails)blockData;
            BlockFace railDirection = rails.getDirection();
            if (FaceUtil.isAlongX(walkDirection) != FaceUtil.isAlongX(railDirection)) {
               return false;
            } else if (blockData instanceof PoweredRail && ((PoweredRail)blockData).isPowered()) {
               if (blockposition.toBlock(world).isBlockIndirectlyPowered()) {
                  return true;
               } else {
                  ++iterCtr;
                  return iterCtr >= 8 ? false : this.checkEnd(world, blockposition, iblockdata, directionMode, iterCtr);
               }
            } else {
               return false;
            }
         }
      }
   }
}
