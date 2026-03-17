package com.bergerkiller.bukkit.tc.rails.type;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogic;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicAir;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicGround;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class RailTypeNone extends RailType {
   public boolean onCollide(MinecartMember<?> with, Block block, BlockFace hitFace) {
      double dx = ((CommonMinecart)with.getEntity()).loc.getX() - (double)block.getX();
      double dy = ((CommonMinecart)with.getEntity()).loc.getY() - (double)block.getY();
      double dz = ((CommonMinecart)with.getEntity()).loc.getZ() - (double)block.getZ();
      double vx = ((CommonMinecart)with.getEntity()).vel.getX();
      double vy = ((CommonMinecart)with.getEntity()).vel.getY();
      double vz = ((CommonMinecart)with.getEntity()).vel.getZ();
      double VEL_LIMIT = 0.05D;
      if ((!(vy < -0.05D) || !(dx < 0.0D) || !(vx < -0.05D)) && (!(dx > 1.0D) || !(vx > 0.05D)) && (!(dz < 0.0D) || !(vz < -0.05D)) && (!(dz > 1.0D) || !(vz > 0.05D))) {
         return (!(vy > 0.05D) || !(dy < -0.5D) || !(dx < 0.0D) || !(vx < -0.05D)) && (!(dx > 1.0D) || !(vx > 0.05D)) && (!(dz < 0.0D) || !(vz < -0.05D)) && (!(dz > 1.0D) || !(vz > 0.05D));
      } else {
         return false;
      }
   }

   public boolean isRail(BlockData blockData) {
      return false;
   }

   public Block findMinecartPos(Block trackBlock) {
      return trackBlock;
   }

   public BlockFace[] getPossibleDirections(Block trackBlock) {
      return new BlockFace[0];
   }

   public Block findRail(Block pos) {
      return pos;
   }

   public List<RailJunction> getJunctions(Block railBlock) {
      return Collections.emptyList();
   }

   public BlockFace getDirection(Block railsBlock) {
      return BlockFace.SELF;
   }

   public BlockFace getSignColumnDirection(Block railsBlock) {
      return BlockFace.SELF;
   }

   public Location getSpawnLocation(Block railsBlock, BlockFace orientation) {
      Location loc = railsBlock.getLocation();
      loc.setX(0.5D);
      loc.setY(0.5D);
      loc.setZ(0.5D);
      loc.setDirection(FaceUtil.faceToVector(orientation));
      return loc;
   }

   public RailLogic getLogic(RailState state) {
      MinecartMember<?> member = state.member();
      return (RailLogic)(member != null && !member.isFlying() ? RailLogicGround.INSTANCE : RailLogicAir.INSTANCE);
   }

   public String toString() {
      return "NONE";
   }
}
