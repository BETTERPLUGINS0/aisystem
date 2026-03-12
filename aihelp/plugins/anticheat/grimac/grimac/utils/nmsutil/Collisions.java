package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.events.packets.PacketWorldBorder;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.blockeffects.BlockEffectsResolver;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_10;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_2;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_4;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_5;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_6;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Direction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleListIterator;
import ac.grim.grimac.shaded.fastutil.floats.FloatArraySet;
import ac.grim.grimac.shaded.fastutil.floats.FloatArrays;
import ac.grim.grimac.shaded.fastutil.floats.FloatSet;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.chunks.Column;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.tags.SyncedTags;
import ac.grim.grimac.utils.latency.CompensatedWorld;
import ac.grim.grimac.utils.math.Location;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.Generated;

public final class Collisions {
   public static final double COLLISION_EPSILON = 1.0E-7D;
   private static final boolean IS_FOURTEEN;
   private static final List<List<Collisions.Axis>> allAxisCombinations;
   private static final List<List<Collisions.Axis>> nonStupidityCombinations;

   public static boolean slowCouldPointThreeHitGround(GrimPlayer player, double x, double y, double z) {
      SimpleCollisionBox oldBB = player.boundingBox;
      player.boundingBox = GetBoundingBox.getBoundingBoxFromPosAndSize(player, x, y, z, 0.6F, 0.06F);
      double movementThreshold = player.getMovementThreshold();
      double posXZ = collide(player, movementThreshold, -movementThreshold, movementThreshold).getY();
      double negXNegZ = collide(player, -movementThreshold, -movementThreshold, -movementThreshold).getY();
      double posXNegZ = collide(player, movementThreshold, -movementThreshold, -movementThreshold).getY();
      double posZNegX = collide(player, -movementThreshold, -movementThreshold, movementThreshold).getY();
      player.boundingBox = oldBB;
      return negXNegZ != -movementThreshold || posXNegZ != -movementThreshold || posXZ != -movementThreshold || posZNegX != -movementThreshold;
   }

   public static Vector3dm collide(GrimPlayer player, double desiredX, double desiredY, double desiredZ) {
      return collide(player, desiredX, desiredY, desiredZ, desiredY, (VectorData)null);
   }

