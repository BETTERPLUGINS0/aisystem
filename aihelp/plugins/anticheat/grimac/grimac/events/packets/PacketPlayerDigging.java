package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.impl.movement.NoSlow;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.FoodProperties;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemConsumable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.item.ItemBehaviour;
import ac.grim.grimac.utils.item.ItemBehaviourRegistry;

public class PacketPlayerDigging extends PacketListenerAbstract {
   private static final boolean RELIABLE_COMPONENT_SYSTEM;
   private static final boolean SERVER_HAS_OFFHAND;

   public PacketPlayerDigging() {
      super(PacketListenerPriority.LOW);
   }

   public static void handleUseItem(@NotNull GrimPlayer player, @NotNull InteractionHand hand) {
      ItemStack item = player.inventory.getItemInHand(hand);
      if (item == null) {
         player.packetStateData.setSlowedByUsingItem(false);
      } else if (player.checkManager.getCompensatedCooldown().hasItem(item)) {
         player.packetStateData.setSlowedByUsingItem(false);
      } else {
         ItemType material = item.getType();
         if (RELIABLE_COMPONENT_SYSTEM && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
            ItemBehaviour itemBehaviour = ItemBehaviourRegistry.getItemBehaviour(material);
            if (itemBehaviour.canUse(item, player.compensatedWorld, player, hand)) {
               player.packetStateData.setSlowedByUsingItem(true);
               player.packetStateData.itemInUseHand = hand;
            } else {
               player.packetStateData.setSlowedByUsingItem(false);
            }

         } else {
            ItemConsumable consumable = (ItemConsumable)item.getComponentOr(ComponentTypes.CONSUMABLE, (Object)null);
            FoodProperties foodComponent = (FoodProperties)item.getComponentOr(ComponentTypes.FOOD, (Object)null);
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) && consumable != null && foodComponent == null) {
               player.packetStateData.setSlowedByUsingItem(true);
               player.packetStateData.itemInUseHand = hand;
            }

            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) && foodComponent != null) {
               if (foodComponent.isCanAlwaysEat() || player.food < 20 || player.gamemode == GameMode.CREATIVE) {
                  player.packetStateData.setSlowedByUsingItem(true);
                  player.packetStateData.itemInUseHand = hand;
                  return;
               }

               player.packetStateData.setSlowedByUsingItem(false);
            }

            if (material.hasAttribute(ItemTypes.ItemAttribute.EDIBLE) && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_15) || player.gamemode != GameMode.CREATIVE) || material == ItemTypes.POTION || material == ItemTypes.MILK_BUCKET) {
               label182: {
                  if (item.getType() == ItemTypes.SPLASH_POTION) {
                     return;
                  }

                  if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9) && item.getLegacyData() > 16384) {
                     return;
                  }

                  if (material != ItemTypes.POTION && material != ItemTypes.MILK_BUCKET && material != ItemTypes.GOLDEN_APPLE && material != ItemTypes.ENCHANTED_GOLDEN_APPLE && material != ItemTypes.HONEY_BOTTLE && material != ItemTypes.SUSPICIOUS_STEW && material != ItemTypes.CHORUS_FRUIT) {
                     if (!item.getType().hasAttribute(ItemTypes.ItemAttribute.EDIBLE) || (player.platformPlayer == null || player.food >= 20) && player.gamemode != GameMode.CREATIVE) {
                        player.packetStateData.setSlowedByUsingItem(false);
                        break label182;
                     }

                     player.packetStateData.setSlowedByUsingItem(true);
                     player.packetStateData.itemInUseHand = hand;
                     return;
                  }

                  player.packetStateData.setSlowedByUsingItem(true);
                  player.packetStateData.itemInUseHand = hand;
                  return;
               }
            }

            if (material == ItemTypes.SHIELD && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
               player.packetStateData.setSlowedByUsingItem(true);
               player.packetStateData.itemInUseHand = hand;
            } else {
               NBTCompound nbt = item.getNBT();
               if (material == ItemTypes.CROSSBOW && nbt != null && nbt.getBoolean("Charged")) {
                  player.packetStateData.setSlowedByUsingItem(false);
               } else {
                  if (material == ItemTypes.TRIDENT && item.getDamageValue() < item.getMaxDamage() - 1 && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13_2) || player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8))) {
                     player.packetStateData.setSlowedByUsingItem(item.getEnchantmentLevel(EnchantmentTypes.RIPTIDE) <= 0);
                     player.packetStateData.itemInUseHand = hand;
                  }

                  if (material == ItemTypes.BOW || material == ItemTypes.CROSSBOW) {
                     player.packetStateData.setSlowedByUsingItem(false);
                  }

                  if (material == ItemTypes.SPYGLASS && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
                     player.packetStateData.setSlowedByUsingItem(true);
                     player.packetStateData.itemInUseHand = hand;
                  }

                  if (material == ItemTypes.GOAT_HORN && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19)) {
                     player.packetStateData.setSlowedByUsingItem(true);
                     player.packetStateData.itemInUseHand = hand;
                  }

                  if (material.hasAttribute(ItemTypes.ItemAttribute.SWORD)) {
                     if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
                        player.packetStateData.setSlowedByUsingItem(true);
                     } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
                        player.packetStateData.setSlowedByUsingItem(false);
                     }
                  }

               }
            }
         }
      }
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      GrimPlayer player;
      if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
         WrapperPlayClientPlayerDigging dig = new WrapperPlayClientPlayerDigging(event);
         if (dig.getAction() == DiggingAction.RELEASE_USE_ITEM) {
            player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) {
               return;
            }

            player.packetStateData.setSlowedByUsingItem(false);
            player.packetStateData.slowedByUsingItemTransaction = player.lastTransactionReceived.get();
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
               ItemStack hand = player.inventory.getItemInHand(player.packetStateData.itemInUseHand);
               if (hand.getType() == ItemTypes.TRIDENT && hand.getEnchantmentLevel(EnchantmentTypes.RIPTIDE) > 0) {
                  player.packetStateData.tryingToRiptide = true;
               }
            }
         }
      }

      GrimPlayer player;
      if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) || event.getPacketType() == PacketType.Play.Client.CLIENT_TICK_END) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player != null && player.packetStateData.isSlowedByUsingItem() && !player.packetStateData.lastPacketWasTeleport && !player.packetStateData.lastPacketWasOnePointSeventeenDuplicate) {
            boolean slotChanged = player.packetStateData.itemInUseHand != InteractionHand.OFF_HAND && player.packetStateData.getSlowedByUsingItemSlot() != player.packetStateData.lastSlotSelected;
            if (slotChanged || player.inventory.getItemInHand(player.packetStateData.itemInUseHand).isEmpty()) {
               player.packetStateData.setSlowedByUsingItem(false);
               if (slotChanged) {
                  ((NoSlow)player.checkManager.getPostPredictionCheck(NoSlow.class)).didSlotChangeLastTick = true;
               }
            }
         }
      }

      if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
         int slot = (new WrapperPlayClientHeldItemChange(event)).getSlot();
         if (slot > 8 || slot < 0) {
            return;
         }

         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         CheckManagerListener.handleQueuedPlaces(player, false, 0.0F, 0.0F, System.currentTimeMillis());
         if (player.packetStateData.lastSlotSelected != slot) {
            if (player.isResetItemUsageOnSlotChange() && GrimAPI.INSTANCE.getItemResetHandler().getItemUsageHand(player.platformPlayer) == InteractionHand.MAIN_HAND) {
               GrimAPI.INSTANCE.getItemResetHandler().resetItemUsage(player.platformPlayer);
            }

            if (player.canSkipTicks() && !player.isTickingReliablyFor(3) && player.packetStateData.itemInUseHand != InteractionHand.OFF_HAND) {
               player.packetStateData.setSlowedByUsingItem(false);
            }
         }

         player.packetStateData.lastSlotSelected = slot;
      }

      if (event.getPacketType() == PacketType.Play.Client.USE_ITEM || event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT && (new WrapperPlayClientPlayerBlockPlacement(event)).getFace() == BlockFace.OTHER) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_8) && player.gamemode == GameMode.SPECTATOR) {
            return;
         }

         InteractionHand hand = SERVER_HAS_OFFHAND && event.getPacketType() == PacketType.Play.Client.USE_ITEM ? (new WrapperPlayClientUseItem(event)).getHand() : InteractionHand.MAIN_HAND;
         player.packetStateData.slowedByUsingItemTransaction = player.lastTransactionReceived.get();
         if (player.isResetItemUsageOnItemUse()) {
            GrimAPI.INSTANCE.getItemResetHandler().resetItemUsage(player.platformPlayer);
         }

         handleUseItem(player, hand);
      }

   }

   static {
      RELIABLE_COMPONENT_SYSTEM = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4);
      SERVER_HAS_OFFHAND = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9);
   }
}
