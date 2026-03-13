package com.nisovin.shopkeepers.shopcreation;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopType;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.container.ShopContainers;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.EventUtils;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.interaction.TestPlayerInteractEvent;
import com.nisovin.shopkeepers.util.inventory.InventoryUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.MutableLong;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

class CreateListener implements Listener {
   private static final long INTERACTION_DELAY_MILLIS = 50L;
   private final SKShopkeepersPlugin plugin;
   private final ContainerSelection containerSelection;
   private final ShopkeeperPlacement shopkeeperPlacement;
   private final Map<UUID, MutableLong> lastHandledPlayerInteractionsMillis = new HashMap();
   private boolean shopCreationItemSelectedMessageEnabled;
   private static final long ANVIL_DEBUG_MESSAGE_THROTTLE_MILLIS;
   @Nullable
   private UUID lastAnvilDebugMessagePlayer = null;
   private long lastAnvilDebugMessageMillis = 0L;

   CreateListener(SKShopkeepersPlugin plugin, ContainerSelection containerSelection, ShopkeeperPlacement shopkeeperPlacement) {
      this.plugin = plugin;
      this.containerSelection = containerSelection;
      this.shopkeeperPlacement = shopkeeperPlacement;
   }

   void onEnable() {
      this.shopCreationItemSelectedMessageEnabled = !Messages.creationItemSelected.isPlainTextEmpty();
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      EventUtils.enforceExecuteFirst(PlayerInteractEvent.class, EventPriority.LOWEST, (Plugin)this.plugin);
   }

