package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTool;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.utils.data.tags.SyncedTag;
import ac.grim.grimac.utils.data.tags.SyncedTags;
import ac.grim.grimac.utils.enums.FluidTag;
import ac.grim.grimac.utils.inventory.EnchantmentHelper;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import lombok.Generated;

public final class BlockBreakSpeed {
   private static final Set<StateType> HARVESTABLE_TYPES_1_21_4;
   private static final boolean SERVER_USES_COMPONENTS_AND_RULES;

   public static double getBlockDamage(GrimPlayer player, WrappedBlockState block) {
      ItemStack tool = player.inventory.getHeldItem();
      return getBlockDamage(player, tool, block.getType());
   }

   public static double getBlockDamage(GrimPlayer player, ItemStack tool, StateType block) {
      ItemType toolType = tool.getType();
      if (player.gamemode == GameMode.CREATIVE) {
         if (SERVER_USES_COMPONENTS_AND_RULES && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
            return (Boolean)tool.getComponent(ComponentTypes.TOOL).map(ItemTool::isCanDestroyBlocksInCreative).orElse(true) ? 1.0D : 0.0D;
         } else {
            return !toolType.hasAttribute(ItemTypes.ItemAttribute.SWORD) && toolType != ItemTypes.TRIDENT && (toolType != ItemTypes.DEBUG_STICK || !player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) && (toolType != ItemTypes.MACE || !player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5)) ? 1.0D : 0.0D;
         }
      } else {
         float blockHardness = block.getHardness();
         if ((block == StateTypes.PISTON || block == StateTypes.PISTON_HEAD || block == StateTypes.STICKY_PISTON) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2)) {
            blockHardness = 0.5F;
         }

