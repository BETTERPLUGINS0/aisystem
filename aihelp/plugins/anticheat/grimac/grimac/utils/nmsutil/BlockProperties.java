package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.data.MainSupportingBlockData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHorse;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.math.GrimMath;
import lombok.Generated;

public final class BlockProperties {
   public static float getFrictionInfluencedSpeed(float f, GrimPlayer player) {
      if (player.lastOnGround) {
         return (float)(player.speed * (double)(0.21600002F / (f * f * f)));
      } else {
         if (player.inVehicle()) {
            PacketEntity riding = player.compensatedEntities.self.getRiding();
            if (riding.type == EntityTypes.PIG || riding instanceof PacketEntityNautilus || riding instanceof PacketEntityHorse) {
               return (float)(player.speed * 0.10000000149011612D);
            }

            if (riding instanceof PacketEntityStrider) {
               PacketEntityStrider strider = (PacketEntityStrider)riding;
               if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20)) {
                  return (float)player.speed * 0.1F;
               }

               return (float)strider.getAttributeValue(Attributes.MOVEMENT_SPEED) * (strider.isShaking ? 0.66F : 1.0F) * 0.1F;
            }
         }

         if (player.isFlying) {
            return player.flySpeed * 20.0F * (player.isSprinting ? 0.1F : 0.05F);
         } else if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19_4)) {
            return player.isSprinting ? 0.025999999F : 0.02F;
         } else {
            return player.lastSprintingForSpeed ? 0.025999999F : 0.02F;
         }
      }
   }

   public static StateType getOnPos(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
      if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
         return getOnBlock(player, playerPos.getX(), playerPos.getY(), playerPos.getZ());
      } else {
         Vector3i pos = getOnPos(player, playerPos, mainSupportingBlockData, 0.2F);
         return player.compensatedWorld.getBlockType((double)pos.x, (double)pos.y, (double)pos.z);
      }
   }

   public static float getFriction(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
      if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
         double searchBelowAmount = 0.5000001D;
         if (player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
            searchBelowAmount = 1.0D;
         }

         StateType type = player.compensatedWorld.getBlockType(playerPos.getX(), playerPos.getY() - searchBelowAmount, playerPos.getZ());
         return getMaterialFriction(player, type);
      } else {
         StateType underPlayer = getBlockPosBelowThatAffectsMyMovement(player, mainSupportingBlockData, playerPos);
         return getMaterialFriction(player, underPlayer);
      }
   }

   public static float getBlockSpeedFactor(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
      if (player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
         return 1.0F;
      } else if (!player.isGliding && !player.isFlying) {
         if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
            return getBlockSpeedFactorLegacy(player, playerPos);
         } else {
            WrappedBlockState inBlock = player.compensatedWorld.getBlock(playerPos.getX(), playerPos.getY(), playerPos.getZ());
            float inBlockSpeedFactor = getBlockSpeedFactor(player, inBlock.getType());
            if (inBlockSpeedFactor == 1.0F && inBlock.getType() != StateTypes.WATER && inBlock.getType() != StateTypes.BUBBLE_COLUMN) {
               StateType underPlayer = getBlockPosBelowThatAffectsMyMovement(player, mainSupportingBlockData, playerPos);
               return getModernVelocityMultiplier(player, getBlockSpeedFactor(player, underPlayer));
            } else {
               return getModernVelocityMultiplier(player, inBlockSpeedFactor);
            }
         }
      } else {
         return 1.0F;
      }
   }

   public static boolean onHoneyBlock(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
      if (player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
         return false;
      } else {
         StateType inBlock = player.compensatedWorld.getBlockType(playerPos.getX(), playerPos.getY(), playerPos.getZ());
         return inBlock == StateTypes.HONEY_BLOCK || getBlockPosBelowThatAffectsMyMovement(player, mainSupportingBlockData, playerPos) == StateTypes.HONEY_BLOCK;
      }
   }

   private static StateType getBlockPosBelowThatAffectsMyMovement(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
      Vector3i pos = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4) ? new Vector3i(GrimMath.floor(playerPos.getX()), GrimMath.floor(playerPos.getY() - 0.5000001D), GrimMath.floor(playerPos.getZ())) : getOnPos(player, playerPos, mainSupportingBlockData, 0.500001F);
      return player.compensatedWorld.getBlockType((double)pos.x, (double)pos.y, (double)pos.z);
   }

   private static Vector3i getOnPos(GrimPlayer player, Vector3d playerPos, MainSupportingBlockData mainSupportingBlockData, float searchBelowPlayer) {
      Vector3i mainBlockPos = mainSupportingBlockData.blockPos();
      if (mainBlockPos == null) {
         return new Vector3i(GrimMath.floor(playerPos.getX()), GrimMath.floor(playerPos.getY() - (double)searchBelowPlayer), GrimMath.floor(playerPos.getZ()));
      } else {
         StateType blockstate = player.compensatedWorld.getBlockType((double)mainBlockPos.x, (double)mainBlockPos.y, (double)mainBlockPos.z);
         boolean shouldReturn = (!((double)searchBelowPlayer <= 0.5D) || !BlockTags.FENCES.contains(blockstate)) && !BlockTags.WALLS.contains(blockstate) && !BlockTags.FENCE_GATES.contains(blockstate);
         return shouldReturn ? mainBlockPos.withY(GrimMath.floor(playerPos.getY() - (double)searchBelowPlayer)) : mainBlockPos;
      }
   }

   public static float getMaterialFriction(GrimPlayer player, StateType material) {
      float friction = 0.6F;
      if (material == StateTypes.ICE) {
         friction = 0.98F;
      }

      if (material == StateTypes.SLIME_BLOCK && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8)) {
         friction = 0.8F;
      }

      if (material == StateTypes.HONEY_BLOCK && player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
         friction = 0.8F;
      }

      if (material == StateTypes.PACKED_ICE) {
         friction = 0.98F;
      }

      if (material == StateTypes.FROSTED_ICE) {
         friction = 0.98F;
      }

      if (material == StateTypes.BLUE_ICE) {
         friction = 0.98F;
         if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
            friction = 0.989F;
         }
      }

      return friction;
   }

   private static StateType getOnBlock(GrimPlayer player, double x, double y, double z) {
      StateType block1 = player.compensatedWorld.getBlockType((double)GrimMath.floor(x), (double)GrimMath.floor(y - 0.20000000298023224D), (double)GrimMath.floor(z));
      if (block1.isAir()) {
         StateType block2 = player.compensatedWorld.getBlockType((double)GrimMath.floor(x), (double)GrimMath.floor(y - 1.2000000476837158D), (double)GrimMath.floor(z));
         if (Materials.isFence(block2) || Materials.isWall(block2) || Materials.isGate(block2)) {
            return block2;
         }
      }

      return block1;
   }

   private static float getBlockSpeedFactorLegacy(GrimPlayer player, Vector3d pos) {
      StateType block = player.compensatedWorld.getBlockType(pos.getX(), pos.getY(), pos.getZ());
      if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_16_1)) {
         StateType onBlock = getOnBlock(player, pos.getX(), pos.getY(), pos.getZ());
         if (onBlock == StateTypes.SOUL_SAND && player.inventory.getBoots().getEnchantmentLevel(EnchantmentTypes.SOUL_SPEED) > 0) {
            return 1.0F;
         }
      }

      float speed = getBlockSpeedFactor(player, block);
      if (speed == 1.0F && block != StateTypes.SOUL_SAND && block != StateTypes.WATER && block != StateTypes.BUBBLE_COLUMN) {
         StateType block2 = player.compensatedWorld.getBlockType(pos.getX(), pos.getY() - 0.5000001D, pos.getZ());
         return getBlockSpeedFactor(player, block2);
      } else {
         return speed;
      }
   }

   private static float getBlockSpeedFactor(GrimPlayer player, StateType type) {
      if (type == StateTypes.HONEY_BLOCK) {
         return 0.4F;
      } else if (type == StateTypes.SOUL_SAND) {
         return player.getClientVersion().isOlderThan(ClientVersion.V_1_21) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16_2) && player.inventory.getBoots().getEnchantmentLevel(EnchantmentTypes.SOUL_SPEED) > 0 ? 1.0F : 0.4F;
      } else {
         return 1.0F;
      }
   }

   private static float getModernVelocityMultiplier(GrimPlayer player, float blockSpeedFactor) {
      return player.getClientVersion().isOlderThan(ClientVersion.V_1_21) ? blockSpeedFactor : (float)GrimMath.lerp((double)((float)player.compensatedEntities.self.getAttributeValue(Attributes.MOVEMENT_EFFICIENCY)), (double)blockSpeedFactor, 1.0D);
   }

   @Generated
   private BlockProperties() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
