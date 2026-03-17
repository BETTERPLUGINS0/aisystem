package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicSloped extends RailLogicHorizontal {
   private static final RailLogicSloped[] values = new RailLogicSloped[4];
   private static final RailLogicSloped[] values_upsideDown = new RailLogicSloped[4];
   protected final double step;

   protected RailLogicSloped(BlockFace direction) {
      this(direction, false);
   }

   protected RailLogicSloped(BlockFace direction, boolean upsideDown) {
      super(direction, upsideDown);
      if (direction != BlockFace.SOUTH && direction != BlockFace.EAST) {
         this.step = -1.0D;
      } else {
         this.step = 1.0D;
      }

   }

   public static RailLogicSloped get(BlockFace direction) {
      return values[FaceUtil.faceToNotch(direction) >> 1];
   }

   public static RailLogicSloped get(BlockFace direction, boolean upsideDown) {
      return upsideDown ? values_upsideDown[FaceUtil.faceToNotch(direction) >> 1] : values[FaceUtil.faceToNotch(direction) >> 1];
   }

   public boolean isSloped() {
      return true;
   }

   protected RailPath createPath() {
      double base_y = this.isUpsideDown() ? -0.2625D : 0.0625D;
      Vector p1;
      Vector p2;
      switch(this.getDirection()) {
      case NORTH:
         p1 = new Vector(0.5D, base_y + 1.0D, 0.0D);
         p2 = new Vector(0.5D, base_y, 1.0D);
         break;
      case EAST:
         p1 = new Vector(0.0D, base_y, 0.5D);
         p2 = new Vector(1.0D, base_y + 1.0D, 0.5D);
         break;
      case SOUTH:
         p1 = new Vector(0.5D, base_y, 0.0D);
         p2 = new Vector(0.5D, base_y + 1.0D, 1.0D);
         break;
      case WEST:
      default:
         p1 = new Vector(0.0D, base_y + 1.0D, 0.5D);
         p2 = new Vector(1.0D, base_y, 0.5D);
      }

      this.getFixedPosition(p1, IntVector3.ZERO);
      this.getFixedPosition(p2, IntVector3.ZERO);
      return (new RailPath.Builder()).up(this.isUpsideDown() ? BlockFace.DOWN : BlockFace.UP).add(p1).add(p2).build();
   }

   public void onPreMove(MinecartMember<?> member) {
      super.onPreMove(member);
      if (this.checkSlopeBlockCollisions()) {
         CommonMinecart<?> entity = (CommonMinecart)member.getEntity();
         Block inside = member.getRailType().findMinecartPos(member.getBlock());
         double blockedDistance = Double.MAX_VALUE;
         Block heading = inside.getRelative(this.getDirection().getOppositeFace());
         if (member.isMoving() && !member.isHeadingTo(this.getDirection().getOppositeFace())) {
            if (member.isHeadingTo(this.getDirection())) {
               Block above = inside.getRelative(BlockFace.UP);
               if (BlockUtil.isSuffocating(above)) {
                  blockedDistance = entity.loc.xz.distance(above);
               }
            }
         } else if (BlockUtil.isSuffocating(heading)) {
            blockedDistance = entity.loc.xz.distance(heading) - 1.0D;
         }

         if (entity.vel.xz.length() > blockedDistance) {
            member.getGroup().setForwardForce(blockedDistance);
         }
      }

   }

   protected boolean checkSlopeBlockCollisions() {
      return true;
   }

   static {
      for(int i = 0; i < 4; ++i) {
         values[i] = new RailLogicSloped(FaceUtil.notchToFace(i << 1), false);
         values_upsideDown[i] = new RailLogicSloped(FaceUtil.notchToFace(i << 1), true);
      }

   }
}
