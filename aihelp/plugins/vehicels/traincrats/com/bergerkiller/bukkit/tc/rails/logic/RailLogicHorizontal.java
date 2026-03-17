package com.bergerkiller.bukkit.tc.rails.logic;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class RailLogicHorizontal extends RailLogic {
   private static final RailLogicHorizontal[] values = new RailLogicHorizontal[8];
   private static final RailLogicHorizontal[] values_upsidedown = new RailLogicHorizontal[8];
   private final boolean upside_down;
   protected final double dx;
   protected final double dz;
   protected final double startX;
   protected final double startZ;
   private final BlockFace horizontalCartDir;
   private final BlockFace[] cartFaces;
   private final BlockFace[] faces;
   private final BlockFace[] ends;
   public static final double Y_POS_OFFSET = 0.0625D;
   public static final double Y_POS_OFFSET_UPSIDEDOWN = -0.0625D;
   public static final double Y_POS_OFFSET_UPSIDEDOWN_SLOPE = -0.2D;

   protected RailLogicHorizontal(BlockFace direction) {
      this(direction, false);
   }

   protected RailLogicHorizontal(BlockFace direction, boolean upsideDown) {
      super(direction);
      this.horizontalCartDir = FaceUtil.getRailsCartDirection(direction);
      this.upside_down = upsideDown;
      this.cartFaces = FaceUtil.getFaces(this.getCartDirection());
      this.ends = FaceUtil.getFaces(direction.getOppositeFace());
      direction = FaceUtil.toRailsDirection(direction);
      if (this.curved) {
         this.dx = 0.5D * (double)direction.getModX();
         this.dz = -0.5D * (double)direction.getModZ();
         direction = direction.getOppositeFace();
      } else {
         this.dx = (double)direction.getModX();
         this.dz = (double)direction.getModZ();
      }

      this.faces = FaceUtil.getFaces(direction);
      double startFactor = MathUtil.invert(0.5D, !this.curved);
      this.startX = startFactor * (double)this.faces[0].getModX();
      this.startZ = startFactor * (double)this.faces[0].getModZ();

      for(int i = 0; i < this.faces.length; ++i) {
         if (this.faces[i] == BlockFace.NORTH || this.faces[i] == BlockFace.SOUTH) {
            this.faces[i] = this.faces[i].getOppositeFace();
         }
      }

   }

   public BlockFace getCartDirection() {
      return this.horizontalCartDir;
   }

   public static RailLogicHorizontal get(BlockFace direction) {
      return values[FaceUtil.faceToNotch(direction)];
   }

   public static RailLogicHorizontal get(BlockFace direction, boolean upsideDown) {
      return upsideDown ? values_upsidedown[FaceUtil.faceToNotch(direction)] : values[FaceUtil.faceToNotch(direction)];
   }

   protected RailPath createPath() {
      double base_y = this.isUpsideDown() ? -0.0625D : 0.0625D;
      Vector p1 = new Vector(this.startX + 0.5D, base_y, this.startZ + 0.5D);
      Vector p2 = p1.clone();
      if (this.alongZ) {
         p2.setZ(p2.getZ() + this.dz);
      } else if (this.alongX) {
         p2.setX(p2.getX() + this.dx);
      } else {
         p2.setX(p2.getX() - this.dx);
         p2.setZ(p2.getZ() - this.dz);
      }

      this.getFixedPosition(p1, IntVector3.ZERO);
      this.getFixedPosition(p2, IntVector3.ZERO);
      return (new RailPath.Builder()).up(this.isUpsideDown() ? BlockFace.DOWN : BlockFace.UP).add(p1).add(p2).build();
   }

   public boolean isUpsideDown() {
      return this.upside_down;
   }

   /** @deprecated */
   @Deprecated
   public void getFixedPosition(Vector position, IntVector3 railPos) {
   }

   public void onPathAdjust(RailState state) {
      if (this.isSloped()) {
         BlockFace enterFaceRot = state.enterFace();
         if (enterFaceRot == FaceUtil.rotate(this.horizontalCartDir, 2) || enterFaceRot == FaceUtil.rotate(this.horizontalCartDir, -2)) {
            state.position().setMotion(this.horizontalCartDir.getOppositeFace());
         }
      }

   }

   public BlockFace getMovementDirection(BlockFace endDirection) {
      BlockFace raildirection = this.getDirection();
      BlockFace direction;
      if (this.isSloped()) {
         if (endDirection != raildirection && endDirection != BlockFace.UP) {
            direction = raildirection.getOppositeFace();
         } else {
            direction = raildirection;
         }
      } else if (this.curved) {
         BlockFace targetFace;
         if (endDirection != this.ends[0] && endDirection != this.ends[1].getOppositeFace()) {
            targetFace = this.ends[1];
         } else {
            targetFace = this.ends[0];
         }

         direction = this.getCartDirection();
         if (!LogicUtil.contains(targetFace, this.cartFaces)) {
            direction = direction.getOppositeFace();
         }
      } else if (endDirection == raildirection.getOppositeFace()) {
         direction = raildirection.getOppositeFace();
      } else {
         direction = raildirection;
      }

      return direction;
   }

   static {
      for(int i = 0; i < 8; ++i) {
         values[i] = new RailLogicHorizontal(FaceUtil.notchToFace(i), false);
         values_upsidedown[i] = new RailLogicHorizontal(FaceUtil.notchToFace(i), true);
      }

   }
}
