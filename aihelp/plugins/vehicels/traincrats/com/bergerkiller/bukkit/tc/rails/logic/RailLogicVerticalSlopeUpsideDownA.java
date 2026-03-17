package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicVerticalSlopeUpsideDownA extends RailLogicVerticalSlopeBase {
   private static final RailLogicVerticalSlopeUpsideDownA[] values = new RailLogicVerticalSlopeUpsideDownA[4];

   protected RailLogicVerticalSlopeUpsideDownA(BlockFace direction) {
      super(direction, true);
   }

   public static RailLogicVerticalSlopeUpsideDownA get(BlockFace direction) {
      return values[FaceUtil.faceToNotch(direction) >> 1];
   }

   protected RailPath createPath() {
      double base_y = -0.2625D;
      Vector p1;
      Vector p2;
      switch(this.getDirection()) {
      case NORTH:
         p1 = new Vector(0.5D, base_y + 1.0D, 0.0D);
         p2 = new Vector(0.5D, base_y, 1.0D);
         break;
      case EAST:
         p1 = new Vector(1.0D, base_y + 1.0D, 0.5D);
         p2 = new Vector(0.0D, base_y, 0.5D);
         break;
      case SOUTH:
         p1 = new Vector(0.5D, base_y + 1.0D, 1.0D);
         p2 = new Vector(0.5D, base_y, 0.0D);
         break;
      case WEST:
      default:
         p1 = new Vector(0.0D, base_y + 1.0D, 0.5D);
         p2 = new Vector(1.0D, base_y, 0.5D);
      }

      if (p2.getY() < 0.0D) {
         Vector d = p2.clone().subtract(p1).normalize();
         d.multiply(p2.getY() / d.getY());
         p2.subtract(d);
      }

      return (new RailPath.Builder()).up(BlockFace.DOWN).add(p1).add(p2).build();
   }

   static {
      for(int i = 0; i < 4; ++i) {
         values[i] = new RailLogicVerticalSlopeUpsideDownA(FaceUtil.notchToFace(i << 1));
      }

   }
}
