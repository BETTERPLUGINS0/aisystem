package ac.grim.grimac.predictionengine;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.enums.FluidTag;
import ac.grim.grimac.utils.enums.Pose;
import ac.grim.grimac.utils.latency.CompensatedEntities;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import ac.grim.grimac.utils.nmsutil.CheckIfChunksLoaded;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.FluidTypeFlowing;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import java.util.Optional;
import lombok.Generated;

public final class PlayerBaseTick {
   private static final boolean SERVER_SUPPORT_ENVIRONMENT_ATTRIBUTES;

   public static boolean canEnterPose(GrimPlayer player, Pose pose, double x, double y, double z) {
      return Collisions.isEmpty(player, getBoundingBoxForPose(player, pose, x, y, z).expand(-1.0E-7D));
   }

   private static SimpleCollisionBox getBoundingBoxForPose(GrimPlayer player, Pose pose, double x, double y, double z) {
      float scale = (float)player.compensatedEntities.self.getAttributeValue(Attributes.SCALE);
      float width = pose.width * scale;
      float height = pose.height * scale;
      float radius = width / 2.0F;
      return new SimpleCollisionBox(x - (double)radius, y, z - (double)radius, x + (double)radius, y + (double)height, z + (double)radius, false);
   }

   public static void doBaseTick(GrimPlayer player) {
      player.baseTickAddition = new Vector3dm();
      player.baseTickWaterPushing = new Vector3dm();
      Vector3dm waterPushVector;
      if (player.isFlying && player.isSneaking && !player.inVehicle()) {
         waterPushVector = new Vector3dm(0.0F, player.flySpeed * -3.0F, 0.0F);
         player.baseTickAddVector(waterPushVector);
         player.trackBaseTickAddition(waterPushVector);
      }

      updateInWaterStateAndDoFluidPushing(player);
      updateFluidOnEyes(player);
      updateSwimming(player);
      if (player.wasTouchingLava) {
         player.fallDistance *= 0.5D;
      }

      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && player.wasTouchingWater && player.isSneaking && !player.isFlying && !player.inVehicle()) {
         waterPushVector = new Vector3dm(0.0F, -0.04F, 0.0F);
         player.baseTickAddVector(waterPushVector);
         player.trackBaseTickAddition(waterPushVector);
      }

