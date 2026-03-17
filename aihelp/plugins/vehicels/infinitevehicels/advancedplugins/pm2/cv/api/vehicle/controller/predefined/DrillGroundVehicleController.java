package advancedplugins.pm2.cv.api.vehicle.controller.predefined;

import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import java.util.LinkedList;
import java.util.List;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class DrillGroundVehicleController extends GroundVehicleController {
   private double minHeight;
   private double maxHeight;
   private double width;
   private float cooldown;
   private DrillGroundVehicleController.DrillingType drillingType;
   private double depth;
   private final String DRILL_MIN_HEIGHT_KEY = "drilling-min-height";
   private final String DRILL_MAX_HEIGHT_KEY = "drilling-max-height";

   public DrillGroundVehicleController(@NotNull Vehicle var1, @Nullable VehicleControllerProperties var2) {
      super(var1, var2);
   }

   public void loadProperties() {
      super.loadProperties();
      this.minHeight = this.properties.getDoubleProperty("drilling-min-height", -1.0D);
      this.maxHeight = this.properties.getDoubleProperty("drilling-max-height", -1.0D);
      this.width = this.properties.getDoubleProperty("drilling-width", -1.0D);
      this.cooldown = this.properties.getFloatProperty("drilling-cooldown", 0.0F);
      this.depth = this.properties.getDoubleProperty("drilling-depth", 5.0D);
      String var1 = this.properties.getStringProperty("drilling-type", "CONE");

      try {
         this.drillingType = DrillGroundVehicleController.DrillingType.valueOf(var1.toUpperCase());
      } catch (IllegalArgumentException var3) {
         this.drillingType = DrillGroundVehicleController.DrillingType.CONE;
      }

   }

   public void tick() {
      super.tick();
      if (this.forward || this.backward || this.turningLeft || this.turningRight) {
         this.verifyValues();
         BlockFace var1 = getDirection(this.vehicle.getLocation().getDirection());
         List var2 = this.locateBlocks(var1);
         Run.sync(() -> {
            var2.forEach(Block::breakNaturally);
         });
      }
   }

   @NotNull
   private List<Block> locateBlocks(BlockFace var1) {
      LinkedList var2 = new LinkedList();
      double var3 = (double)this.vehicle.getCurrentHitBox().getBlockMinY();

      for(int var5 = 0; (double)var5 < this.depth; ++var5) {
         double var6 = (double)(this.vehicle.getLocation().getBlockX() + var1.getModX() * var5);
         double var8 = (double)(this.vehicle.getLocation().getBlockZ() + var1.getModZ() * var5);
         switch(this.drillingType.ordinal()) {
         case 0:
            this.addSquareBlocks(var2, var6, var8, var3);
            break;
         case 1:
            this.addCircleBlocks(var2, var6, var8, var3);
            break;
         case 2:
            this.addRectangleBlocks(var2, var6, var8, var3);
            break;
         case 3:
            this.addConeBlocks(var2, var6, var8, var3, var5);
            break;
         case 4:
            this.addPyramidBlocks(var2, var6, var8, var3, var5);
            break;
         case 5:
            this.addSpiralBlocks(var2, var6, var8, var3, var5);
            break;
         case 6:
            this.addCrossBlocks(var2, var6, var8, var3);
            break;
         case 7:
            this.addDiamondBlocks(var2, var6, var8, var3);
            break;
         case 8:
            this.addStarBlocks(var2, var6, var8, var3);
            break;
         case 9:
            this.addHexagonBlocks(var2, var6, var8, var3);
         }
      }

      return var2;
   }

   private void addSquareBlocks(List<Block> var1, double var2, double var4, double var6) {
      for(double var8 = FastMath.min(this.minHeight, this.maxHeight); var8 <= FastMath.max(this.minHeight, this.maxHeight); ++var8) {
         for(double var10 = -this.width; var10 <= this.width; ++var10) {
            for(double var12 = -this.width; var12 <= this.width; ++var12) {
               this.addBlockIfSolid(var1, var2 + var10, this.vehicle.getLocation().getY() + var8, var4 + var12);
            }
         }
      }

   }

   private void addCircleBlocks(List<Block> var1, double var2, double var4, double var6) {
      double var8 = this.width;

      for(double var10 = FastMath.min(this.minHeight, this.maxHeight); var10 <= FastMath.max(this.minHeight, this.maxHeight); ++var10) {
         for(double var12 = -var8; var12 <= var8; ++var12) {
            for(double var14 = -var8; var14 <= var8; ++var14) {
               double var16 = Math.sqrt(var12 * var12 + var14 * var14);
               if (var16 <= var8) {
                  this.addBlockIfSolid(var1, var2 + var12, this.vehicle.getLocation().getY() + var10, var4 + var14);
               }
            }
         }
      }

   }

   private void addRectangleBlocks(List<Block> var1, double var2, double var4, double var6) {
      double var8 = this.width;
      double var10 = this.width * 0.6D;

      for(double var12 = FastMath.min(this.minHeight, this.maxHeight); var12 <= FastMath.max(this.minHeight, this.maxHeight); ++var12) {
         for(double var14 = -var8; var14 <= var8; ++var14) {
            for(double var16 = -var10; var16 <= var10; ++var16) {
               this.addBlockIfSolid(var1, var2 + var14, this.vehicle.getLocation().getY() + var12, var4 + var16);
            }
         }
      }

   }

   private void addConeBlocks(List<Block> var1, double var2, double var4, double var6, int var8) {
      double var9 = 1.0D - (double)var8 / this.depth;
      double var11 = this.width * var9;
      int var13 = (int)Math.floor(var6 - this.minHeight);
      int var14 = (int)Math.floor(var6 + this.maxHeight);
      int var15 = (int)Math.floor(this.vehicle.getY());

      for(int var16 = var13; var16 <= var14; ++var16) {
         if (var16 >= var15) {
            for(int var17 = (int)Math.floor(-var11); (double)var17 <= var11; ++var17) {
               for(int var18 = (int)Math.floor(-var11); (double)var18 <= var11; ++var18) {
                  if ((double)(var17 * var17 + var18 * var18) <= var11 * var11) {
                     this.addBlockIfSolid(var1, var2 + (double)var17, (double)var16, var4 + (double)var18);
                  }
               }
            }
         }
      }

   }

   private void addPyramidBlocks(List<Block> var1, double var2, double var4, double var6, int var8) {
      double var9 = 1.0D - (double)var8 / this.depth;
      double var11 = this.width * var9;

      for(double var13 = FastMath.min(this.minHeight, this.maxHeight); var13 <= FastMath.max(this.minHeight, this.maxHeight); ++var13) {
         for(double var15 = -var11; var15 <= var11; ++var15) {
            for(double var17 = -var11; var17 <= var11; ++var17) {
               this.addBlockIfSolid(var1, var2 + var15, this.vehicle.getLocation().getY() + var13, var4 + var17);
            }
         }
      }

   }

   private void addSpiralBlocks(List<Block> var1, double var2, double var4, double var6, int var8) {
      double var9 = (double)var8 / this.depth * 3.141592653589793D * 4.0D;
      double var11 = this.width * (1.0D - (double)var8 / this.depth);
      double var13 = Math.cos(var9) * var11;
      double var15 = Math.sin(var9) * var11;

      for(double var17 = FastMath.min(this.minHeight, this.maxHeight); var17 <= FastMath.max(this.minHeight, this.maxHeight); ++var17) {
         double var19 = this.width * 0.3D;

         for(double var21 = 0.0D; var21 <= var19; var21 += 0.5D) {
            for(double var23 = 0.0D; var23 < 6.283185307179586D; var23 += 0.7853981633974483D) {
               double var25 = var13 + Math.cos(var23) * var21;
               double var27 = var15 + Math.sin(var23) * var21;
               this.addBlockIfSolid(var1, var2 + var25, this.vehicle.getLocation().getY() + var17, var4 + var27);
            }
         }
      }

   }

   private void addCrossBlocks(List<Block> var1, double var2, double var4, double var6) {
      double var8 = this.width * 0.3D;

      for(double var10 = FastMath.min(this.minHeight, this.maxHeight); var10 <= FastMath.max(this.minHeight, this.maxHeight); ++var10) {
         double var12;
         double var14;
         for(var12 = -this.width; var12 <= this.width; ++var12) {
            for(var14 = -var8; var14 <= var8; ++var14) {
               this.addBlockIfSolid(var1, var2 + var12, this.vehicle.getLocation().getY() + var10, var4 + var14);
            }
         }

         for(var12 = -this.width; var12 <= this.width; ++var12) {
            for(var14 = -var8; var14 <= var8; ++var14) {
               this.addBlockIfSolid(var1, var2 + var14, this.vehicle.getLocation().getY() + var10, var4 + var12);
            }
         }
      }

   }

   private void addDiamondBlocks(List<Block> var1, double var2, double var4, double var6) {
      for(double var8 = FastMath.min(this.minHeight, this.maxHeight); var8 <= FastMath.max(this.minHeight, this.maxHeight); ++var8) {
         for(double var10 = -this.width; var10 <= this.width; ++var10) {
            for(double var12 = -this.width; var12 <= this.width; ++var12) {
               if (Math.abs(var10) + Math.abs(var12) <= this.width) {
                  this.addBlockIfSolid(var1, var2 + var10, this.vehicle.getLocation().getY() + var8, var4 + var12);
               }
            }
         }
      }

   }

   private void addStarBlocks(List<Block> var1, double var2, double var4, double var6) {
      byte var8 = 5;
      double var9 = this.width;
      double var11 = this.width * 0.4D;

      for(double var13 = FastMath.min(this.minHeight, this.maxHeight); var13 <= FastMath.max(this.minHeight, this.maxHeight); ++var13) {
         for(double var15 = -var9; var15 <= var9; ++var15) {
            for(double var17 = -var9; var17 <= var9; ++var17) {
               if (this.isInStar(var15, var17, var8, var9, var11)) {
                  this.addBlockIfSolid(var1, var2 + var15, this.vehicle.getLocation().getY() + var13, var4 + var17);
               }
            }
         }
      }

   }

   private void addHexagonBlocks(List<Block> var1, double var2, double var4, double var6) {
      double var8 = this.width;

      for(double var10 = FastMath.min(this.minHeight, this.maxHeight); var10 <= FastMath.max(this.minHeight, this.maxHeight); ++var10) {
         for(double var12 = -var8; var12 <= var8; ++var12) {
            for(double var14 = -var8; var14 <= var8; ++var14) {
               if (this.isInHexagon(var12, var14, var8)) {
                  this.addBlockIfSolid(var1, var2 + var12, this.vehicle.getLocation().getY() + var10, var4 + var14);
               }
            }
         }
      }

   }

   private boolean isInStar(double var1, double var3, int var5, double var6, double var8) {
      double var10 = Math.atan2(var3, var1);
      if (var10 < 0.0D) {
         var10 += 6.283185307179586D;
      }

      double var12 = 6.283185307179586D / (double)(var5 * 2);
      int var14 = (int)(var10 / var12);
      boolean var15 = var14 % 2 == 0;
      double var16 = Math.sqrt(var1 * var1 + var3 * var3);
      return var16 <= (var15 ? var6 : var8);
   }

   private boolean isInHexagon(double var1, double var3, double var5) {
      double var7 = Math.sqrt(3.0D) / 3.0D * var1 - 0.3333333333333333D * var3;
      double var9 = 0.6666666666666666D * var3;
      return Math.abs(var7) <= var5 && Math.abs(var9) <= var5 && Math.abs(var7 + var9) <= var5;
   }

   private void addBlockIfSolid(List<Block> var1, double var2, double var4, double var6) {
      Location var8 = new Location(this.vehicle.getWorld(), var2, var4, var6);
      Block var9 = var8.getBlock();
      if (var9.getType().isSolid()) {
         var1.add(var9);
      }

   }

   private void verifyValues() {
      if (this.minHeight == -1.0D) {
         this.minHeight = 0.0D;
      }

      if (this.maxHeight == -1.0D) {
         this.maxHeight = this.vehicle.getCurrentHitBox().getHeight();
      }

      if (this.width == -1.0D) {
         this.width = this.vehicle.getCurrentHitBox().getWidth();
      }

   }

   public static BlockFace getDirection(Vector var0) {
      BlockFace var1 = BlockFace.SELF;
      float var2 = Float.MAX_VALUE;
      BlockFace[] var4 = BlockFace.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFace var7 = var4[var6];
         if (var7 != BlockFace.SELF) {
            float var3 = var0.angle(var7.getDirection());
            if (!Float.isNaN(var3) && var3 < var2) {
               var2 = var3;
               var1 = var7;
            }
         }
      }

      return var1;
   }

   @Internal
   public static enum DrillingType {
      SQUARE,
      CIRCLE,
      RECTANGLE,
      CONE,
      PYRAMID,
      SPIRAL,
      CROSS,
      DIAMOND,
      STAR,
      HEXAGON;

      // $FF: synthetic method
      private static DrillGroundVehicleController.DrillingType[] $values() {
         return new DrillGroundVehicleController.DrillingType[]{SQUARE, CIRCLE, RECTANGLE, CONE, PYRAMID, SPIRAL, CROSS, DIAMOND, STAR, HEXAGON};
      }
   }
}
