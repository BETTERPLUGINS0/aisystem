package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.blocks.DoorHandler;
import ac.grim.grimac.utils.math.Vector3dm;
import lombok.Generated;

public final class FluidTypeFlowing {
   public static Vector3dm getFlow(GrimPlayer player, int originalX, int originalY, int originalZ) {
      float fluidLevel = (float)Math.min(player.compensatedWorld.getFluidLevelAt(originalX, originalY, originalZ), 0.8888888888888888D);
      ClientVersion version = player.getClientVersion();
      if (fluidLevel == 0.0F) {
         return new Vector3dm();
      } else {
         double d0 = 0.0D;
         double d1 = 0.0D;
         BlockFace[] var10 = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
         int var11 = var10.length;

         int modifiedX;
         for(int var12 = 0; var12 < var11; ++var12) {
            BlockFace enumdirection = var10[var12];
            modifiedX = originalX + enumdirection.getModX();
            int modifiedZ = originalZ + enumdirection.getModZ();
            if (affectsFlow(player, originalX, originalY, originalZ, modifiedX, originalY, modifiedZ)) {
               float f = (float)Math.min(player.compensatedWorld.getFluidLevelAt(modifiedX, originalY, modifiedZ), 0.8888888888888888D);
               float f1 = 0.0F;
               if (f == 0.0F) {
                  StateType mat = player.compensatedWorld.getBlockType((double)modifiedX, (double)originalY, (double)modifiedZ);
                  if (Materials.isSolidBlockingBlacklist(mat, version) && affectsFlow(player, originalX, originalY, originalZ, modifiedX, originalY - 1, modifiedZ)) {
                     f = (float)Math.min(player.compensatedWorld.getFluidLevelAt(modifiedX, originalY - 1, modifiedZ), 0.8888888888888888D);
                     if (f > 0.0F) {
                        f1 = fluidLevel - (f - 0.8888889F);
                     }
                  }
               } else if (f > 0.0F) {
                  f1 = fluidLevel - f;
               }

               if (f1 != 0.0F) {
                  d0 += (double)((float)enumdirection.getModX() * f1);
                  d1 += (double)((float)enumdirection.getModZ() * f1);
               }
            }
         }

         Vector3dm vec3d = new Vector3dm(d0, 0.0D, d1);
         WrappedBlockState state = player.compensatedWorld.getBlock(originalX, originalY, originalZ);
         if ((state.getType() == StateTypes.WATER || state.getType() == StateTypes.LAVA) && state.getLevel() >= 8) {
            BlockFace[] var21 = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            int var22 = var21.length;

            for(modifiedX = 0; modifiedX < var22; ++modifiedX) {
               BlockFace enumdirection = var21[modifiedX];
               if (isSolidFace(player, originalX, originalY, originalZ, enumdirection) || isSolidFace(player, originalX, originalY + 1, originalZ, enumdirection)) {
                  vec3d = normalizeVectorWithoutNaN(vec3d).add(0.0D, -6.0D, 0.0D);
                  break;
               }
            }
         }

         return normalizeVectorWithoutNaN(vec3d);
      }
   }

   private static boolean affectsFlow(GrimPlayer player, int originalX, int originalY, int originalZ, int x2, int y2, int z2) {
      return isEmpty(player, x2, y2, z2) || isSame(player, originalX, originalY, originalZ, x2, y2, z2);
   }

   private static boolean isSolidFace(GrimPlayer player, int originalX, int y, int originalZ, BlockFace direction) {
      int x = originalX + direction.getModX();
      int z = originalZ + direction.getModZ();
      WrappedBlockState data = player.compensatedWorld.getBlock(x, y, z);
      StateType type = data.getType();
      if (isSame(player, x, y, z, originalX, y, originalZ)) {
         return false;
      } else if (type == StateTypes.ICE) {
         return false;
      } else {
         if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_12)) {
            if (type == StateTypes.PISTON || type == StateTypes.STICKY_PISTON) {
               return data.getFacing().getOppositeFace() == direction || CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, 0, 0, 0).isFullBlock();
            }

            if (type == StateTypes.PISTON_HEAD) {
               return data.getFacing() == direction;
            }
         }

         if (player.getClientVersion().isOlderThan(ClientVersion.V_1_12)) {
            return !Materials.isSolidBlockingBlacklist(type, player.getClientVersion());
         } else if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_12) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            if (!Materials.isStairs(type) && !Materials.isLeaves(type) && !Materials.isShulker(type) && !Materials.isGlassBlock(type) && !BlockTags.TRAPDOORS.contains(type)) {
               if (type != StateTypes.BEACON && !BlockTags.CAULDRONS.contains(type) && type != StateTypes.GLOWSTONE && type != StateTypes.SEA_LANTERN && type != StateTypes.CONDUIT) {
                  if (type != StateTypes.PISTON && type != StateTypes.STICKY_PISTON && type != StateTypes.PISTON_HEAD) {
                     return type == StateTypes.SOUL_SAND || CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z).isFullBlock();
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else if (Materials.isLeaves(type)) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2);
         } else if (type == StateTypes.SNOW) {
            return data.getLayers() == 8;
         } else if (Materials.isStairs(type)) {
            return data.getFacing() == direction;
         } else if (type == StateTypes.COMPOSTER) {
            return true;
         } else if (type == StateTypes.SOUL_SAND) {
            return player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2) || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16);
         } else if (type == StateTypes.LADDER) {
            return data.getFacing().getOppositeFace() == direction;
         } else if (!BlockTags.TRAPDOORS.contains(type)) {
            if (BlockTags.DOORS.contains(type)) {
               CollisionData collisionData = CollisionData.getData(type);
               if (collisionData.dynamic instanceof DoorHandler) {
                  BlockFace dir = ((DoorHandler)collisionData.dynamic).fetchDirection(player, player.getClientVersion(), data, x, y, z);
                  return dir.getOppositeFace() == direction;
               }
            }

            return CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z).isFullBlock();
         } else {
            return data.getFacing().getOppositeFace() == direction && data.isOpen();
         }
      }
   }

   private static Vector3dm normalizeVectorWithoutNaN(Vector3dm vector) {
      double var0 = vector.length();
      return var0 < 1.0E-4D ? new Vector3dm() : vector.multiply(1.0D / var0);
   }

   public static boolean isEmpty(GrimPlayer player, int x, int y, int z) {
      return player.compensatedWorld.getFluidLevelAt(x, y, z) == 0.0D;
   }

   public static boolean isSame(GrimPlayer player, int x1, int y1, int z1, int x2, int y2, int z2) {
      return player.compensatedWorld.getWaterFluidLevelAt(x1, y1, z1) > 0.0D && player.compensatedWorld.getWaterFluidLevelAt(x2, y2, z2) > 0.0D || player.compensatedWorld.getLavaFluidLevelAt(x1, y1, z1) > 0.0D && player.compensatedWorld.getLavaFluidLevelAt(x2, y2, z2) > 0.0D;
   }

   @Generated
   private FluidTypeFlowing() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
