package advancedplugins.pm2.cv.models.core.util;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelHelper {
   public static Vector3f calculateJointScale(ProcessedJoint.Cube var0) {
      float var1 = (float)((var0.getTo().x - var0.getFrom().x) / 16.0D);
      float var2 = (float)((var0.getTo().y - var0.getFrom().y) / 16.0D);
      float var3 = (float)((var0.getTo().z - var0.getFrom().z) / 16.0D);
      return new Vector3f(var1, var2, var3);
   }

   public static Vector3f calculateJointPosition(ProcessedJoint.Cube var0) {
      return new Vector3f((float)(var0.getOrigin().x / 16.0D), (float)(var0.getOrigin().y / 16.0D), (float)(var0.getOrigin().z / 16.0D));
   }

   public static Quaternionf calculateJointRotation(ProcessedJoint.Cube var0) {
      Quaternionf var1 = (new Quaternionf()).rotateY((float)Math.toRadians(120.0D));
      Vector3f var2 = new Vector3f((float)(var0.getOrigin().x / 16.0D), (float)(var0.getOrigin().y / 16.0D), (float)(var0.getOrigin().z / 16.0D));
      Vector3f var3 = new Vector3f((float)(var0.getFrom().x / 16.0D) - var2.x(), (float)(var0.getFrom().y / 16.0D) - var2.y(), (float)(var0.getFrom().z / 16.0D) - var2.z());
      Quaternionf var4 = getQuaternionf(new Vector3f());
      Vector3f var5 = new Vector3f(var3);
      var4.transform(var5);
      Vector3f var6 = new Vector3f(var5);
      Vector3f var7 = new Vector3f(var2);
      var1.transform(var6);
      var1.transform(var7);
      return (new Quaternionf(var1)).mul(var4);
   }

   @NotNull
   public static Quaternionf getQuaternionf(Vector3f var0) {
      return var0 == null ? new Quaternionf() : (new Quaternionf()).rotateXYZ((float)Math.toRadians((double)var0.x()), (float)Math.toRadians((double)var0.y()), (float)Math.toRadians((double)var0.z()));
   }

   @NotNull
   public static Quaternionf getQuaternionf(BlockbenchModel.Cube var0) {
      Quaternionf var1 = new Quaternionf();
      if (var0.getRotation() == null) {
         return var1;
      } else if (var0.getRotation().length == 0) {
         return var1;
      } else {
         List var2 = List.of(var0.getRotation()[0], var0.getRotation()[1], var0.getRotation()[2]);
         double var3 = (double)(Float)var2.get(0);
         double var5 = (double)(Float)var2.get(1);
         double var7 = (double)(Float)var2.get(2);
         var1.rotationXYZ((float)Math.toRadians(var3), (float)Math.toRadians(var5), (float)Math.toRadians(var7));
         return var1;
      }
   }

   public static ModelHelper.PlacementResult calculatePlacement(Block var0) {
      Location var1 = determineTargetLocation(var0, BlockFace.UP);
      if (var1 == null) {
         return createFailureResult();
      } else if (isDownwardPlacement(BlockFace.UP) && !isLocationEmpty(var1)) {
         return createFailureResult();
      } else {
         BlockFace var2 = getCardinalDirection(90.0F);
         return createSuccessResult(var1, var2);
      }
   }

   private static Location determineTargetLocation(Block var0, BlockFace var1) {
      return var1 != BlockFace.UP && var1.getModY() != 0 && var1 != BlockFace.DOWN ? null : var0.getRelative(var1).getLocation();
   }

   private static ModelHelper.PlacementResult createFailureResult() {
      return new ModelHelper.PlacementResult(false, (Location)null, (BlockFace)null);
   }

   private static ModelHelper.PlacementResult createSuccessResult(Location var0, BlockFace var1) {
      return new ModelHelper.PlacementResult(true, var0, var1);
   }

   private static boolean isDownwardPlacement(BlockFace var0) {
      return var0 == BlockFace.DOWN;
   }

   private static boolean isLocationEmpty(Location var0) {
      return var0.getBlock().getType() == Material.AIR;
   }

   public static Location getCenteredLocation(Location var0, BlockFace var1) {
      var0 = var0.clone();
      switch(var1) {
      case NORTH:
         var0.add(0.0D, 0.0D, 0.0D);
         break;
      case EAST:
         var0.add(1.0D, 0.0D, 0.0D);
         break;
      case SOUTH:
         var0.add(1.0D, 0.0D, 1.0D);
         break;
      case WEST:
         var0.add(0.0D, 0.0D, 1.0D);
      }

      return var0;
   }

   public static float getYawFromFacing(BlockFace var0) {
      float var10000;
      switch(var0) {
      case NORTH:
         var10000 = 0.0F;
         break;
      case EAST:
         var10000 = -90.0F;
         break;
      case SOUTH:
         var10000 = 180.0F;
         break;
      case WEST:
         var10000 = 90.0F;
         break;
      default:
         var10000 = 0.0F;
      }

      return var10000;
   }

   private static BlockFace getCardinalDirection(float var0) {
      if (var0 < 0.0F) {
         var0 += 360.0F;
      }

      if ((var0 %= 360.0F) <= 45.0F) {
         return BlockFace.NORTH;
      } else if (var0 <= 135.0F) {
         return BlockFace.EAST;
      } else if (var0 <= 225.0F) {
         return BlockFace.SOUTH;
      } else {
         return var0 <= 315.0F ? BlockFace.WEST : BlockFace.NORTH;
      }
   }

   public static class PlacementResult {
      private final boolean valid;
      private final Location location;
      private final BlockFace face;

      public PlacementResult(boolean var1, Location var2, BlockFace var3) {
         this.valid = var1;
         this.location = var2;
         this.face = var3;
      }

      public boolean valid() {
         return this.valid;
      }

      public Location location() {
         return this.location;
      }

      public BlockFace face() {
         return this.face;
      }
   }
}