         if (blockHardness == -1.0F) {
            return 0.0D;
         } else {
            BlockBreakSpeed.ToolSpeedData toolSpeedData;
            if (SERVER_USES_COMPONENTS_AND_RULES && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
               toolSpeedData = getModernToolSpeedData(player, tool, block);
            } else {
               toolSpeedData = getLegacyToolSpeedData(player, tool, block);
            }

            float speedMultiplier = getSpeedMultiplierFromToolData(player, tool, toolSpeedData);
            boolean canHarvest = !block.isRequiresCorrectTool() || toolSpeedData.isCorrectToolForDrop || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_4) && HARVESTABLE_TYPES_1_21_4.contains(block);
            float damage = speedMultiplier / blockHardness;
            damage /= canHarvest ? 30.0F : 100.0F;
            return (double)damage;
         }
      }
   }

   private static float getSpeedMultiplierFromToolData(GrimPlayer player, ItemStack tool, BlockBreakSpeed.ToolSpeedData data) {
      float speedMultiplier = data.speedMultiplier;
      if (speedMultiplier > 1.0F) {
         if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            speedMultiplier += (float)player.compensatedEntities.self.getAttributeValue(Attributes.MINING_EFFICIENCY);
         } else {
            int digSpeed = tool.getEnchantmentLevel(EnchantmentTypes.BLOCK_EFFICIENCY);
            if (digSpeed > 0) {
               speedMultiplier += (float)(digSpeed * digSpeed + 1);
            }
         }
      }

      OptionalInt digSpeed = player.compensatedEntities.getPotionLevelForSelfPlayer(PotionTypes.HASTE);
      OptionalInt conduit = player.compensatedEntities.getPotionLevelForSelfPlayer(PotionTypes.CONDUIT_POWER);
      if (digSpeed.isPresent() || conduit.isPresent()) {
         int hasteLevel = Math.max(digSpeed.isEmpty() ? 0 : digSpeed.getAsInt(), conduit.isEmpty() ? 0 : conduit.getAsInt());
         speedMultiplier *= (float)(1.0D + 0.2D * (double)(hasteLevel + 1));
      }

      OptionalInt miningFatigue = player.compensatedEntities.getPotionLevelForSelfPlayer(PotionTypes.MINING_FATIGUE);
      if (miningFatigue.isPresent()) {
         switch(miningFatigue.getAsInt()) {
         case 0:
            speedMultiplier *= 0.3F;
            break;
         case 1:
            speedMultiplier *= 0.09F;
            break;
         case 2:
            speedMultiplier *= 0.0027F;
            break;
         default:
            speedMultiplier *= 8.1E-4F;
         }
      }

      speedMultiplier *= (float)player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
      if (player.fluidOnEyes == FluidTag.WATER) {
         if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            speedMultiplier *= (float)player.compensatedEntities.self.getAttributeValue(Attributes.SUBMERGED_MINING_SPEED);
         } else if (EnchantmentHelper.getMaximumEnchantLevel(player.inventory, EnchantmentTypes.AQUA_AFFINITY) == 0) {
            speedMultiplier /= 5.0F;
         }
      }

      if (!player.packetStateData.packetPlayerOnGround) {
         speedMultiplier /= 5.0F;
      }

      return speedMultiplier;
   }

   private static BlockBreakSpeed.ToolSpeedData getModernToolSpeedData(GrimPlayer player, ItemStack tool, StateType block) {
      Optional<ItemTool> toolComponentOpt = tool.getComponent(ComponentTypes.TOOL);
      float speedMultiplier = 1.0F;
      boolean isCorrectToolForDrop = false;
      if (toolComponentOpt.isPresent()) {
         ItemTool itemTool = (ItemTool)toolComponentOpt.get();
         speedMultiplier = itemTool.getDefaultMiningSpeed();
         boolean speedFound = false;
         boolean dropsFound = false;
         Iterator var9 = itemTool.getRules().iterator();

         while(var9.hasNext()) {
            ItemTool.Rule rule = (ItemTool.Rule)var9.next();
            MappedEntitySet<StateType.Mapped> predicate = rule.getBlocks();
            ResourceLocation tagKey = predicate.getTagKey();
            boolean isMatch;
            if (tagKey == null) {
               isMatch = predicate.getEntities().contains(block.getMapped());
            } else {
               SyncedTag<StateType> playerTag = player.tagManager.block(tagKey);
               isMatch = playerTag != null && playerTag.contains(block) || BlockTags.getByName(tagKey.getKey()).contains(block);
            }

            if (isMatch) {
               if (!speedFound && rule.getSpeed() != null) {
                  speedMultiplier = rule.getSpeed();
                  speedFound = true;
               }

               if (!dropsFound && rule.getCorrectForDrops() != null) {
                  isCorrectToolForDrop = rule.getCorrectForDrops();
                  dropsFound = true;
               }
            }

            if (speedFound && dropsFound) {
               break;
            }
         }
      }

      return new BlockBreakSpeed.ToolSpeedData(speedMultiplier, isCorrectToolForDrop);
   }

   private static BlockBreakSpeed.ToolSpeedData getLegacyToolSpeedData(GrimPlayer player, ItemStack tool, StateType block) {
      ItemType toolType = tool.getType();
      float speedMultiplier = 1.0F;
      boolean isCorrectToolForDrop = false;
      if (toolType.hasAttribute(ItemTypes.ItemAttribute.AXE)) {
         isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_AXE).contains(block);
      } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.PICKAXE)) {
         isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_PICKAXE).contains(block);
      } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.SHOVEL)) {
         isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_SHOVEL).contains(block);
      } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.HOE)) {
         isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_HOE).contains(block);
      }

      if (isCorrectToolForDrop) {
         int tier = 0;
         if (toolType.hasAttribute(ItemTypes.ItemAttribute.WOOD_TIER)) {
            speedMultiplier = 2.0F;
         } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.STONE_TIER)) {
            speedMultiplier = 4.0F;
            tier = 1;
         } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.IRON_TIER)) {
            speedMultiplier = 6.0F;
            tier = 2;
         } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.DIAMOND_TIER)) {
            speedMultiplier = 8.0F;
            tier = 3;
         } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.GOLD_TIER)) {
            speedMultiplier = 12.0F;
         } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.NETHERITE_TIER)) {
            speedMultiplier = 9.0F;
            tier = 4;
         }

         if (tier < 3 && player.tagManager.block(SyncedTags.NEEDS_DIAMOND_TOOL).contains(block)) {
            isCorrectToolForDrop = false;
         } else if (tier < 2 && player.tagManager.block(SyncedTags.NEEDS_IRON_TOOL).contains(block)) {
            isCorrectToolForDrop = false;
         } else if (tier < 1 && player.tagManager.block(SyncedTags.NEEDS_STONE_TOOL).contains(block)) {
            isCorrectToolForDrop = false;
         }
      }

      if (toolType == ItemTypes.SHEARS) {
         isCorrectToolForDrop = true;
         if (block != StateTypes.COBWEB && !Materials.isLeaves(block)) {
            if (BlockTags.WOOL.contains(block)) {
               speedMultiplier = 5.0F;
            } else if (block != StateTypes.VINE && block != StateTypes.GLOW_LICHEN) {
               isCorrectToolForDrop = block == StateTypes.COBWEB || block == StateTypes.REDSTONE_WIRE || block == StateTypes.TRIPWIRE;
            } else {
               speedMultiplier = 2.0F;
            }
         } else {
            speedMultiplier = 15.0F;
         }
      }

      if (toolType.hasAttribute(ItemTypes.ItemAttribute.SWORD)) {
         if (block == StateTypes.COBWEB) {
            speedMultiplier = 15.0F;
         } else if (player.tagManager.block(SyncedTags.SWORD_EFFICIENT).contains(block)) {
            speedMultiplier = 1.5F;
         }

         isCorrectToolForDrop = block == StateTypes.COBWEB;
      }

      return new BlockBreakSpeed.ToolSpeedData(speedMultiplier, isCorrectToolForDrop);
   }

   @Generated
   private BlockBreakSpeed() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      HARVESTABLE_TYPES_1_21_4 = Sets.newHashSet(new StateType[]{StateTypes.BELL, StateTypes.LANTERN, StateTypes.SOUL_LANTERN, StateTypes.COPPER_DOOR, StateTypes.EXPOSED_COPPER_DOOR, StateTypes.OXIDIZED_COPPER_DOOR, StateTypes.WEATHERED_COPPER_DOOR, StateTypes.WAXED_COPPER_DOOR, StateTypes.WAXED_EXPOSED_COPPER_DOOR, StateTypes.WAXED_OXIDIZED_COPPER_DOOR, StateTypes.WAXED_WEATHERED_COPPER_DOOR, StateTypes.IRON_DOOR, StateTypes.HEAVY_WEIGHTED_PRESSURE_PLATE, StateTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, StateTypes.POLISHED_BLACKSTONE_PRESSURE_PLATE, StateTypes.STONE_PRESSURE_PLATE, StateTypes.BREWING_STAND, StateTypes.ENDER_CHEST});
      SERVER_USES_COMPONENTS_AND_RULES = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5);
   }

   static record ToolSpeedData(float speedMultiplier, boolean isCorrectToolForDrop) {
      ToolSpeedData(float speedMultiplier, boolean isCorrectToolForDrop) {
         this.speedMultiplier = speedMultiplier;
         this.isCorrectToolForDrop = isCorrectToolForDrop;
      }

      public float speedMultiplier() {
         return this.speedMultiplier;
      }

      public boolean isCorrectToolForDrop() {
         return this.isCorrectToolForDrop;
      }
   }
}