   void onDisable() {
      HandlerList.unregisterAll(this);
      this.lastHandledPlayerInteractionsMillis.clear();
      ShopCreationItemSelectionTask.onDisable();
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      UUID uniqueId = player.getUniqueId();
      this.lastHandledPlayerInteractionsMillis.remove(uniqueId);
      ShopCreationItemSelectionTask.cleanupAndCancel(player);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onItemHeld(PlayerItemHeldEvent event) {
      if (this.shopCreationItemSelectedMessageEnabled) {
         Player player = event.getPlayer();
         ItemStack newItemInHand = player.getInventory().getItem(event.getNewSlot());
         if (ShopCreationItem.isShopCreationItem(newItemInHand)) {
            if (this.plugin.hasCreatePermission(player)) {
               ShopCreationItemSelectionTask.start(this.plugin, player);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = false
   )
   void onPlayerInteract(PlayerInteractEvent event) {
      if (!(event instanceof TestPlayerInteractEvent)) {
         Action action = event.getAction();
         if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            ItemStack itemInHand = event.getItem();
            ShopCreationItem shopCreationItem = new ShopCreationItem(itemInHand);
            if (shopCreationItem.isShopCreationItem()) {
               Player player = event.getPlayer();
               Log.debug(() -> {
                  String var10000 = player.getName();
                  return "Player " + var10000 + " is interacting with the shop creation item: action=" + String.valueOf(action) + ", hand=" + String.valueOf(event.getHand());
               });
               Result useItemInHand = event.useItemInHand();
               if (Settings.preventShopCreationItemRegularUsage && !PermissionUtils.hasPermission(player, "shopkeeper.bypass")) {
                  Log.debug("  Preventing normal shop creation item usage");
                  event.setCancelled(true);
               }

               if (event.getHand() != EquipmentSlot.HAND) {
                  Log.debug("  Ignoring off-hand interaction");
               } else if (useItemInHand == Result.DENY) {
                  Log.debug("  Ignoring already cancelled item interaction");
               } else {
                  event.setCancelled(true);
                  UUID uniqueId = player.getUniqueId();
                  MutableLong lastHandledInteractionMillis = (MutableLong)this.lastHandledPlayerInteractionsMillis.computeIfAbsent(uniqueId, (uuid) -> {
                     return new MutableLong();
                  });

                  assert lastHandledInteractionMillis != null;

                  long nowMillis = System.currentTimeMillis();
                  long millisSinceLastInteraction = nowMillis - lastHandledInteractionMillis.getValue();
                  if (millisSinceLastInteraction < 50L) {
                     Log.debug(() -> {
                        String var10000 = player.getName();
                        return "Ignoring interaction of player " + var10000 + ": Last handled interaction was " + millisSinceLastInteraction + " ms ago.";
                     });
                  } else {
                     lastHandledInteractionMillis.setValue(nowMillis);
                     ShopType<?> shopType = (ShopType)this.plugin.getShopTypeRegistry().getSelection(player);
                     boolean isShopTypeFixed = false;
                     ShopObjectType<?> shopObjectType = (ShopObjectType)this.plugin.getShopObjectTypeRegistry().getSelection(player);
                     boolean isShopObjectTypeFixed = false;
                     if (shopType != null && shopObjectType != null) {
                        String itemShopTypeId = shopCreationItem.getShopTypeId();
                        if (itemShopTypeId != null) {
                           AbstractShopType<?> itemShopType = (AbstractShopType)this.plugin.getShopTypeRegistry().get(itemShopTypeId);
                           if (itemShopType == null) {
                              TextUtils.sendMessage(player, (Text)Messages.commandShopTypeArgumentInvalid, (Object[])("argument", itemShopTypeId));
                              return;
                           }

                           if (!(itemShopType instanceof PlayerShopType)) {
                              TextUtils.sendMessage(player, (Text)Messages.commandShopTypeArgumentNoPlayerShop, (Object[])("argument", itemShopTypeId));
                              return;
                           }

                           shopType = itemShopType;
                           isShopTypeFixed = true;
                        }

                        String itemShopObjectTypeId = shopCreationItem.getObjectTypeId();
                        if (itemShopObjectTypeId != null) {
                           AbstractShopObjectType<?> itemShopObjectType = (AbstractShopObjectType)this.plugin.getShopObjectTypeRegistry().get(itemShopObjectTypeId);
                           if (itemShopObjectType == null) {
                              TextUtils.sendMessage(player, (Text)Messages.commandShopObjectTypeArgumentInvalid, (Object[])("argument", itemShopObjectTypeId));
                              return;
                           }

                           shopObjectType = itemShopObjectType;
                           isShopObjectTypeFixed = true;
                        }

                        if (action == Action.RIGHT_CLICK_AIR) {
                           if (player.isSneaking() ^ Settings.invertShopTypeAndObjectTypeSelection) {
                              if (!isShopObjectTypeFixed) {
                                 this.plugin.getShopObjectTypeRegistry().selectNext(player);
                              }
                           } else if (!isShopTypeFixed) {
                              this.plugin.getShopTypeRegistry().selectNext(player);
                           }
                        } else if (action == Action.LEFT_CLICK_AIR) {
                           if (player.isSneaking() ^ Settings.invertShopTypeAndObjectTypeSelection) {
                              if (!isShopObjectTypeFixed) {
                                 this.plugin.getShopObjectTypeRegistry().selectPrevious(player);
                              }
                           } else if (!isShopTypeFixed) {
                              this.plugin.getShopTypeRegistry().selectPrevious(player);
                           }
                        } else if (action == Action.RIGHT_CLICK_BLOCK) {
                           Block clickedBlock = (Block)Unsafe.assertNonNull(event.getClickedBlock());
                           Block selectedContainer = this.containerSelection.getSelectedContainer(player);
                           if (selectedContainer != null && !ShopContainers.isSupportedContainer(selectedContainer.getType())) {
                              this.containerSelection.selectContainer(player, (Block)null);
                              selectedContainer = null;
                           }

                           boolean isContainerSelection = false;
                           if (!clickedBlock.equals(selectedContainer)) {
                              if (ShopContainers.isSupportedContainer(clickedBlock.getType())) {
                                 isContainerSelection = true;
                                 if (this.containerSelection.validateContainer(player, clickedBlock)) {
                                    this.containerSelection.selectContainer(player, clickedBlock);
                                    TextUtils.sendMessage(player, (Text)Messages.containerSelected);
                                 }
                              } else if (ItemUtils.isContainer(clickedBlock.getType())) {
                                 isContainerSelection = true;
                                 TextUtils.sendMessage(player, (Text)Messages.unsupportedContainer);
                              }
                           }

                           if (!isContainerSelection) {
                              if (selectedContainer == null) {
                                 TextUtils.sendMessage(player, (Text)Messages.mustSelectContainer);
                                 return;
                              }

                              assert ShopContainers.isSupportedContainer(selectedContainer.getType());

                              assert shopType instanceof PlayerShopType;

                              BlockFace clickedBlockFace = event.getBlockFace();
                              Location spawnLocation = this.shopkeeperPlacement.determineSpawnLocation(player, clickedBlock, clickedBlockFace);
                              ShopCreationData creationData = PlayerShopCreationData.create(player, (PlayerShopType)((PlayerShopType)shopType), (ShopObjectType)shopObjectType, spawnLocation, clickedBlockFace, selectedContainer);
                              Shopkeeper shopkeeper = this.plugin.handleShopkeeperCreation(creationData);
                              if (shopkeeper != null) {
                                 this.containerSelection.selectContainer(player, (Block)null);
                                 Bukkit.getScheduler().runTask(this.plugin, () -> {
                                    ItemStack newItemInMainHand = ItemUtils.decreaseItemAmount(itemInHand, 1);
                                    player.getInventory().setItemInMainHand(newItemInMainHand);
                                 });
                              }
                           }
                        }

                     } else {
                        TextUtils.sendMessage(player, (Text)Messages.noPermission);
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onBlockDispense(BlockDispenseEvent event) {
      if (Settings.preventShopCreationItemRegularUsage && ShopCreationItem.isShopCreationItem(event.getItem())) {
         Log.debug(() -> {
            return "Preventing dispensing of shop creation item at " + TextUtils.getLocationString(event.getBlock());
         });
         event.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      this.handleEntityInteraction(event);
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
      this.handleEntityInteraction(event);
   }

   private void handleEntityInteraction(PlayerInteractEntityEvent event) {
      if (Settings.preventShopCreationItemRegularUsage) {
         Player player = event.getPlayer();
         if (!PermissionUtils.hasPermission(player, "shopkeeper.bypass")) {
            ItemStack itemInHand = player.getInventory().getItem(event.getHand());
            if (ShopCreationItem.isShopCreationItem(itemInHand)) {
               Log.debug(() -> {
                  return event instanceof PlayerInteractAtEntityEvent ? "Preventing interaction at entity with shop creation item for player " + TextUtils.getPlayerString(player) : "Preventing entity interaction with shop creation item for player " + TextUtils.getPlayerString(player);
               });
               event.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOW,
      ignoreCancelled = true
   )
   void onPrepareAnvilEvent(PrepareAnvilEvent event) {
      if (Settings.preventShopCreationItemRegularUsage) {
         if (!ItemUtils.isEmpty(event.getResult())) {
            Inventory anvilInventory = event.getInventory();
            if (ShopCreationItem.isShopCreationItem(anvilInventory.getItem(0)) || ShopCreationItem.isShopCreationItem(anvilInventory.getItem(1))) {
               if (Settings.debug) {
                  Player player = (Player)event.getView().getPlayer();
                  UUID playerUniqueId = player.getUniqueId();
                  long nowMillis = System.currentTimeMillis();
                  if (!playerUniqueId.equals(this.lastAnvilDebugMessagePlayer) || nowMillis - this.lastAnvilDebugMessageMillis > ANVIL_DEBUG_MESSAGE_THROTTLE_MILLIS) {
                     this.lastAnvilDebugMessagePlayer = playerUniqueId;
                     this.lastAnvilDebugMessageMillis = nowMillis;
                     Log.debug(() -> {
                        return "Preventing renaming of shop creation item by " + player.getName() + " (debug output is throttled)";
                     });
                  }
               }

               event.setResult((ItemStack)null);
               InventoryUtils.updateInventoryLater((Inventory)anvilInventory);
            }
         }
      }
   }

   static {
      ANVIL_DEBUG_MESSAGE_THROTTLE_MILLIS = TimeUnit.SECONDS.toMillis(5L);
   }
}
