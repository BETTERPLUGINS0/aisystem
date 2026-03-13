package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Wall;
import org.bukkit.block.data.type.RedstoneWire.Connection;
import org.bukkit.block.data.type.Wall.Height;
import org.bukkit.util.BlockVector;

@Snippet("object-rotator")
@Desc("Configures rotation for iris")
public class IrisObjectRotation {
   private static final List<BlockFace> WALL_FACES;
   @Desc("If this rotator is enabled or not")
   private boolean enabled = true;
   @Desc("The x axis rotation")
   private IrisAxisRotationClamp xAxis = new IrisAxisRotationClamp();
   @Desc("The y axis rotation")
   private IrisAxisRotationClamp yAxis = new IrisAxisRotationClamp(true, false, 0.0D, 0.0D, 90.0D);
   @Desc("The z axis rotation")
   private IrisAxisRotationClamp zAxis = new IrisAxisRotationClamp();

   public static IrisObjectRotation of(double x, double y, double z) {
      IrisObjectRotation var6 = new IrisObjectRotation();
      IrisAxisRotationClamp var7 = new IrisAxisRotationClamp();
      IrisAxisRotationClamp var8 = new IrisAxisRotationClamp();
      IrisAxisRotationClamp var9 = new IrisAxisRotationClamp();
      var6.setEnabled(var0 != 0.0D || var2 != 0.0D || var4 != 0.0D);
      var6.setXAxis(var7);
      var6.setYAxis(var8);
      var6.setZAxis(var9);
      var7.setEnabled(var0 != 0.0D);
      var8.setEnabled(var2 != 0.0D);
      var9.setEnabled(var4 != 0.0D);
      var7.setInterval(90.0D);
      var8.setInterval(90.0D);
      var9.setInterval(90.0D);
      var7.minMax(var0);
      var8.minMax(var2);
      var9.minMax(var4);
      return var6;
   }

   public double getYRotation(int spin) {
      return this.getRotation(var1, this.yAxis);
   }

   public double getXRotation(int spin) {
      return this.getRotation(var1, this.xAxis);
   }

   public double getZRotation(int spin) {
      return this.getRotation(var1, this.zAxis);
   }

   public IrisObject rotateCopy(IrisObject e) {
      return var1 == null ? null : var1.rotateCopy(this);
   }

   public IrisJigsawPiece rotateCopy(IrisJigsawPiece v, IrisPosition offset) {
      IrisJigsawPiece var3 = var1.copy();
      Iterator var4 = var3.getConnectors().iterator();

      while(var4.hasNext()) {
         IrisJigsawPieceConnector var5 = (IrisJigsawPieceConnector)var4.next();
         var5.setPosition(this.rotate(var5.getPosition()).add(var2));
         var5.setDirection(this.rotate(var5.getDirection()));
      }

      try {
         IrisObjectTranslate var8 = var3.getPlacementOptions().getTranslate();
         IrisPosition var7 = this.rotate(new IrisPosition(var8.getX(), var8.getY(), var8.getZ())).add(var2);
         var8.setX(var7.getX()).setY(var7.getY()).setZ(var7.getZ());
      } catch (NullPointerException var6) {
      }

      return var3;
   }

   public BlockVector rotate(BlockVector direction) {
      return this.rotate((BlockVector)var1, 0, 0, 0);
   }

   public IrisDirection rotate(IrisDirection direction) {
      BlockVector var2 = this.rotate(var1.toVector().toBlockVector());
      return IrisDirection.closest(var2);
   }

   public double getRotation(int spin, IrisAxisRotationClamp clamp) {
      if (!this.enabled) {
         return 0.0D;
      } else {
         return !var2.isEnabled() ? 0.0D : var2.getRadians(var1);
      }
   }

