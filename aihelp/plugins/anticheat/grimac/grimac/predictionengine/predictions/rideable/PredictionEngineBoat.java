package ac.grim.grimac.predictionengine.predictions.rideable;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.VehicleData;
import ac.grim.grimac.utils.enums.BoatEntityStatus;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PredictionEngineBoat extends PredictionEngine {
   public PredictionEngineBoat(GrimPlayer player) {
      player.uncertaintyHandler.collidingEntities.add(0);
      player.vehicleData.midTickY = 0.0D;
      player.vehicleData.oldStatus = player.vehicleData.status;
      player.vehicleData.status = getStatus(player);
   }

   private static BoatEntityStatus getStatus(GrimPlayer player) {
      BoatEntityStatus status = isUnderwater(player);
      if (status != null) {
         player.vehicleData.waterLevel = player.boundingBox.maxY;
         return status;
      } else if (checkInWater(player)) {
         return BoatEntityStatus.IN_WATER;
      } else {
         float friction = getGroundFriction(player);
         if (friction > 0.0F) {
            player.vehicleData.landFriction = friction;
            return BoatEntityStatus.ON_LAND;
         } else {
            return BoatEntityStatus.IN_AIR;
         }
      }
   }

   @Nullable
   private static BoatEntityStatus isUnderwater(@NotNull GrimPlayer player) {
      SimpleCollisionBox box = player.boundingBox;
      double maxBoxY = box.maxY + 0.001D;
      int minX = GrimMath.floor(box.minX);
      int maxX = GrimMath.ceil(box.maxX);
      int minY = GrimMath.floor(box.maxY);
      int maxY = GrimMath.ceil(maxBoxY);
      int minZ = GrimMath.floor(box.minZ);
      int maxZ = GrimMath.ceil(box.maxZ);
      boolean underWater = false;

      for(int x = minX; x < maxX; ++x) {
         for(int y = minY; y < maxY; ++y) {
            for(int z = minZ; z < maxZ; ++z) {
               double level = player.compensatedWorld.getWaterFluidLevelAt(x, y, z);
               if (maxBoxY < (double)y + level) {
                  if (!player.compensatedWorld.isWaterSourceBlock(x, y, z)) {
                     return BoatEntityStatus.UNDER_FLOWING_WATER;
                  }

                  underWater = true;
               }
            }
         }
      }

      return underWater ? BoatEntityStatus.UNDER_WATER : null;
   }

   private static boolean checkInWater(GrimPlayer grimPlayer) {
      SimpleCollisionBox box = grimPlayer.boundingBox;
      int minX = GrimMath.floor(box.minX);
      int maxX = GrimMath.ceil(box.maxX);
      int minY = GrimMath.floor(box.minY);
      int maxY = GrimMath.ceil(box.minY + 0.001D);
      int minZ = GrimMath.floor(box.minZ);
      int maxZ = GrimMath.ceil(box.maxZ);
      boolean inWater = false;
      grimPlayer.vehicleData.waterLevel = -1.7976931348623157E308D;

      for(int x = minX; x < maxX; ++x) {
         for(int y = minY; y < maxY; ++y) {
            for(int z = minZ; z < maxZ; ++z) {
               double level = grimPlayer.compensatedWorld.getWaterFluidLevelAt(x, y, z);
               if (level > 0.0D) {
                  float f = (float)((double)((float)y) + level);
                  grimPlayer.vehicleData.waterLevel = Math.max((double)f, grimPlayer.vehicleData.waterLevel);
                  inWater |= box.minY < (double)f;
               }
            }
         }
      }

      return inWater;
   }

   public static float getGroundFriction(GrimPlayer player) {
      SimpleCollisionBox playerBox = player.boundingBox;
      SimpleCollisionBox box = new SimpleCollisionBox(playerBox.minX, playerBox.minY - 0.001D, playerBox.minZ, playerBox.maxX, playerBox.minY, playerBox.maxZ, false);
      int minX = (int)(Math.floor(box.minX) - 1.0D);
      int maxX = (int)(Math.ceil(box.maxX) + 1.0D);
      int minY = (int)(Math.floor(box.minY) - 1.0D);
      int maxY = (int)(Math.ceil(box.maxY) + 1.0D);
      int minZ = (int)(Math.floor(box.minZ) - 1.0D);
      int maxZ = (int)(Math.ceil(box.maxZ) + 1.0D);
      float friction = 0.0F;
      int blocks = 0;

      for(int x = minX; x < maxX; ++x) {
         for(int z = minZ; z < maxZ; ++z) {
            int j2 = (x != minX && x != maxX - 1 ? 0 : 1) + (z != minZ && z != maxZ - 1 ? 0 : 1);
            if (j2 != 2) {
               for(int y = minY; y < maxY; ++y) {
                  if (j2 != 1 || y != minY && y != maxY - 1) {
                     WrappedBlockState blockData = player.compensatedWorld.getBlock(x, y, z);
                     StateType blockMaterial = blockData.getType();
                     if (blockMaterial != StateTypes.LILY_PAD && CollisionData.getData(blockMaterial).getMovementCollisionBox(player, player.getClientVersion(), blockData, x, y, z).isIntersected(box)) {
                        friction += BlockProperties.getMaterialFriction(player, blockMaterial);
                        ++blocks;
                     }
                  }
               }
            }
         }
      }

      return friction / (float)blocks;
   }

   public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
      List<VectorData> vectors = new ArrayList();
      Iterator var5 = possibleVectors.iterator();

      while(var5.hasNext()) {
         VectorData data = (VectorData)var5.next();
         data.input = new Vector3dm(player.vehicleData.vehicleForward, 0.0F, player.vehicleData.vehicleHorizontal);

         for(int applyStuckSpeed = 1; applyStuckSpeed >= 0 && (applyStuckSpeed != 0 || !player.isForceStuckSpeed()); --applyStuckSpeed) {
            if (player.vehicleData.vehicleForward == 0.0F) {
               Vector3dm vector = data.vector.clone();
               this.controlBoat(player, vector, true);
               if (applyStuckSpeed != 0) {
                  vector.multiply(player.stuckSpeedMultiplier);
               }

               vectors.add(data.returnNewModified(vector, VectorData.VectorType.InputResult));
            }

            this.controlBoat(player, data.vector, false);
            if (applyStuckSpeed != 0) {
               data.vector.multiply(player.stuckSpeedMultiplier);
            }

            vectors.add(data);
         }
      }

      return vectors;
   }

   public Set<VectorData> fetchPossibleStartTickVectors(GrimPlayer player) {
      Set<VectorData> vectors = player.getPossibleVelocities();
      this.addFluidPushingToStartingVectors(player, vectors);
      Iterator var3 = vectors.iterator();

      while(var3.hasNext()) {
         VectorData data = (VectorData)var3.next();
         this.floatBoat(player, data.vector);
      }

      return vectors;
   }

   public void endOfTick(GrimPlayer player, double d) {
      super.endOfTick(player, d);
      Collisions.handleInsideBlocks(player);
      Collisions.applyEffectsFromBlocks(player);
      Collisions.applyEffectsFromBlocks(player);
   }

   public boolean canSwimHop(GrimPlayer player) {
      return false;
   }

   private void floatBoat(GrimPlayer player, Vector3dm vector) {
      double d1 = player.hasGravity ? -0.03999999910593033D : 0.0D;
      double d2 = 0.0D;
      float invFriction = 0.05F;
      if (player.vehicleData.oldStatus == BoatEntityStatus.IN_AIR && player.vehicleData.status != BoatEntityStatus.IN_AIR && player.vehicleData.status != BoatEntityStatus.ON_LAND) {
         player.vehicleData.waterLevel = player.lastY + player.boundingBox.maxY - player.boundingBox.minY;
         player.lastY = (double)(this.getWaterLevelAbove(player) - 0.5625F) + 0.101D;
         player.boundingBox = GetBoundingBox.getCollisionBoxForPlayer(player, player.lastX, player.lastY, player.lastZ);
         player.actualMovement = new Vector3dm(player.x - player.lastX, player.y - player.lastY, player.z - player.lastZ);
         vector.setY(0);
         player.vehicleData.lastYd = 0.0D;
         player.vehicleData.status = BoatEntityStatus.IN_WATER;
      } else {
         if (player.vehicleData.status == BoatEntityStatus.IN_WATER) {
            d2 = (player.vehicleData.waterLevel - player.lastY) / (player.boundingBox.maxY - player.boundingBox.minY);
            invFriction = 0.9F;
         } else if (player.vehicleData.status == BoatEntityStatus.UNDER_FLOWING_WATER) {
            d1 = -7.0E-4D;
            invFriction = 0.9F;
         } else if (player.vehicleData.status == BoatEntityStatus.UNDER_WATER) {
            d2 = 0.009999999776482582D;
            invFriction = 0.45F;
         } else if (player.vehicleData.status == BoatEntityStatus.IN_AIR) {
            invFriction = 0.9F;
         } else if (player.vehicleData.status == BoatEntityStatus.ON_LAND) {
            invFriction = player.vehicleData.landFriction;
            VehicleData var10000 = player.vehicleData;
            var10000.landFriction /= 2.0F;
         }

         vector.setX(vector.getX() * (double)invFriction);
         vector.setY(vector.getY() + d1);
         vector.setZ(vector.getZ() * (double)invFriction);
         if (d2 > 0.0D) {
            double yVel = vector.getY();
            vector.setY((yVel + d2 * 0.06153846016296973D) * 0.75D);
         }
      }

   }

   public float getWaterLevelAbove(GrimPlayer player) {
      SimpleCollisionBox axisalignedbb = player.boundingBox;
      int i = (int)Math.floor(axisalignedbb.minX);
      int j = (int)Math.ceil(axisalignedbb.maxX);
      int k = (int)Math.floor(axisalignedbb.maxY);
      int l = (int)Math.ceil(axisalignedbb.maxY - player.vehicleData.lastYd);
      int i1 = (int)Math.floor(axisalignedbb.minZ);
      int j1 = (int)Math.ceil(axisalignedbb.maxZ);

      label35:
      for(int k1 = k; k1 < l; ++k1) {
         float f = 0.0F;

         for(int l1 = i; l1 < j; ++l1) {
            for(int i2 = i1; i2 < j1; ++i2) {
               double level = player.compensatedWorld.getWaterFluidLevelAt(l1, k1, i2);
               f = (float)Math.max((double)f, level);
               if (f >= 1.0F) {
                  continue label35;
               }
            }
         }

         if (f < 1.0F) {
            return (float)k1 + f;
         }
      }

      return (float)(l + 1);
   }

   private void controlBoat(GrimPlayer player, Vector3dm vector, boolean intermediate) {
      float f = 0.0F;
      if (player.vehicleData.vehicleHorizontal != 0.0F && !intermediate && player.vehicleData.vehicleForward == 0.0F) {
         f += 0.005F;
      }

      if (intermediate || (double)player.vehicleData.vehicleForward > 0.1D) {
         f += 0.04F;
      }

      if (intermediate || (double)player.vehicleData.vehicleForward < -0.01D) {
         f -= 0.005F;
      }

      vector.add(new Vector3dm((double)(player.trigHandler.sin(GrimMath.radians(-player.yaw)) * f), 0.0D, (double)(player.trigHandler.cos(GrimMath.radians(player.yaw)) * f)));
   }
}