   public static Vector3dm collide(GrimPlayer player, double desiredX, double desiredY, double desiredZ, double clientVelY, VectorData data) {
      if (desiredX == 0.0D && desiredY == 0.0D && desiredZ == 0.0D) {
         return new Vector3dm();
      } else {
         SimpleCollisionBox grabBoxesBB = player.boundingBox.copy();
         double stepUpHeight = (double)player.getMaxUpStep();
         if (desiredX == 0.0D && desiredZ == 0.0D) {
            if (desiredY > 0.0D) {
               grabBoxesBB.maxY += desiredY;
            } else {
               grabBoxesBB.minY += desiredY;
            }
         } else if (stepUpHeight > 0.0D && (player.lastOnGround || desiredY < 0.0D || clientVelY < 0.0D)) {
            if (desiredY <= 0.0D) {
               grabBoxesBB.expandToCoordinate(desiredX, desiredY, desiredZ);
               grabBoxesBB.maxY += stepUpHeight;
            } else {
               grabBoxesBB.expandToCoordinate(desiredX, Math.max(stepUpHeight, desiredY), desiredZ);
            }
         } else {
            grabBoxesBB.expandToCoordinate(desiredX, desiredY, desiredZ);
         }

         List<SimpleCollisionBox> desiredMovementCollisionBoxes = new ArrayList();
         getCollisionBoxes(player, grabBoxesBB, desiredMovementCollisionBoxes, false);
         double bestInput = Double.MAX_VALUE;
         Vector3dm bestOrderResult = null;
         Vector3dm bestTheoreticalCollisionResult = VectorUtils.cutBoxToVector(player.actualMovement, (new SimpleCollisionBox(0.0D, Math.min(0.0D, desiredY), 0.0D, desiredX, Math.max(stepUpHeight, desiredY), desiredZ)).sort());
         int zeroCount = (desiredX == 0.0D ? 1 : 0) + (desiredY == 0.0D ? 1 : 0) + (desiredZ == 0.0D ? 1 : 0);
         Iterator var19 = (data != null && data.isZeroPointZeroThree() ? allAxisCombinations : nonStupidityCombinations).iterator();

         while(var19.hasNext()) {
            List<Collisions.Axis> order = (List)var19.next();
            Vector3dm collisionResult = collideBoundingBoxLegacy(new Vector3dm(desiredX, desiredY, desiredZ), player.boundingBox, desiredMovementCollisionBoxes, order);
            boolean movingIntoGroundReal = player.pointThreeEstimator.closeEnoughToGroundToStepWithPointThree(data, clientVelY) || collisionResult.getY() != desiredY && (desiredY < 0.0D || clientVelY < 0.0D);
            boolean movingIntoGround = player.lastOnGround || movingIntoGroundReal;
            boolean disallowStepping = player.getSetbackTeleportUtil().getRequiredSetBack() != null && player.getSetbackTeleportUtil().getRequiredSetBack().getTicksComplete() == 1;
            if (!disallowStepping && stepUpHeight > 0.0D && movingIntoGround && (collisionResult.getX() != desiredX || collisionResult.getZ() != desiredZ)) {
               player.uncertaintyHandler.isStepMovement = true;
               if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21)) {
                  SimpleCollisionBox startingOffsetBox = movingIntoGroundReal ? player.boundingBox.copy().offset(0.0D, collisionResult.getY(), 0.0D) : player.boundingBox.copy();
                  SimpleCollisionBox offsetByHorizAndStepBox = startingOffsetBox.copy().expandToCoordinate(desiredX, stepUpHeight, desiredZ);
                  if (!movingIntoGroundReal) {
                     offsetByHorizAndStepBox = offsetByHorizAndStepBox.copy().expandToCoordinate(0.0D, -9.999999747378752E-6D, 0.0D);
                  }

                  List<SimpleCollisionBox> stepCollisions = new ArrayList();
                  getCollisionBoxes(player, offsetByHorizAndStepBox, stepCollisions, false);
                  float[] stepHeights = collectStepHeights(startingOffsetBox, stepCollisions, (float)stepUpHeight, (float)collisionResult.getY());
                  float[] var29 = stepHeights;
                  int var30 = stepHeights.length;

                  for(int var31 = 0; var31 < var30; ++var31) {
                     float stepHeight = var29[var31];
                     Vector3dm vec3d2 = collideBoundingBoxLegacy(new Vector3dm(desiredX, (double)stepHeight, desiredZ), startingOffsetBox, stepCollisions, order);
                     if (getHorizontalDistanceSqr(vec3d2) > getHorizontalDistanceSqr(collisionResult)) {
                        double d = player.boundingBox.minY - startingOffsetBox.minY;
                        collisionResult = vec3d2.add(new Vector3dm(0.0D, -d, 0.0D));
                        break;
                     }
                  }
               } else {
                  Vector3dm regularStepUp = collideBoundingBoxLegacy(new Vector3dm(desiredX, stepUpHeight, desiredZ), player.boundingBox, desiredMovementCollisionBoxes, order);
                  if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8)) {
                     Vector3dm stepUpBugFix = collideBoundingBoxLegacy(new Vector3dm(0.0D, stepUpHeight, 0.0D), player.boundingBox.copy().expandToCoordinate(desiredX, 0.0D, desiredZ), desiredMovementCollisionBoxes, order);
                     if (stepUpBugFix.getY() < stepUpHeight) {
                        Vector3dm stepUpBugFixResult = collideBoundingBoxLegacy(new Vector3dm(desiredX, 0.0D, desiredZ), player.boundingBox.copy().offset(0.0D, stepUpBugFix.getY(), 0.0D), desiredMovementCollisionBoxes, order).add(stepUpBugFix);
                        if (getHorizontalDistanceSqr(stepUpBugFixResult) > getHorizontalDistanceSqr(regularStepUp)) {
                           regularStepUp = stepUpBugFixResult;
                        }
                     }
                  }

                  if (getHorizontalDistanceSqr(regularStepUp) > getHorizontalDistanceSqr(collisionResult)) {
                     collisionResult = regularStepUp.add(collideBoundingBoxLegacy(new Vector3dm(0.0D, -regularStepUp.getY() + (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) ? desiredY : 0.0D), 0.0D), player.boundingBox.copy().offset(regularStepUp.getX(), regularStepUp.getY(), regularStepUp.getZ()), desiredMovementCollisionBoxes, order));
                  }
               }
            }

            double resultAccuracy = collisionResult.distanceSquared(bestTheoreticalCollisionResult);
            if (player.wouldCollisionResultFlagGroundSpoof(desiredY, collisionResult.getY())) {
               ++resultAccuracy;
            }

            if (resultAccuracy < bestInput) {
               bestOrderResult = collisionResult;
               bestInput = resultAccuracy;
               if (resultAccuracy < 1.0000000000000002E-10D || zeroCount >= 2) {
                  break;
               }
            }
         }

         return bestOrderResult;
      }
   }

   private static float[] collectStepHeights(SimpleCollisionBox collisionBox, List<SimpleCollisionBox> collisions, float stepHeight, float collideY) {
      FloatSet floatSet = new FloatArraySet(4);
      Iterator var5 = collisions.iterator();

      while(var5.hasNext()) {
         SimpleCollisionBox blockBox = (SimpleCollisionBox)var5.next();
         DoubleListIterator var7 = blockBox.getYPointPositions().iterator();

         while(var7.hasNext()) {
            double possibleStepY = (Double)var7.next();
            float yDiff = (float)(possibleStepY - collisionBox.minY);
            if (!(yDiff < 0.0F) && yDiff != collideY) {
               if (yDiff > stepHeight) {
                  break;
               }

               floatSet.add(yDiff);
            }
         }
      }

      float[] fs = floatSet.toFloatArray();
      FloatArrays.unstableSort(fs);
      return fs;
   }

   public static boolean addWorldBorder(GrimPlayer player, SimpleCollisionBox wantedBB, List<SimpleCollisionBox> listOfBlocks, boolean onlyCheckCollide) {
      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8)) {
         PacketWorldBorder border = (PacketWorldBorder)player.checkManager.getPacketCheck(PacketWorldBorder.class);
         double minX = Math.floor(border.getMinX());
         double minZ = Math.floor(border.getMinZ());
         double maxX = Math.ceil(border.getMaxX());
         double maxZ = Math.ceil(border.getMaxZ());
         double toMinX = player.lastX - minX;
         double toMaxX = maxX - player.lastX;
         double minimumInXDirection = Math.min(toMinX, toMaxX);
         double toMinZ = player.lastZ - minZ;
         double toMaxZ = maxZ - player.lastZ;
         double minimumInZDirection = Math.min(toMinZ, toMaxZ);
         double distanceToBorder = Math.min(minimumInXDirection, minimumInZDirection);
         if (distanceToBorder < 16.0D && player.lastX > minX && player.lastX < maxX && player.lastZ > minZ && player.lastZ < maxZ) {
            if (listOfBlocks == null) {
               listOfBlocks = new ArrayList();
            }

            ((List)listOfBlocks).add(new SimpleCollisionBox(minX - 10.0D, Double.NEGATIVE_INFINITY, maxZ, maxX + 10.0D, Double.POSITIVE_INFINITY, maxZ, false));
            ((List)listOfBlocks).add(new SimpleCollisionBox(minX - 10.0D, Double.NEGATIVE_INFINITY, minZ, maxX + 10.0D, Double.POSITIVE_INFINITY, minZ, false));
            ((List)listOfBlocks).add(new SimpleCollisionBox(maxX, Double.NEGATIVE_INFINITY, minZ - 10.0D, maxX, Double.POSITIVE_INFINITY, maxZ + 10.0D, false));
            ((List)listOfBlocks).add(new SimpleCollisionBox(minX, Double.NEGATIVE_INFINITY, minZ - 10.0D, minX, Double.POSITIVE_INFINITY, maxZ + 10.0D, false));
            if (onlyCheckCollide) {
               Iterator var27 = ((List)listOfBlocks).iterator();

               while(var27.hasNext()) {
                  SimpleCollisionBox box = (SimpleCollisionBox)var27.next();
                  if (box.isIntersected(wantedBB)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean getCollisionBoxes(GrimPlayer player, SimpleCollisionBox wantedBB, List<SimpleCollisionBox> listOfBlocks, boolean onlyCheckCollide) {
      SimpleCollisionBox expandedBB = wantedBB.copy();
      boolean collided = addWorldBorder(player, wantedBB, listOfBlocks, onlyCheckCollide);
      if (onlyCheckCollide && collided) {
         return true;
      } else {
         int minBlockX = (int)Math.floor(expandedBB.minX - 1.0E-7D) - 1;
         int maxBlockX = (int)Math.floor(expandedBB.maxX + 1.0E-7D) + 1;
         int minBlockY = (int)Math.floor(expandedBB.minY - 1.0E-7D) - 1;
         int maxBlockY = (int)Math.floor(expandedBB.maxY + 1.0E-7D) + 1;
         int minBlockZ = (int)Math.floor(expandedBB.minZ - 1.0E-7D) - 1;
         int maxBlockZ = (int)Math.floor(expandedBB.maxZ + 1.0E-7D) + 1;
         int minSection = player.compensatedWorld.getMinHeight() >> 4;
         int minBlock = minSection << 4;
         int maxBlock = player.compensatedWorld.getMaxHeight() - 1;
         int minChunkX = minBlockX >> 4;
         int maxChunkX = maxBlockX >> 4;
         int minChunkZ = minBlockZ >> 4;
         int maxChunkZ = maxBlockZ >> 4;
         int minYIterate = Math.max(minBlock, minBlockY);
         int maxYIterate = Math.min(maxBlock, maxBlockY);

         for(int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
            int minZ = currChunkZ == minChunkZ ? minBlockZ & 15 : 0;
            int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 15 : 15;

            for(int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
               int minX = currChunkX == minChunkX ? minBlockX & 15 : 0;
               int maxX = currChunkX == maxChunkX ? maxBlockX & 15 : 15;
               int chunkXGlobalPos = currChunkX << 4;
               int chunkZGlobalPos = currChunkZ << 4;
               Column chunk = player.compensatedWorld.getChunk(currChunkX, currChunkZ);
               if (chunk != null) {
                  BaseChunk[] sections = chunk.chunks();

                  for(int y = minYIterate; y <= maxYIterate; ++y) {
                     int sectionIndex = (y >> 4) - minSection;
                     BaseChunk section = sections[sectionIndex];
                     if (section == null || IS_FOURTEEN && section.isEmpty()) {
                        y = (y & -16) + 15;
                     } else {
                        for(int currZ = minZ; currZ <= maxZ; ++currZ) {
                           for(int currX = minX; currX <= maxX; ++currX) {
                              int x = currX | chunkXGlobalPos;
                              int z = currZ | chunkZGlobalPos;
                              WrappedBlockState data = section.get(CompensatedWorld.blockVersion, x & 15, y & 15, z & 15, false);
                              if (data.getGlobalId() != 0) {
                                 int edgeCount = (x != minBlockX && x != maxBlockX ? 0 : 1) + (y != minBlockY && y != maxBlockY ? 0 : 1) + (z != minBlockZ && z != maxBlockZ ? 0 : 1);
                                 StateType type = data.getType();
                                 if (edgeCount != 3 && (edgeCount != 1 || Materials.isShapeExceedsCube(type)) && (edgeCount != 2 || type == StateTypes.PISTON_HEAD)) {
                                    CollisionBox collisionBox = CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z);
                                    if (!onlyCheckCollide) {
                                       collisionBox.downCast(listOfBlocks);
                                    } else if (collisionBox.isCollided(wantedBB)) {
                                       return true;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public static Vector3dm collideBoundingBoxLegacy(Vector3dm toCollide, SimpleCollisionBox box, List<SimpleCollisionBox> desiredMovementCollisionBoxes, List<Collisions.Axis> order) {
      double x = toCollide.getX();
      double y = toCollide.getY();
      double z = toCollide.getZ();
      SimpleCollisionBox setBB = box.copy();
      Iterator var11 = order.iterator();

      while(true) {
         while(var11.hasNext()) {
            Collisions.Axis axis = (Collisions.Axis)var11.next();
            Iterator var13;
            SimpleCollisionBox bb;
            if (axis == Collisions.Axis.X) {
               for(var13 = desiredMovementCollisionBoxes.iterator(); var13.hasNext(); x = bb.collideX(setBB, x)) {
                  bb = (SimpleCollisionBox)var13.next();
               }

               setBB.offset(x, 0.0D, 0.0D);
            } else if (axis == Collisions.Axis.Y) {
               for(var13 = desiredMovementCollisionBoxes.iterator(); var13.hasNext(); y = bb.collideY(setBB, y)) {
                  bb = (SimpleCollisionBox)var13.next();
               }

               setBB.offset(0.0D, y, 0.0D);
            } else if (axis == Collisions.Axis.Z) {
               for(var13 = desiredMovementCollisionBoxes.iterator(); var13.hasNext(); z = bb.collideZ(setBB, z)) {
                  bb = (SimpleCollisionBox)var13.next();
               }

               setBB.offset(0.0D, 0.0D, z);
            }
         }

         return new Vector3dm(x, y, z);
      }
   }

   public static boolean isEmpty(GrimPlayer player, SimpleCollisionBox playerBB) {
      return !getCollisionBoxes(player, playerBB, (List)null, true);
   }

   public static double getHorizontalDistanceSqr(Vector3dm vector) {
      return vector.getX() * vector.getX() + vector.getZ() * vector.getZ();
   }

   public static Vector3dm maybeBackOffFromEdge(Vector3dm vec3, GrimPlayer player, boolean overrideVersion) {
      if (!player.isFlying && player.isSneaking && isAboveGround(player)) {
         double x = vec3.getX();
         double z = vec3.getZ();
         double maxStepDown = !overrideVersion && !player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_11) ? -0.9999999D : (double)(-player.getMaxUpStep());

         while(true) {
            while(x != 0.0D && isEmpty(player, player.boundingBox.copy().offset(x, maxStepDown, 0.0D))) {
               if (x < 0.05D && x >= -0.05D) {
                  x = 0.0D;
               } else if (x > 0.0D) {
                  x -= 0.05D;
               } else {
                  x += 0.05D;
               }
            }

            while(true) {
               while(z != 0.0D && isEmpty(player, player.boundingBox.copy().offset(0.0D, maxStepDown, z))) {
                  if (z < 0.05D && z >= -0.05D) {
                     z = 0.0D;
                  } else if (z > 0.0D) {
                     z -= 0.05D;
                  } else {
                     z += 0.05D;
                  }
               }

               while(true) {
                  while(x != 0.0D && z != 0.0D && isEmpty(player, player.boundingBox.copy().offset(x, maxStepDown, z))) {
                     if (x < 0.05D && x >= -0.05D) {
                        x = 0.0D;
                     } else if (x > 0.0D) {
                        x -= 0.05D;
                     } else {
                        x += 0.05D;
                     }

                     if (z < 0.05D && z >= -0.05D) {
                        z = 0.0D;
                     } else if (z > 0.0D) {
                        z -= 0.05D;
                     } else {
                        z += 0.05D;
                     }
                  }

                  vec3 = new Vector3dm(x, vec3.getY(), z);
                  return vec3;
               }
            }
         }
      } else {
         return vec3;
      }
   }

   public static boolean isAboveGround(GrimPlayer player) {
      return player.lastOnGround || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16_2) && player.fallDistance < (double)player.getMaxUpStep() && !isEmpty(player, player.boundingBox.copy().offset(0.0D, player.fallDistance - (double)player.getMaxUpStep(), 0.0D));
   }

   public static void handleInsideBlocks(GrimPlayer player) {
      if (!player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
         double expandAmount = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19_4) ? 1.0E-5D : 0.001D;
         SimpleCollisionBox aABB = (player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z) : player.boundingBox.copy()).expand(-expandAmount);
         Location blockPos = new Location((PlatformWorld)null, aABB.minX, aABB.minY, aABB.minZ);
         Location blockPos2 = new Location((PlatformWorld)null, aABB.maxX, aABB.maxY, aABB.maxZ);
         if (!CheckIfChunksLoaded.areChunksUnloadedAt(player, blockPos.getBlockX(), blockPos.getBlockY(), blockPos.getBlockZ(), blockPos2.getBlockX(), blockPos2.getBlockY(), blockPos2.getBlockZ())) {
            for(int blockX = blockPos.getBlockX(); blockX <= blockPos2.getBlockX(); ++blockX) {
               for(int blockY = blockPos.getBlockY(); blockY <= blockPos2.getBlockY(); ++blockY) {
                  for(int blockZ = blockPos.getBlockZ(); blockZ <= blockPos2.getBlockZ(); ++blockZ) {
                     WrappedBlockState block = player.compensatedWorld.getBlock(blockX, blockY, blockZ);
                     StateType blockType = block.getType();
                     if (!blockType.isAir()) {
                        onInsideBlock(player, blockType, block, blockX, blockY, blockZ, true);
                     }
                  }
               }
            }

         }
      }
   }

   public static void onInsideBlock(GrimPlayer player, StateType blockType, WrappedBlockState block, int blockX, int blockY, int blockZ, boolean magic) {
      if (blockType == StateTypes.COBWEB) {
         if (player.compensatedEntities.hasPotionEffect(PotionTypes.WEAVING)) {
            player.stuckSpeedMultiplier = new Vector3dm(0.5D, 0.25D, 0.5D);
         } else {
            player.stuckSpeedMultiplier = new Vector3dm(0.25D, 0.05000000074505806D, 0.25D);
         }
      }

      if (blockType == StateTypes.SWEET_BERRY_BUSH && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
         player.stuckSpeedMultiplier = new Vector3dm(0.800000011920929D, 0.75D, 0.800000011920929D);
      }

      if (blockType == StateTypes.POWDER_SNOW && (double)blockX == Math.floor(player.x) && (double)blockY == Math.floor(player.y) && (double)blockZ == Math.floor(player.z) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
         player.stuckSpeedMultiplier = new Vector3dm(0.8999999761581421D, 1.5D, 0.8999999761581421D);
      }

      if (blockType == StateTypes.SOUL_SAND && player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
         player.clientVelocity.setX(player.clientVelocity.getX() * 0.4D);
         player.clientVelocity.setZ(player.clientVelocity.getZ() * 0.4D);
      }

      if (blockType == StateTypes.LAVA && player.getClientVersion().isOlderThan(ClientVersion.V_1_16) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
         player.wasTouchingLava = true;
      }

      if (blockType == StateTypes.BUBBLE_COLUMN && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && magic) {
         WrappedBlockState blockAbove = player.compensatedWorld.getBlock(blockX, blockY + 1, blockZ);
         if (player.inVehicle() && player.compensatedEntities.self.getRiding().isBoat) {
            if (!blockAbove.getType().isAir()) {
               if (block.isDrag()) {
                  player.clientVelocity.setY(Math.max(-0.3D, player.clientVelocity.getY() - 0.03D));
               } else {
                  player.clientVelocity.setY(Math.min(0.7D, player.clientVelocity.getY() + 0.06D));
               }
            }
         } else {
            Iterator var8;
            VectorData vector;
            if (blockAbove.getType().isAir()) {
               var8 = player.getPossibleVelocitiesMinusKnockback().iterator();

               while(var8.hasNext()) {
                  vector = (VectorData)var8.next();
                  if (block.isDrag()) {
                     vector.vector.setY(Math.max(-0.9D, vector.vector.getY() - 0.03D));
                  } else {
                     vector.vector.setY(Math.min(1.8D, vector.vector.getY() + 0.1D));
                  }
               }
            } else {
               var8 = player.getPossibleVelocitiesMinusKnockback().iterator();

               while(var8.hasNext()) {
                  vector = (VectorData)var8.next();
                  if (block.isDrag()) {
                     vector.vector.setY(Math.max(-0.3D, vector.vector.getY() - 0.03D));
                  } else {
                     vector.vector.setY(Math.min(0.7D, vector.vector.getY() + 0.06D));
                  }
               }
            }
         }

         player.fallDistance = 0.0D;
      }

      if (blockType == StateTypes.HONEY_BLOCK && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_15)) {
         if (isSlidingDown(player.clientVelocity, player, blockX, blockY, blockZ)) {
            if (getOldDeltaY(player, player.clientVelocity.getY()) < -0.13D) {
               double d0 = -0.05D / getOldDeltaY(player, player.clientVelocity.getY());
               player.clientVelocity.setX(player.clientVelocity.getX() * d0);
               player.clientVelocity.setY(getNewDeltaY(player, -0.05D));
               player.clientVelocity.setZ(player.clientVelocity.getZ() * d0);
            } else {
               player.clientVelocity.setY(getNewDeltaY(player, -0.05D));
            }
         }

         player.fallDistance = 0.0D;
      }

   }

   public static void applyEffectsFromBlocks(GrimPlayer player) {
      if (!player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2)) {
         if (player.stuckSpeedMultiplier.getX() < 0.99D) {
            player.uncertaintyHandler.lastStuckSpeedMultiplier.reset();
         }

         player.stuckSpeedMultiplier = new Vector3dm(1, 1, 1);
         player.finalMovementsThisTick.clear();
         Vector3d from = new Vector3d(player.lastX, player.lastY, player.lastZ);
         Vector3d to = new Vector3d(player.x, player.y, player.z);
         ClientVersion clientVersion = player.getClientVersion();
         if (clientVersion.isOlderThan(ClientVersion.V_1_21_5)) {
            player.finalMovementsThisTick.add(new GrimPlayer.Movement(from, to));
         } else if (clientVersion.isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
            player.finalMovementsThisTick.addAll(player.movementThisTick);
            player.movementThisTick.clear();
            if (player.finalMovementsThisTick.isEmpty()) {
               player.finalMovementsThisTick.add(new GrimPlayer.Movement(from, to));
            } else if (((GrimPlayer.Movement)player.finalMovementsThisTick.get(player.finalMovementsThisTick.size() - 1)).to().distanceSquared(to) > 9.999999439624929E-11D) {
               player.finalMovementsThisTick.add(new GrimPlayer.Movement(((GrimPlayer.Movement)player.finalMovementsThisTick.get(player.finalMovementsThisTick.size() - 1)).to(), to));
            }
         }

         resolveBlockEffects(player, player.finalMovementsThisTick);
         if (player.stuckSpeedMultiplier.getX() < 0.9D) {
            player.fallDistance = 0.0D;
         }

         if (player.isFlying) {
            player.stuckSpeedMultiplier = new Vector3dm(1, 1, 1);
         }

      }
   }

   public static void resolveBlockEffects(GrimPlayer player, Vector3d from, Vector3d to) {
      resolveBlockEffects(player, List.of(new GrimPlayer.Movement(from, to)));
   }

   public static void resolveBlockEffects(GrimPlayer player, List<GrimPlayer.Movement> movements) {
      ClientVersion version = player.getClientVersion();
      BlockEffectsResolver resolver;
      if (version == ClientVersion.V_1_21_2) {
         resolver = BlockEffectsResolverV1_21_2.INSTANCE;
      } else if (version == ClientVersion.V_1_21_4) {
         resolver = BlockEffectsResolverV1_21_4.INSTANCE;
      } else if (version == ClientVersion.V_1_21_5) {
         resolver = BlockEffectsResolverV1_21_5.INSTANCE;
      } else if (version.isNewerThanOrEquals(ClientVersion.V_1_21_6) && version.isOlderThanOrEquals(ClientVersion.V_1_21_7)) {
         resolver = BlockEffectsResolverV1_21_6.INSTANCE;
      } else {
         resolver = BlockEffectsResolverV1_21_10.INSTANCE;
      }

      resolver.applyEffectsFromBlocks(player, movements);
   }

   private static double getOldDeltaY(GrimPlayer player, double value) {
      return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) ? value / 0.9800000190734863D + 0.08D : value;
   }

   private static double getNewDeltaY(GrimPlayer player, double value) {
      return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) ? (value - 0.08D) * 0.9800000190734863D : value;
   }

   private static boolean isSlidingDown(Vector3dm vector, GrimPlayer player, int locationX, int locationY, int locationZ) {
      if (player.onGround) {
         return false;
      } else if (player.y > (double)locationY + 0.9375D - 1.0E-7D) {
         return false;
      } else if (getOldDeltaY(player, vector.getY()) >= -0.08D) {
         return false;
      } else {
         double d0 = Math.abs((double)locationX + 0.5D - player.lastX);
         double d1 = Math.abs((double)locationZ + 0.5D - player.lastZ);
         double d2 = 0.4375D + (double)(player.pose.width / 2.0F);
         return d0 + 1.0E-7D > d2 || d1 + 1.0E-7D > d2;
      }
   }

   public static boolean checkStuckSpeed(GrimPlayer player, double expand) {
      SimpleCollisionBox aABB = GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z).expand(expand);
      Location blockPos = new Location((PlatformWorld)null, aABB.minX, aABB.minY, aABB.minZ);
      Location blockPos2 = new Location((PlatformWorld)null, aABB.maxX, aABB.maxY, aABB.maxZ);
      if (CheckIfChunksLoaded.areChunksUnloadedAt(player, blockPos.getBlockX(), blockPos.getBlockY(), blockPos.getBlockZ(), blockPos2.getBlockX(), blockPos2.getBlockY(), blockPos2.getBlockZ())) {
         return false;
      } else {
         for(int i = blockPos.getBlockX(); i <= blockPos2.getBlockX(); ++i) {
            for(int j = blockPos.getBlockY(); j <= blockPos2.getBlockY(); ++j) {
               for(int k = blockPos.getBlockZ(); k <= blockPos2.getBlockZ(); ++k) {
                  WrappedBlockState block = player.compensatedWorld.getBlock(i, j, k);
                  StateType blockType = block.getType();
                  if (blockType == StateTypes.COBWEB) {
                     return true;
                  }

                  if (blockType == StateTypes.SWEET_BERRY_BUSH && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
                     return true;
                  }

                  if (blockType == StateTypes.POWDER_SNOW && (double)i == Math.floor(player.x) && (double)j == Math.floor(player.y) && (double)k == Math.floor(player.z) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   public static boolean suffocatesAt(GrimPlayer player, SimpleCollisionBox playerBB) {
      for(int y = (int)Math.floor(playerBB.minY); (double)y < Math.ceil(playerBB.maxY); ++y) {
         for(int z = (int)Math.floor(playerBB.minZ); (double)z < Math.ceil(playerBB.maxZ); ++z) {
            for(int x = (int)Math.floor(playerBB.minX); (double)x < Math.ceil(playerBB.maxX); ++x) {
               if (doesBlockSuffocate(player, x, y, z)) {
                  if (!player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16)) {
                     return true;
                  }

                  WrappedBlockState data = player.compensatedWorld.getBlock(x, y, z);
                  CollisionBox box = CollisionData.getData(data.getType()).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z);
                  if (box.isIntersected(playerBB)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean doesBlockSuffocate(GrimPlayer player, int x, int y, int z) {
      WrappedBlockState data = player.compensatedWorld.getBlock(x, y, z);
      StateType mat = data.getType();
      if (!mat.isSolid()) {
         return false;
      } else if (mat != StateTypes.OBSERVER && mat != StateTypes.REDSTONE_BLOCK) {
         if (mat == StateTypes.TNT) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14);
         } else if (mat == StateTypes.FARMLAND) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16);
         } else if (mat == StateTypes.SOUL_SAND) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) || player.getClientVersion().isOlderThan(ClientVersion.V_1_14);
         } else if ((mat == StateTypes.PISTON || mat == StateTypes.STICKY_PISTON) && player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
            return false;
         } else if (mat != StateTypes.ICE && mat != StateTypes.FROSTED_ICE) {
            if (!BlockTags.LEAVES.contains(mat) && !BlockTags.GLASS_BLOCKS.contains(mat)) {
               if (mat != StateTypes.DIRT_PATH) {
                  if (mat == StateTypes.BEACON) {
                     return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14);
                  } else if (Materials.isSolidBlockingBlacklist(mat, player.getClientVersion())) {
                     return false;
                  } else {
                     CollisionBox box = CollisionData.getData(mat).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z);
                     return box.isFullBlock();
                  }
               } else {
                  return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) || player.getClientVersion().isOlderThan(ClientVersion.V_1_9);
               }
            } else {
               return false;
            }
         } else {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14);
         }
      } else {
         return player.getClientVersion().isNewerThan(ClientVersion.V_1_13_2);
      }
   }

   public static boolean hasMaterial(GrimPlayer player, SimpleCollisionBox checkBox, Predicate<Pair<WrappedBlockState, Vector3i>> searchingFor) {
      int minBlockX = (int)Math.floor(checkBox.minX);
      int maxBlockX = (int)Math.floor(checkBox.maxX);
      int minBlockY = (int)Math.floor(checkBox.minY);
      int maxBlockY = (int)Math.floor(checkBox.maxY);
      int minBlockZ = (int)Math.floor(checkBox.minZ);
      int maxBlockZ = (int)Math.floor(checkBox.maxZ);
      int minSection = player.compensatedWorld.getMinHeight() >> 4;
      int minBlock = minSection << 4;
      int maxBlock = player.compensatedWorld.getMaxHeight() - 1;
      int minChunkX = minBlockX >> 4;
      int maxChunkX = maxBlockX >> 4;
      int minChunkZ = minBlockZ >> 4;
      int maxChunkZ = maxBlockZ >> 4;
      int minYIterate = Math.max(minBlock, minBlockY);
      int maxYIterate = Math.min(maxBlock, maxBlockY);

      for(int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
         int minZ = currChunkZ == minChunkZ ? minBlockZ & 15 : 0;
         int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 15 : 15;

         for(int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
            int minX = currChunkX == minChunkX ? minBlockX & 15 : 0;
            int maxX = currChunkX == maxChunkX ? maxBlockX & 15 : 15;
            int chunkXGlobalPos = currChunkX << 4;
            int chunkZGlobalPos = currChunkZ << 4;
            Column chunk = player.compensatedWorld.getChunk(currChunkX, currChunkZ);
            if (chunk != null) {
               BaseChunk[] sections = chunk.chunks();

               for(int y = minYIterate; y <= maxYIterate; ++y) {
                  BaseChunk section = sections[(y >> 4) - minSection];
                  if (section == null || IS_FOURTEEN && section.isEmpty()) {
                     y = (y & -16) + 15;
                  } else {
                     for(int currZ = minZ; currZ <= maxZ; ++currZ) {
                        for(int currX = minX; currX <= maxX; ++currX) {
                           int x = currX | chunkXGlobalPos;
                           int z = currZ | chunkZGlobalPos;
                           WrappedBlockState data = section.get(CompensatedWorld.blockVersion, x & 15, y & 15, z & 15, false);
                           if (searchingFor.test(new Pair(data, new Vector3i(x, y, z)))) {
                              return true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public static void forEachCollisionBox(@NotNull GrimPlayer player, @NotNull SimpleCollisionBox checkBox, @NotNull Consumer<Vector3d> searchingFor) {
      int minBlockX = (int)Math.floor(checkBox.minX - 1.0E-7D) - 1;
      int maxBlockX = (int)Math.floor(checkBox.maxX + 1.0E-7D) + 1;
      int minBlockY = (int)Math.floor(checkBox.minY - 1.0E-7D) - 1;
      int maxBlockY = (int)Math.floor(checkBox.maxY + 1.0E-7D) + 1;
      int minBlockZ = (int)Math.floor(checkBox.minZ - 1.0E-7D) - 1;
      int maxBlockZ = (int)Math.floor(checkBox.maxZ + 1.0E-7D) + 1;
      int minSection = player.compensatedWorld.getMinHeight() >> 4;
      int minBlock = minSection << 4;
      int maxBlock = player.compensatedWorld.getMaxHeight() - 1;
      int minChunkX = minBlockX >> 4;
      int maxChunkX = maxBlockX >> 4;
      int minChunkZ = minBlockZ >> 4;
      int maxChunkZ = maxBlockZ >> 4;
      int minYIterate = Math.max(minBlock, minBlockY);
      int maxYIterate = Math.min(maxBlock, maxBlockY);

      for(int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
         int minZ = currChunkZ == minChunkZ ? minBlockZ & 15 : 0;
         int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 15 : 15;

         for(int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
            int minX = currChunkX == minChunkX ? minBlockX & 15 : 0;
            int maxX = currChunkX == maxChunkX ? maxBlockX & 15 : 15;
            int chunkXGlobalPos = currChunkX << 4;
            int chunkZGlobalPos = currChunkZ << 4;
            Column chunk = player.compensatedWorld.getChunk(currChunkX, currChunkZ);
            if (chunk != null) {
               BaseChunk[] sections = chunk.chunks();

               for(int y = minYIterate; y <= maxYIterate; ++y) {
                  BaseChunk section = sections[(y >> 4) - minSection];
                  if (section == null || IS_FOURTEEN && section.isEmpty()) {
                     y = (y & -16) + 15;
                  } else {
                     for(int currZ = minZ; currZ <= maxZ; ++currZ) {
                        for(int currX = minX; currX <= maxX; ++currX) {
                           int x = currX | chunkXGlobalPos;
                           int z = currZ | chunkZGlobalPos;
                           WrappedBlockState data = section.get(CompensatedWorld.blockVersion, x & 15, y & 15, z & 15, false);
                           if (data.getGlobalId() != 0) {
                              int edgeCount = (x != minBlockX && x != maxBlockX ? 0 : 1) + (y != minBlockY && y != maxBlockY ? 0 : 1) + (z != minBlockZ && z != maxBlockZ ? 0 : 1);
                              StateType type = data.getType();
                              if (edgeCount != 3 && (edgeCount != 1 || Materials.isShapeExceedsCube(type)) && (edgeCount != 2 || type == StateTypes.PISTON_HEAD)) {
                                 CollisionBox collisionBox = CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z);
                                 if (collisionBox.isIntersected(checkBox)) {
                                    searchingFor.accept(new Vector3d((double)x, (double)y, (double)z));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public static boolean onClimbable(GrimPlayer player, double x, double y, double z) {
      WrappedBlockState blockState = player.compensatedWorld.getBlock(x, y, z);
      StateType blockMaterial = blockState.getType();
      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_11) && player.isGliding && BlockTags.CAN_GLIDE_THROUGH.contains(blockMaterial)) {
         return false;
      } else if (blockMaterial != StateTypes.CAVE_VINES && blockMaterial != StateTypes.CAVE_VINES_PLANT) {
         if (player.tagManager.block(SyncedTags.CLIMBABLE).contains(blockMaterial)) {
            return true;
         } else {
            return blockMaterial == StateTypes.SWEET_BERRY_BUSH && player.getClientVersion().isOlderThan(ClientVersion.V_1_14) ? true : trapdoorUsableAsLadder(player, x, y, z, blockState);
         }
      } else {
         return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17);
      }
   }

   public static boolean trapdoorUsableAsLadder(GrimPlayer player, double x, double y, double z, WrappedBlockState blockData) {
      if (!BlockTags.TRAPDOORS.contains(blockData.getType())) {
         return false;
      } else if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
         return false;
      } else {
         if (blockData.isOpen()) {
            WrappedBlockState blockBelow = player.compensatedWorld.getBlock(x, y - 1.0D, z);
            if (blockBelow.getType() == StateTypes.LADDER) {
               return blockData.getFacing() == blockBelow.getFacing();
            }
         }

         return false;
      }
   }

   @Generated
   private Collisions() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      IS_FOURTEEN = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);
      allAxisCombinations = Arrays.asList(Arrays.asList(Collisions.Axis.Y, Collisions.Axis.X, Collisions.Axis.Z), Arrays.asList(Collisions.Axis.Y, Collisions.Axis.Z, Collisions.Axis.X), Arrays.asList(Collisions.Axis.X, Collisions.Axis.Y, Collisions.Axis.Z), Arrays.asList(Collisions.Axis.X, Collisions.Axis.Z, Collisions.Axis.Y), Arrays.asList(Collisions.Axis.Z, Collisions.Axis.X, Collisions.Axis.Y), Arrays.asList(Collisions.Axis.Z, Collisions.Axis.Y, Collisions.Axis.X));
      nonStupidityCombinations = Arrays.asList(Arrays.asList(Collisions.Axis.Y, Collisions.Axis.X, Collisions.Axis.Z), Arrays.asList(Collisions.Axis.Y, Collisions.Axis.Z, Collisions.Axis.X));
   }

   public static enum Axis {
      X {
         public double get(Vector3d vector) {
            return vector.getX();
         }

         public int get(Vector3i vector) {
            return vector.getX();
         }

         public double choose(double x, double y, double z) {
            return x;
         }

         public int choose(int x, int y, int z) {
            return x;
         }

         public Direction getPositive() {
            return Direction.EAST;
         }

         public Direction getNegative() {
            return Direction.WEST;
         }
      },
      Y {
         public double get(Vector3d vector) {
            return vector.getY();
         }

         public int get(Vector3i vector) {
            return vector.getY();
         }

         public double choose(double x, double y, double z) {
            return y;
         }

         public int choose(int x, int y, int z) {
            return y;
         }

         public Direction getPositive() {
            return Direction.UP;
         }

         public Direction getNegative() {
            return Direction.DOWN;
         }
      },
      Z {
         public double get(Vector3d vector) {
            return vector.getZ();
         }

         public int get(Vector3i vector) {
            return vector.getZ();
         }

         public double choose(double x, double y, double z) {
            return z;
         }

         public int choose(int x, int y, int z) {
            return z;
         }

         public Direction getPositive() {
            return Direction.SOUTH;
         }

         public Direction getNegative() {
            return Direction.NORTH;
         }
      };

      public abstract double get(Vector3d var1);

      public abstract int get(Vector3i var1);

      public abstract double choose(double var1, double var3, double var5);

      public abstract int choose(int var1, int var2, int var3);

      public abstract Direction getPositive();

      public abstract Direction getNegative();

      // $FF: synthetic method
      private static Collisions.Axis[] $values() {
         return new Collisions.Axis[]{X, Y, Z};
      }
   }
}