   public BlockFace getFace(BlockVector v) {
      int var2 = (int)Math.round(var1.getX());
      int var3 = (int)Math.round(var1.getY());
      int var4 = (int)Math.round(var1.getZ());
      if (var2 == 0 && var4 == -1) {
         return BlockFace.NORTH;
      } else if (var2 == 0 && var4 == 1) {
         return BlockFace.SOUTH;
      } else if (var2 == 1 && var4 == 0) {
         return BlockFace.EAST;
      } else if (var2 == -1 && var4 == 0) {
         return BlockFace.WEST;
      } else if (var3 > 0) {
         return BlockFace.UP;
      } else {
         return var3 < 0 ? BlockFace.DOWN : BlockFace.SOUTH;
      }
   }

   public BlockFace getHexFace(BlockVector v) {
      int var2 = var1.getBlockX();
      int var3 = var1.getBlockY();
      int var4 = var1.getBlockZ();
      if (var2 == 0 && var4 == -1) {
         return BlockFace.NORTH;
      } else if (var2 == 1 && var4 == -2) {
         return BlockFace.NORTH_NORTH_EAST;
      } else if (var2 == 1 && var4 == -1) {
         return BlockFace.NORTH_EAST;
      } else if (var2 == 2 && var4 == -1) {
         return BlockFace.EAST_NORTH_EAST;
      } else if (var2 == 1 && var4 == 0) {
         return BlockFace.EAST;
      } else if (var2 == 2 && var4 == 1) {
         return BlockFace.EAST_SOUTH_EAST;
      } else if (var2 == 1 && var4 == 1) {
         return BlockFace.SOUTH_EAST;
      } else if (var2 == 1 && var4 == 2) {
         return BlockFace.SOUTH_SOUTH_EAST;
      } else if (var2 == 0 && var4 == 1) {
         return BlockFace.SOUTH;
      } else if (var2 == -1 && var4 == 2) {
         return BlockFace.SOUTH_SOUTH_WEST;
      } else if (var2 == -1 && var4 == 1) {
         return BlockFace.SOUTH_WEST;
      } else if (var2 == -2 && var4 == 1) {
         return BlockFace.WEST_SOUTH_WEST;
      } else if (var2 == -1 && var4 == 0) {
         return BlockFace.WEST;
      } else if (var2 == -2 && var4 == -1) {
         return BlockFace.WEST_NORTH_WEST;
      } else if (var2 == -1 && var4 == -1) {
         return BlockFace.NORTH_WEST;
      } else if (var2 == -1 && var4 == -2) {
         return BlockFace.NORTH_NORTH_WEST;
      } else if (var3 > 0) {
         return BlockFace.UP;
      } else {
         return var3 < 0 ? BlockFace.DOWN : BlockFace.SOUTH;
      }
   }

