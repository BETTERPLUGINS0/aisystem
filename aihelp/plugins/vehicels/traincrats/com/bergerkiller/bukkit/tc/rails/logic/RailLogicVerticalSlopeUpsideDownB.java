package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicVerticalSlopeUpsideDownB extends RailLogicVerticalSlopeBase {
   private static final RailLogicVerticalSlopeUpsideDownB[] values = new RailLogicVerticalSlopeUpsideDownB[4];

   private RailLogicVerticalSlopeUpsideDownB(BlockFace direction) {
      super(direction, true);
   }

   public static RailLogicVerticalSlopeUpsideDownB get(BlockFace direction) {
      return values[FaceUtil.faceToNotch(direction) >> 1];
   }

   public RailPath getPath() {
      double dx = 0.5D - 0.4375D * (double)this.getDirection().getModX();
      double dz = 0.5D - 0.4375D * (double)this.getDirection().getModZ();
      Vector p1 = new Vector(dx, 1.0D, dz);
      Vector p2 = new Vector(dx, -0.2D, dz);
      Vector p3 = new Vector(dx, -0.2625D, dz);
      if (this.alongZ) {
         p3.setZ(0.5D - 0.5D * (double)this.getDirection().getModZ());
      } else if (this.alongX) {
         p3.setX(0.5D - 0.5D * (double)this.getDirection().getModX());
      }

      return (new RailPath.Builder()).add(p1, this.getDirection()).add(p2, this.getDirection()).add(p3, BlockFace.DOWN).build();
   }

   static {
      for(int i = 0; i < 4; ++i) {
         values[i] = new RailLogicVerticalSlopeUpsideDownB(FaceUtil.notchToFace(i << 1));
      }

   }
}
