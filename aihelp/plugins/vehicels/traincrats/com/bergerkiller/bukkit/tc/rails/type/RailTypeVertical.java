package com.bergerkiller.bukkit.tc.rails.type;

import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogic;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVertical;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVerticalSlopeNormalB;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVerticalSlopeUpsideDownB;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVerticalSlopeUpsideDownC;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVerticalSlopeUpsideDownD;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Rails;

public class RailTypeVertical extends RailType {
   private static final BlockFace[] SIGN_TRIGGER_DIRS;

   public boolean isRail(BlockData blockData) {
      return Util.ISVERTRAIL.get(blockData);
   }

   public Block findRail(Block pos) {
      if (this.isRail(pos)) {
         return pos;
      } else {
         Block below = pos.getRelative(BlockFace.DOWN);
         if (this.isRail(below) && this.getAfterSlope(below) != null) {
            return below;
         } else {
            Block above = pos.getRelative(BlockFace.UP);
            if (this.isRail(above) && this.isVerticalSlopeUpsideDownB(above)) {
               return above;
            } else {
               Block twoAbove = pos.getRelative(0, 2, 0);
               return this.isRail(twoAbove) && this.isVerticalSlopeUpsideDownB(twoAbove) ? twoAbove : null;
            }
         }
      }
   }

   public Block findMinecartPos(Block trackBlock) {
      return trackBlock;
   }

   public boolean onBlockCollision(MinecartMember<?> member, Block railsBlock, Block hitBlock, BlockFace hitFace) {
      if (!super.onBlockCollision(member, railsBlock, hitBlock, hitFace)) {
         return false;
      } else {
         Block minecartPos = this.findMinecartPos(railsBlock);
         if (hitBlock.getX() == minecartPos.getX() && hitBlock.getZ() == minecartPos.getZ()) {
            int dy = hitBlock.getY() - minecartPos.getY();
            if (dy >= -1 && dy <= 1) {
               return !this.isRail(hitBlock);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean isHeadOnCollision(MinecartMember<?> member, Block railsBlock, Block hitBlock) {
      if (super.isHeadOnCollision(member, railsBlock, hitBlock)) {
         return true;
      } else {
         Block minecartPos = this.findMinecartPos(railsBlock);
         return hitBlock.getY() - minecartPos.getY() == member.getDirectionTo().getModY();
      }
   }

   public BlockFace[] getPossibleDirections(Block trackBlock) {
      return new BlockFace[]{BlockFace.UP, BlockFace.DOWN};
   }

   public boolean onCollide(MinecartMember<?> with, Block block, BlockFace hitFace) {
      return false;
   }

   public BlockFace getDirection(Block railsBlock) {
      return BlockFace.UP;
   }

   public BlockFace getSignColumnDirection(Block railsBlock) {
      return Util.getVerticalRailDirection(railsBlock);
   }

   public BlockFace[] getSignTriggerDirections(Block railBlock, Block signBlock, BlockFace signFacing) {
      return SIGN_TRIGGER_DIRS;
   }

   public RailLogic getLogic(RailState state) {
      BlockFace dir = Util.getVerticalRailDirection(state.railBlock());
      if (this.isVerticalSlopeUpsideDown(state.railBlock())) {
         if (state.railPosition().getY() < 0.0D && this.isVerticalSlopeUpsideDownB(state.railBlock())) {
            return RailLogicVerticalSlopeUpsideDownB.get(dir.getOppositeFace());
         } else {
            return (RailLogic)(this.isVerticalSlopeUpsideDownB(state.railBlock()) ? RailLogicVerticalSlopeUpsideDownD.get(dir.getOppositeFace()) : RailLogicVerticalSlopeUpsideDownC.get(dir.getOppositeFace()));
         }
      } else if (this.isVerticalSlopeUpsideDownB(state.railBlock())) {
         return RailLogicVerticalSlopeUpsideDownB.get(dir.getOppositeFace());
      } else {
         return (RailLogic)(this.getAfterSlope(state.railBlock()) != null ? RailLogicVerticalSlopeNormalB.get(dir) : RailLogicVertical.get(dir));
      }
   }

   public Location getSpawnLocation(Block railsBlock, BlockFace orientation) {
      BlockFace dir = Util.getVerticalRailDirection(railsBlock);
      double dx = 0.5D + 0.4375D * (double)dir.getModX();
      double dz = 0.5D + 0.4375D * (double)dir.getModZ();
      return new Location(railsBlock.getWorld(), (double)railsBlock.getX() + dx, (double)railsBlock.getY() + 0.5D, (double)railsBlock.getZ() + dz, (float)FaceUtil.faceToYaw(dir), -90.0F);
   }

   private boolean isVerticalSlopeUpsideDown(Block railsBlock) {
      Block above = railsBlock.getRelative(BlockFace.UP);
      return this.isUpsideDownRail(above);
   }

   private boolean isVerticalSlopeUpsideDownB(Block railsBlock) {
      BlockFace dir = Util.getVerticalRailDirection(railsBlock);
      Block slopeBlock = railsBlock.getRelative(dir.getModX(), -1, dir.getModZ());
      return this.isUpsideDownRail(slopeBlock);
   }

   private boolean isUpsideDownRail(Block railsBlock) {
      MaterialData materialData = WorldUtil.getBlockData(railsBlock).getMaterialData();
      return materialData instanceof Rails ? RailType.REGULAR.isUpsideDown(railsBlock, (Rails)materialData) : false;
   }

   private Block getAfterSlope(Block verticalRail) {
      if (!this.isRail(verticalRail)) {
         return null;
      } else {
         Block above = verticalRail.getRelative(BlockFace.UP);
         if (BlockUtil.isSolid(above)) {
            return null;
         } else {
            BlockFace dir = Util.getVerticalRailDirection(verticalRail);
            Block possible = above.getRelative(dir);
            RailPiece railPiece = RailType.findRailPiece(possible);
            return railPiece != null && LogicUtil.contains(dir.getOppositeFace(), railPiece.type().getPossibleDirections(railPiece.block())) ? railPiece.block() : null;
         }
      }
   }

   static {
      SIGN_TRIGGER_DIRS = new BlockFace[]{BlockFace.UP, BlockFace.DOWN};
   }
}
