package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicVertical extends RailLogic {
   private static final RailLogicVertical[] values = new RailLogicVertical[4];
   public static final double XZ_POS_OFFSET = 0.4375D;

   private RailLogicVertical(BlockFace direction) {
      super(direction);
   }

   public static RailLogicVertical get(BlockFace direction) {
      return values[FaceUtil.faceToNotch(direction) >> 1];
   }

   public BlockFace getMovementDirection(BlockFace endDirection) {
      return FaceUtil.isVertical(endDirection) ? endDirection : BlockFace.DOWN;
   }

   public double getForwardVelocity(MinecartMember<?> member) {
      CommonEntity<?> e = member.getEntity();
      double dot = (double)member.getDirection().getModY() * e.vel.getY();
      return MathUtil.invert(e.vel.length(), dot < 0.0D);
   }

   public void setForwardVelocity(MinecartMember<?> member, double force) {
      ((CommonMinecart)member.getEntity()).vel.setY((double)member.getDirection().getModY() * force);
   }

   protected RailPath createPath() {
      double dx = 0.5D + 0.4375D * (double)this.getDirection().getModX();
      double dz = 0.5D + 0.4375D * (double)this.getDirection().getModZ();
      Vector p1 = new Vector(dx, 0.0D, dz);
      Vector p2 = new Vector(dx, 1.0D, dz);
      return (new RailPath.Builder()).up(this.getDirection().getOppositeFace()).add(p1).add(p2).build();
   }

   public boolean hasVerticalMovement() {
      return true;
   }

   static {
      for(int i = 0; i < 4; ++i) {
         values[i] = new RailLogicVertical(FaceUtil.notchToFace(i << 1));
      }

   }
}