      player.lastPose = player.pose;
      if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
         player.isSlowMovement = player.isSneaking;
      } else {
         player.isSlowMovement = !player.wasFlying && !player.isSwimming && canEnterPose(player, Pose.CROUCHING, player.lastX, player.lastY, player.lastZ) && (player.wasSneaking || !player.isInBed && !canEnterPose(player, Pose.STANDING, player.lastX, player.lastY, player.lastZ)) || (player.pose == Pose.SWIMMING || !player.isGliding && player.pose == Pose.FALL_FLYING) && !player.wasTouchingWater;
         if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_14_4)) {
            player.isSlowMovement |= player.isSneaking;
         }
      }

      if (player.inVehicle()) {
         player.isSlowMovement = false;
      }

      if (!player.inVehicle()) {
         moveTowardsClosestSpace(player, player.lastX - (player.boundingBox.maxX - player.boundingBox.minX) * 0.35D, player.lastZ + (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35D);
         moveTowardsClosestSpace(player, player.lastX - (player.boundingBox.maxX - player.boundingBox.minX) * 0.35D, player.lastZ - (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35D);
         moveTowardsClosestSpace(player, player.lastX + (player.boundingBox.maxX - player.boundingBox.minX) * 0.35D, player.lastZ - (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35D);
         moveTowardsClosestSpace(player, player.lastX + (player.boundingBox.maxX - player.boundingBox.minX) * 0.35D, player.lastZ + (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35D);
      }

      if (player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
         updatePlayerSize(player);
      }

   }

   private static void updateFluidOnEyes(GrimPlayer player) {
      player.wasEyeInWater = player.fluidOnEyes == FluidTag.WATER;
      player.fluidOnEyes = null;
      double d0 = player.lastY + player.getEyeHeight() - 0.1111111119389534D;
      PacketEntity riding = player.compensatedEntities.self.getRiding();
      if (riding == null || !riding.isBoat || player.vehicleData.boatUnderwater || !(player.boundingBox.maxY >= d0) || !(player.boundingBox.minY <= d0)) {
         double d1 = (double)((float)Math.floor(d0)) + player.compensatedWorld.getWaterFluidLevelAt(player.lastX, d0, player.lastZ);
         if (d1 > d0) {
            player.fluidOnEyes = FluidTag.WATER;
            if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2)) {
               player.wasEyeInWater = true;
            }

         } else {
            if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2)) {
               player.wasEyeInWater = false;
            }

            d1 = (double)((float)Math.floor(d0)) + player.compensatedWorld.getWaterFluidLevelAt(player.lastX, d0, player.lastZ);
            if (d1 > d0) {
               player.fluidOnEyes = FluidTag.LAVA;
            }

         }
      }
   }

   private static void updateInWaterStateAndDoFluidPushing(GrimPlayer player) {
      player.fluidHeight.clear();
      updateInWaterStateAndDoWaterCurrentPushing(player);
      boolean fastLava = SERVER_SUPPORT_ENVIRONMENT_ATTRIBUTES && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_11) ? (Boolean)player.dimensionType.getAttributes().getOrDefault(EnvironmentAttributes.GAMEPLAY_FAST_LAVA) : player.dimensionType.isUltraWarm();
      double multiplier = fastLava ? 0.007D : 0.0023333333333333335D;
      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16)) {
         player.wasTouchingLava = updateFluidHeightAndDoFluidPushing(player, FluidTag.LAVA, multiplier);
      } else if (player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
         SimpleCollisionBox playerBox = player.boundingBox.copy().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D);
         player.wasTouchingLava = player.compensatedWorld.containsLava(playerBox);
      }

   }

   public static void updatePowderSnow(GrimPlayer player) {
      if (!player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
         ValuedAttribute playerSpeed = (ValuedAttribute)player.compensatedEntities.self.getAttribute(Attributes.MOVEMENT_SPEED).orElseThrow();
         Optional<WrapperPlayServerUpdateAttributes.Property> property = playerSpeed.property();
         if (!property.isEmpty()) {
            ((WrapperPlayServerUpdateAttributes.Property)property.get()).getModifiers().removeIf((modifier) -> {
               return modifier.getUUID().equals(CompensatedEntities.SNOW_MODIFIER_UUID) || modifier.getName().getKey().equals("powder_snow");
            });
            playerSpeed.recalculate();
            StateType type = BlockProperties.getOnPos(player, player.mainSupportingBlockData, new Vector3d(player.x, player.y, player.z));
            if (!type.isAir()) {
               int i = player.powderSnowFrozenTicks;
               if (i > 0) {
                  int ticksToFreeze = 140;
                  float percentFrozen = (float)Math.min(i, ticksToFreeze) / (float)ticksToFreeze;
                  float percentFrozenReducedToSpeed = -0.05F * percentFrozen;
                  ((WrapperPlayServerUpdateAttributes.Property)property.get()).getModifiers().add(new WrapperPlayServerUpdateAttributes.PropertyModifier(CompensatedEntities.SNOW_MODIFIER_UUID, (double)percentFrozenReducedToSpeed, WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.ADDITION));
                  playerSpeed.recalculate();
               }
            }

         }
      }
   }

   public static void updatePlayerPose(GrimPlayer player) {
      if (canEnterPose(player, Pose.SWIMMING, player.x, player.y, player.z)) {
         Pose pose;
         if (player.isGliding) {
            pose = Pose.FALL_FLYING;
         } else if (player.isInBed) {
            pose = Pose.SLEEPING;
         } else if (player.isSwimming) {
            pose = Pose.SWIMMING;
         } else if (player.isRiptidePose) {
            pose = Pose.SPIN_ATTACK;
         } else if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && player.getClientVersion().isOlderThan(ClientVersion.V_1_14) && player.isSneaking) {
            pose = Pose.NINE_CROUCHING;
         } else if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && player.isSneaking && !player.isFlying) {
            pose = Pose.CROUCHING;
         } else {
            pose = Pose.STANDING;
         }

         if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && !player.inVehicle() && !canEnterPose(player, pose, player.x, player.y, player.z)) {
            if (canEnterPose(player, Pose.CROUCHING, player.x, player.y, player.z)) {
               pose = Pose.CROUCHING;
            } else {
               pose = Pose.SWIMMING;
            }
         }

         player.pose = pose;
         player.boundingBox = getBoundingBoxForPose(player, player.pose, player.x, player.y, player.z);
      }

   }

   private static void updatePlayerSize(GrimPlayer player) {
      Pose pose;
      if (player.isGliding) {
         pose = Pose.FALL_FLYING;
      } else if (player.isInBed) {
         pose = Pose.SLEEPING;
      } else if (!player.isSwimming && !player.isRiptidePose) {
         if (player.isSneaking && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            pose = Pose.NINE_CROUCHING;
         } else {
            pose = Pose.STANDING;
         }
      } else {
         pose = Pose.SWIMMING;
      }

      if (pose != player.pose) {
         Pose oldPose = player.pose;
         player.pose = pose;
         SimpleCollisionBox box = GetBoundingBox.getCollisionBoxForPlayer(player, player.lastX, player.lastY, player.lastZ);
         boolean collides = !Collisions.isEmpty(player, box);
         if (collides) {
            player.pose = oldPose;
            return;
         }
      }

      player.boundingBox = GetBoundingBox.getCollisionBoxForPlayer(player, player.lastX, player.lastY, player.lastZ);
   }

   private static void updateSwimming(GrimPlayer player) {
      if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         player.isSwimming = false;
      } else if (player.isFlying) {
         player.isSwimming = false;
      } else if (player.inVehicle()) {
         player.isSwimming = false;
      } else if (player.isSwimming) {
         player.isSwimming = player.lastSprinting && player.wasTouchingWater;
      } else {
         boolean feetInWater = player.getClientVersion().isOlderThan(ClientVersion.V_1_17) || player.compensatedWorld.getWaterFluidLevelAt(player.lastX, player.lastY, player.lastZ) > 0.0D;
         player.isSwimming = player.lastSprinting && player.wasEyeInWater && player.wasTouchingWater && feetInWater;
      }

   }

   private static void moveTowardsClosestSpace(GrimPlayer player, double xPosition, double zPosition) {
      double movementThreshold = player.getMovementThreshold();
      player.boundingBox = player.boundingBox.expand(movementThreshold, 0.0D, movementThreshold);
      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
         moveTowardsClosestSpaceModern(player, xPosition, zPosition);
      } else {
         moveTowardsClosestSpaceLegacy(player, xPosition, zPosition);
      }

      player.boundingBox = player.boundingBox.expand(-movementThreshold, 0.0D, -movementThreshold);
   }

   private static void moveTowardsClosestSpaceLegacy(GrimPlayer player, double x, double z) {
      int floorX = GrimMath.floor(x);
      int floorZ = GrimMath.floor(z);
      int floorY = GrimMath.floor(player.lastY + 0.5D);
      double d0 = x - (double)floorX;
      double d1 = z - (double)floorZ;
      boolean suffocates;
      if (player.isSwimming) {
         SimpleCollisionBox blockPos = (new SimpleCollisionBox((double)floorX, (double)floorY, (double)floorZ, (double)floorX + 1.0D, (double)(floorY + 1), (double)floorZ + 1.0D, false)).expand(-1.0E-7D);
         suffocates = Collisions.suffocatesAt(player, blockPos);
      } else {
         suffocates = !clearAbove(player, floorX, floorY, floorZ);
      }

      if (suffocates) {
         int i = -1;
         double d2 = 9999.0D;
         if (clearAbove(player, floorX - 1, floorY, floorZ) && d0 < d2) {
            d2 = d0;
            i = 0;
         }

         if (clearAbove(player, floorX + 1, floorY, floorZ) && 1.0D - d0 < d2) {
            d2 = 1.0D - d0;
            i = 1;
         }

         if (clearAbove(player, floorX, floorY, floorZ - 1) && d1 < d2) {
            d2 = d1;
            i = 4;
         }

         if (clearAbove(player, floorX, floorY, floorZ + 1) && 1.0D - d1 < d2) {
            i = 5;
         }

         UncertaintyHandler var10000;
         if (i == 0) {
            var10000 = player.uncertaintyHandler;
            var10000.xNegativeUncertainty -= 0.1D;
            var10000 = player.uncertaintyHandler;
            var10000.xPositiveUncertainty += 0.1D;
            player.pointThreeEstimator.setPushing(true);
         }

         if (i == 1) {
            var10000 = player.uncertaintyHandler;
            var10000.xNegativeUncertainty -= 0.1D;
            var10000 = player.uncertaintyHandler;
            var10000.xPositiveUncertainty += 0.1D;
            player.pointThreeEstimator.setPushing(true);
         }

         if (i == 4) {
            var10000 = player.uncertaintyHandler;
            var10000.zNegativeUncertainty -= 0.1D;
            var10000 = player.uncertaintyHandler;
            var10000.zPositiveUncertainty += 0.1D;
            player.pointThreeEstimator.setPushing(true);
         }

         if (i == 5) {
            var10000 = player.uncertaintyHandler;
            var10000.zNegativeUncertainty -= 0.1D;
            var10000 = player.uncertaintyHandler;
            var10000.zPositiveUncertainty += 0.1D;
            player.pointThreeEstimator.setPushing(true);
         }
      }

   }

   private static void moveTowardsClosestSpaceModern(GrimPlayer player, double xPosition, double zPosition) {
      int blockX = (int)Math.floor(xPosition);
      int blockZ = (int)Math.floor(zPosition);
      if (suffocatesAt(player, blockX, blockZ)) {
         double relativeXMovement = xPosition - (double)blockX;
         double relativeZMovement = zPosition - (double)blockZ;
         BlockFace direction = null;
         double lowestValue = Double.MAX_VALUE;
         BlockFace[] var14 = new BlockFace[]{BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH};
         int var15 = var14.length;

         for(int var16 = 0; var16 < var15; ++var16) {
            BlockFace direction2 = var14[var16];
            double d7 = direction2 != BlockFace.WEST && direction2 != BlockFace.EAST ? relativeZMovement : relativeXMovement;
            double d6 = direction2 != BlockFace.EAST && direction2 != BlockFace.SOUTH ? d7 : 1.0D - d7;
            boolean var10000;
            switch(direction2) {
            case EAST:
               var10000 = suffocatesAt(player, blockX + 1, blockZ);
               break;
            case WEST:
               var10000 = suffocatesAt(player, blockX - 1, blockZ);
               break;
            case NORTH:
               var10000 = suffocatesAt(player, blockX, blockZ - 1);
               break;
            default:
               var10000 = suffocatesAt(player, blockX, blockZ + 1);
            }

            boolean doesSuffocate = var10000;
            if (!(d6 >= lowestValue) && !doesSuffocate) {
               lowestValue = d6;
               direction = direction2;
            }
         }

         if (direction != null) {
            UncertaintyHandler var23;
            if (direction != BlockFace.WEST && direction != BlockFace.EAST) {
               var23 = player.uncertaintyHandler;
               var23.zPositiveUncertainty += 0.15D;
               var23 = player.uncertaintyHandler;
               var23.zNegativeUncertainty -= 0.15D;
               player.pointThreeEstimator.setPushing(true);
            } else {
               var23 = player.uncertaintyHandler;
               var23.xPositiveUncertainty += 0.15D;
               var23 = player.uncertaintyHandler;
               var23.xNegativeUncertainty -= 0.15D;
               player.pointThreeEstimator.setPushing(true);
            }
         }

      }
   }

   public static void updateInWaterStateAndDoWaterCurrentPushing(GrimPlayer player) {
      PacketEntity riding = player.compensatedEntities.self.getRiding();
      player.wasWasTouchingWater = player.wasTouchingWater;
      player.wasTouchingWater = updateFluidHeightAndDoFluidPushing(player, FluidTag.WATER, 0.014D) && (riding == null || !riding.isBoat);
      if (player.wasTouchingWater) {
         player.fallDistance = 0.0D;
      }

   }

   private static boolean updateFluidHeightAndDoFluidPushing(GrimPlayer player, FluidTag tag, double multiplier) {
      return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) ? updateFluidHeightAndDoFluidPushingModern(player, tag, multiplier) : updateFluidHeightAndDoFluidPushingLegacy(player, tag, multiplier);
   }

   private static boolean updateFluidHeightAndDoFluidPushingLegacy(GrimPlayer player, FluidTag tag, double multiplier) {
      SimpleCollisionBox aABB = player.boundingBox.copy().expand(0.0D, -0.4D, 0.0D).expand(-0.001D);
      int floorX = GrimMath.floor(aABB.minX);
      int ceilX = GrimMath.ceil(aABB.maxX);
      int floorY = GrimMath.floor(aABB.minY);
      int ceilY = GrimMath.ceil(aABB.maxY);
      int floorZ = GrimMath.floor(aABB.minZ);
      int ceilZ = GrimMath.ceil(aABB.maxZ);
      if (CheckIfChunksLoaded.areChunksUnloadedAt(player, floorX, floorY, floorZ, ceilX, ceilY, ceilZ)) {
         return false;
      } else {
         boolean hasPushed = false;
         Vector3dm vec3 = new Vector3dm();

         for(int x = floorX; x < ceilX; ++x) {
            for(int y = floorY; y < ceilY; ++y) {
               for(int z = floorZ; z < ceilZ; ++z) {
                  double fluidHeight;
                  if (tag == FluidTag.WATER) {
                     fluidHeight = player.compensatedWorld.getWaterFluidLevelAt(x, y, z);
                  } else {
                     fluidHeight = player.compensatedWorld.getLavaFluidLevelAt(x, y, z);
                  }

                  if (fluidHeight != 0.0D) {
                     double d0 = (double)((float)(y + 1)) - fluidHeight;
                     if (!player.isFlying && (double)ceilY >= d0) {
                        hasPushed = true;
                        vec3.add(FluidTypeFlowing.getFlow(player, x, y, z));
                     }
                  }
               }
            }
         }

         if (tag == FluidTag.WATER && vec3.lengthSquared() > 0.0D) {
            vec3.normalize();
            vec3.multiply(multiplier);
            player.baseTickAddWaterPushing(vec3);
            player.baseTickAddVector(vec3);
         }

         return hasPushed;
      }
   }

   private static boolean updateFluidHeightAndDoFluidPushingModern(GrimPlayer player, FluidTag tag, double multiplier) {
      SimpleCollisionBox aABB = player.boundingBox.copy().expand(-0.001D);
      int floorX = GrimMath.floor(aABB.minX);
      int ceilX = GrimMath.ceil(aABB.maxX);
      int floorY = GrimMath.floor(aABB.minY);
      int ceilY = GrimMath.ceil(aABB.maxY);
      int floorZ = GrimMath.floor(aABB.minZ);
      int ceilZ = GrimMath.ceil(aABB.maxZ);
      if (CheckIfChunksLoaded.areChunksUnloadedAt(player, floorX, floorY, floorZ, ceilX, ceilY, ceilZ)) {
         return false;
      } else {
         double d2 = 0.0D;
         boolean hasTouched = false;
         Vector3dm vec3 = new Vector3dm();
         int n7 = 0;

         for(int x = floorX; x < ceilX; ++x) {
            for(int y = floorY; y < ceilY; ++y) {
               for(int z = floorZ; z < ceilZ; ++z) {
                  double fluidHeight;
                  if (tag == FluidTag.WATER) {
                     fluidHeight = player.compensatedWorld.getWaterFluidLevelAt(x, y, z);
                  } else {
                     fluidHeight = player.compensatedWorld.getLavaFluidLevelAt(x, y, z);
                  }

                  if (player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
                     fluidHeight = Math.min(fluidHeight, 0.8888888888888888D);
                  }

                  double fluidHeightToWorld;
                  if (fluidHeight != 0.0D && !((fluidHeightToWorld = (double)y + fluidHeight) < aABB.minY)) {
                     hasTouched = true;
                     d2 = Math.max(fluidHeightToWorld - aABB.minY, d2);
                     if (!player.isFlying && !(player.getVehicle() instanceof PacketEntityNautilus)) {
                        Vector3dm vec32 = FluidTypeFlowing.getFlow(player, x, y, z);
                        if (d2 < 0.4D) {
                           vec32 = vec32.multiply(d2);
                        }

                        vec3 = vec3.add(vec32);
                        ++n7;
                     }
                  }
               }
            }
         }

         if (vec3.lengthSquared() > 0.0D) {
            if (n7 > 0) {
               vec3 = vec3.multiply(1.0D / (double)n7);
            }

            if (player.inVehicle()) {
               vec3 = vec3.normalize();
            }

            if (tag != FluidTag.LAVA || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16)) {
               vec3 = vec3.multiply(multiplier);
               player.baseTickAddWaterPushing(vec3);
               if (Math.abs(player.clientVelocity.getX()) < 0.003D && Math.abs(player.clientVelocity.getZ()) < 0.003D && vec3.length() < 0.0045000000000000005D) {
                  vec3 = vec3.normalize().multiply(0.0045000000000000005D);
               }

               player.baseTickAddVector(vec3);
            }
         }

         if (tag == FluidTag.LAVA) {
            player.slightlyTouchingLava = hasTouched && d2 <= 0.4D;
         }

         if (tag == FluidTag.WATER) {
            player.slightlyTouchingWater = hasTouched && d2 <= 0.4D;
         }

         player.fluidHeight.put(tag, d2);
         return hasTouched;
      }
   }

   private static boolean suffocatesAt(GrimPlayer player, int x, int z) {
      SimpleCollisionBox axisAlignedBB = (new SimpleCollisionBox((double)x, player.boundingBox.minY, (double)z, (double)x + 1.0D, player.boundingBox.maxY, (double)z + 1.0D, false)).expand(-1.0E-7D);
      return Collisions.suffocatesAt(player, axisAlignedBB);
   }

   private static boolean clearAbove(GrimPlayer player, int x, int y, int z) {
      return !Collisions.doesBlockSuffocate(player, x, y, z) && !Collisions.doesBlockSuffocate(player, x, y + 1, z);
   }

   @Generated
   private PlayerBaseTick() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      SERVER_SUPPORT_ENVIRONMENT_ATTRIBUTES = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);
   }
}