   public BlockFace faceForAxis(Axis axis) {
      BlockFace var10000;
      switch(var1) {
      case X:
         var10000 = BlockFace.EAST;
         break;
      case Y:
         var10000 = BlockFace.UP;
         break;
      case Z:
         var10000 = BlockFace.NORTH;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public Axis axisFor(BlockFace f) {
      Axis var10000;
      switch(var1) {
      case NORTH:
      case SOUTH:
         var10000 = Axis.Z;
         break;
      case EAST:
      case WEST:
         var10000 = Axis.X;
         break;
      default:
         var10000 = Axis.Y;
      }

      return var10000;
   }

   public Axis axisFor2D(BlockFace f) {
      Axis var10000;
      switch(var1) {
      case EAST:
      case WEST:
      case UP:
      case DOWN:
         var10000 = Axis.X;
         break;
      default:
         var10000 = Axis.Z;
      }

      return var10000;
   }

   public BlockData rotate(BlockData dd, int spinxx, int spinyy, int spinzz) {
      BlockData var5 = var1;

      try {
         int var6 = (int)(90.0D * Math.ceil(Math.abs((double)var2 % 360.0D / 90.0D)));
         int var7 = (int)(90.0D * Math.ceil(Math.abs((double)var3 % 360.0D / 90.0D)));
         int var8 = (int)(90.0D * Math.ceil(Math.abs((double)var4 % 360.0D / 90.0D)));
         if (!this.canRotate()) {
            return var5;
         }

         BlockFace var15;
         BlockVector var16;
         BlockFace var17;
         if (var5 instanceof Directional) {
            Directional var9 = (Directional)var5;
            var15 = var9.getFacing();
            var16 = new BlockVector(var15.getModX(), var15.getModY(), var15.getModZ());
            var16 = this.rotate(var16.clone(), var6, var7, var8);
            var17 = this.getFace(var16);
            if (var9.getFaces().contains(var17)) {
               var9.setFacing(var17);
            } else if (!var9.getMaterial().isSolid()) {
               var5 = null;
            }
         } else if (var5 instanceof Rotatable) {
            Rotatable var10 = (Rotatable)var5;
            var15 = var10.getRotation();
            var16 = new BlockVector(var15.getModX(), 0, var15.getModZ());
            var16 = this.rotate(var16.clone(), var6, var7, var8);
            var17 = this.getHexFace(var16);
            var10.setRotation(var17);
         } else if (var5 instanceof Orientable) {
            Orientable var11 = (Orientable)var5;
            var15 = this.getFace(var11.getAxis());
            var16 = new BlockVector(var15.getModX(), var15.getModY(), var15.getModZ());
            var16 = this.rotate(var16.clone(), var6, var7, var8);
            Axis var26 = this.getAxis(var16);
            if (!var26.equals(var11.getAxis()) && var11.getAxes().contains(var26)) {
               var11.setAxis(var26);
            }
         } else {
            Iterator var27;
            if (var5 instanceof MultipleFacing) {
               MultipleFacing var12 = (MultipleFacing)var5;
               KList var23 = new KList();
               var27 = var12.getFaces().iterator();

               while(var27.hasNext()) {
                  var17 = (BlockFace)var27.next();
                  BlockVector var18 = new BlockVector(var17.getModX(), var17.getModY(), var17.getModZ());
                  var18 = this.rotate(var18.clone(), var6, var7, var8);
                  BlockFace var19 = this.getFace(var18);
                  if (var12.getAllowedFaces().contains(var19)) {
                     var23.add(var19);
                  }
               }

               var27 = var12.getFaces().iterator();

               while(var27.hasNext()) {
                  var17 = (BlockFace)var27.next();
                  var12.setFace(var17, false);
               }

               var27 = var23.iterator();

               while(var27.hasNext()) {
                  var17 = (BlockFace)var27.next();
                  var12.setFace(var17, true);
               }
            } else if (var5 instanceof Wall) {
               Wall var13 = (Wall)var5;
               KMap var24 = new KMap();
               var27 = WALL_FACES.iterator();

               while(var27.hasNext()) {
                  var17 = (BlockFace)var27.next();
                  Height var28 = var13.getHeight(var17);
                  BlockVector var30 = new BlockVector(var17.getModX(), var17.getModY(), var17.getModZ());
                  var30 = this.rotate(var30.clone(), var6, var7, var8);
                  BlockFace var20 = this.getFace(var30);
                  if (WALL_FACES.contains(var20)) {
                     var24.put(var20, var28);
                  }
               }

               var27 = WALL_FACES.iterator();

               while(var27.hasNext()) {
                  var17 = (BlockFace)var27.next();
                  var13.setHeight(var17, (Height)var24.getOrDefault(var17, Height.NONE));
               }
            } else if (var5 instanceof RedstoneWire) {
               RedstoneWire var14 = (RedstoneWire)var5;
               HashMap var25 = new HashMap();
               Set var31 = var14.getAllowedFaces();
               Iterator var32 = var31.iterator();

               BlockFace var29;
               while(var32.hasNext()) {
                  var29 = (BlockFace)var32.next();
                  Connection var33 = var14.getFace(var29);
                  BlockVector var34 = new BlockVector(var29.getModX(), var29.getModY(), var29.getModZ());
                  var34 = this.rotate(var34.clone(), var6, var7, var8);
                  BlockFace var21 = this.getFace(var34);
                  if (var31.contains(var21)) {
                     var25.put(var21, var33);
                  }
               }

               var32 = var31.iterator();

               while(var32.hasNext()) {
                  var29 = (BlockFace)var32.next();
                  var14.setFace(var29, (Connection)var25.getOrDefault(var29, Connection.NONE));
               }
            }
         }
      } catch (Throwable var22) {
         Iris.reportError(var22);
      }

      return var5;
   }

   public Axis getAxis(BlockVector v) {
      if (Math.abs(var1.getBlockX()) > Math.max(Math.abs(var1.getBlockY()), Math.abs(var1.getBlockZ()))) {
         return Axis.X;
      } else if (Math.abs(var1.getBlockY()) > Math.max(Math.abs(var1.getBlockX()), Math.abs(var1.getBlockZ()))) {
         return Axis.Y;
      } else {
         return Math.abs(var1.getBlockZ()) > Math.max(Math.abs(var1.getBlockX()), Math.abs(var1.getBlockY())) ? Axis.Z : Axis.Y;
      }
   }

   private BlockFace getFace(Axis axis) {
      BlockFace var10000;
      switch(var1) {
      case X:
         var10000 = BlockFace.EAST;
         break;
      case Y:
         var10000 = BlockFace.UP;
         break;
      case Z:
         var10000 = BlockFace.SOUTH;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public IrisPosition rotate(IrisPosition b) {
      return this.rotate((IrisPosition)var1, 0, 0, 0);
   }

   public IrisPosition rotate(IrisPosition b, int spinx, int spiny, int spinz) {
      return new IrisPosition(this.rotate(new BlockVector(var1.getX(), var1.getY(), var1.getZ()), var2, var3, var4));
   }

   public BlockVector rotate(BlockVector b, int spinx, int spiny, int spinz) {
      if (!this.canRotate()) {
         return var1;
      } else {
         BlockVector var5 = var1.clone();
         double var6;
         if (this.canRotateX()) {
            if (this.getXAxis().isLocked()) {
               if (Math.abs(this.getXAxis().getMax()) % 360.0D == 180.0D) {
                  var5.setZ(-var5.getZ());
                  var5.setY(-var5.getY());
               } else if (this.getXAxis().getMax() % 360.0D != 90.0D && this.getXAxis().getMax() % 360.0D != -270.0D) {
                  if (this.getXAxis().getMax() != -90.0D && this.getXAxis().getMax() % 360.0D != 270.0D) {
                     var5.rotateAroundX(this.getXRotation(var2));
                  } else {
                     var6 = var5.getZ();
                     var5.setZ(-var5.getY());
                     var5.setY(var6);
                  }
               } else {
                  var6 = var5.getZ();
                  var5.setZ(var5.getY());
                  var5.setY(-var6);
               }
            } else {
               var5.rotateAroundX(this.getXRotation(var2));
            }
         }

         if (this.canRotateZ()) {
            if (this.getZAxis().isLocked()) {
               if (Math.abs(this.getZAxis().getMax()) % 360.0D == 180.0D) {
                  var5.setY(-var5.getY());
                  var5.setX(-var5.getX());
               } else if (this.getZAxis().getMax() % 360.0D != 90.0D && this.getZAxis().getMax() % 360.0D != -270.0D) {
                  if (this.getZAxis().getMax() != -90.0D && this.getZAxis().getMax() % 360.0D != 270.0D) {
                     var5.rotateAroundZ(this.getZRotation(var4));
                  } else {
                     var6 = var5.getY();
                     var5.setY(-var5.getX());
                     var5.setX(var6);
                  }
               } else {
                  var6 = var5.getY();
                  var5.setY(var5.getX());
                  var5.setX(-var6);
               }
            } else {
               var5.rotateAroundY(this.getZRotation(var4));
            }
         }

         if (this.canRotateY()) {
            if (this.getYAxis().isLocked()) {
               if (Math.abs(this.getYAxis().getMax()) % 360.0D == 180.0D) {
                  var5.setX(-var5.getX());
                  var5.setZ(-var5.getZ());
               } else if (this.getYAxis().getMax() % 360.0D != 90.0D && this.getYAxis().getMax() % 360.0D != -270.0D) {
                  if (this.getYAxis().getMax() != -90.0D && this.getYAxis().getMax() % 360.0D != 270.0D) {
                     var5.rotateAroundY(this.getYRotation(var3));
                  } else {
                     var6 = var5.getX();
                     var5.setX(-var5.getZ());
                     var5.setZ(var6);
                  }
               } else {
                  var6 = var5.getX();
                  var5.setX(var5.getZ());
                  var5.setZ(-var6);
               }
            } else {
               var5.rotateAroundY(this.getYRotation(var3));
            }
         }

         return var5;
      }
   }

   public boolean canRotateX() {
      return this.enabled && this.xAxis.isEnabled();
   }

   public boolean canRotateY() {
      return this.enabled && this.yAxis.isEnabled();
   }

   public boolean canRotateZ() {
      return this.enabled && this.zAxis.isEnabled();
   }

   public boolean canRotate() {
      return this.canRotateX() || this.canRotateY() || this.canRotateZ();
   }

   @Generated
   public IrisObjectRotation() {
   }

   @Generated
   public IrisObjectRotation(final boolean enabled, final IrisAxisRotationClamp xAxis, final IrisAxisRotationClamp yAxis, final IrisAxisRotationClamp zAxis) {
      this.enabled = var1;
      this.xAxis = var2;
      this.yAxis = var3;
      this.zAxis = var4;
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public IrisAxisRotationClamp getXAxis() {
      return this.xAxis;
   }

   @Generated
   public IrisAxisRotationClamp getYAxis() {
      return this.yAxis;
   }

   @Generated
   public IrisAxisRotationClamp getZAxis() {
      return this.zAxis;
   }

   @Generated
   public IrisObjectRotation setEnabled(final boolean enabled) {
      this.enabled = var1;
      return this;
   }

   @Generated
   public IrisObjectRotation setXAxis(final IrisAxisRotationClamp xAxis) {
      this.xAxis = var1;
      return this;
   }

   @Generated
   public IrisObjectRotation setYAxis(final IrisAxisRotationClamp yAxis) {
      this.yAxis = var1;
      return this;
   }

   @Generated
   public IrisObjectRotation setZAxis(final IrisAxisRotationClamp zAxis) {
      this.zAxis = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectRotation)) {
         return false;
      } else {
         IrisObjectRotation var2 = (IrisObjectRotation)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isEnabled() != var2.isEnabled()) {
            return false;
         } else {
            label49: {
               IrisAxisRotationClamp var3 = this.getXAxis();
               IrisAxisRotationClamp var4 = var2.getXAxis();
               if (var3 == null) {
                  if (var4 == null) {
                     break label49;
                  }
               } else if (var3.equals(var4)) {
                  break label49;
               }

               return false;
            }

            IrisAxisRotationClamp var5 = this.getYAxis();
            IrisAxisRotationClamp var6 = var2.getYAxis();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisAxisRotationClamp var7 = this.getZAxis();
            IrisAxisRotationClamp var8 = var2.getZAxis();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisObjectRotation;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + (this.isEnabled() ? 79 : 97);
      IrisAxisRotationClamp var3 = this.getXAxis();
      var6 = var6 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisAxisRotationClamp var4 = this.getYAxis();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisAxisRotationClamp var5 = this.getZAxis();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isEnabled();
      return "IrisObjectRotation(enabled=" + var10000 + ", xAxis=" + String.valueOf(this.getXAxis()) + ", yAxis=" + String.valueOf(this.getYAxis()) + ", zAxis=" + String.valueOf(this.getZAxis()) + ")";
   }

   static {
      WALL_FACES = List.of(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);
   }
}
