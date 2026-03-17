package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicVerticalSlopeNormalA extends RailLogicVerticalSlopeBase {
   private static final RailLogicVerticalSlopeNormalA[] values = new RailLogicVerticalSlopeNormalA[4];

   private RailLogicVerticalSlopeNormalA(BlockFace direction) {
      super(direction, false);
   }

   public static RailLogicVerticalSlopeNormalA get(BlockFace direction) {
      return values[FaceUtil.faceToNotch(direction) >> 1];
   }

   protected RailPath createPath() {
      double dx = 0.5D + 0.4375D * (double)this.getDirection().getModX();
      double dz = 0.5D + 0.4375D * (double)this.getDirection().getModZ();
      Vector p1 = new Vector(dx, 0.0625D, dz);
      Vector p2 = new Vector(dx, 1.0D, dz);
      if (this.alongZ) {
         p1.setZ(0.5D - 0.5D * (double)this.getDirection().getModZ());
      } else if (this.alongX) {
         p1.setX(0.5D - 0.5D * (double)this.getDirection().getModX());
      }

      return (new RailPath.Builder()).add(p1, BlockFace.UP).add(p2, this.getDirection().getOppositeFace()).build();
   }

   static {
      for(int i = 0; i < 4; ++i) {
         values[i] = new RailLogicVerticalSlopeNormalA(FaceUtil.notchToFace(i << 1));
      }

   }
}
