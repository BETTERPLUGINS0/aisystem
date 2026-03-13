package github.nighter.smartspawner.spawner.gui.stacker;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerRemoveEvent;
import github.nighter.smartspawner.api.events.SpawnerStackEvent;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.logging.SpawnerEventType;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuUI;
import github.nighter.smartspawner.spawner.item.SpawnerItemFactory;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.utils.SpawnerLocationLockManager;
import github.nighter.smartspawner.spawner.utils.SpawnerTypeChecker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerStackerHandler implements Listener {
   private final SmartSpawner plugin;
   private final MessageService messageService;
   private final SpawnerMenuUI spawnerMenuUI;
   private final LanguageManager languageManager;
   private final SpawnerItemFactory spawnerItemFactory;
   private final SpawnerLocationLockManager locationLockManager;
   private final SpawnerManager spawnerManager;
   private static final Sound STACK_SOUND;
   private static final Sound CLICK_SOUND;
   private static final float SOUND_VOLUME = 1.0F;
   private static final float SOUND_PITCH = 1.0F;
   private static final int[] DECREASE_SLOTS;
   private static final int[] INCREASE_SLOTS;
   private static final int SPAWNER_INFO_SLOT = 13;
   private static final int[] STACK_AMOUNTS;
   private static final int REMOVE_ALL_SLOT = 22;
   private static final int ADD_ALL_SLOT = 4;
   private final Map<UUID, Long> lastClickTime = new ConcurrentHashMap(16, 0.75F, 2);
   private final Map<UUID, Scheduler.Task> pendingUpdates = new ConcurrentHashMap(16, 0.75F, 2);
   private final Map<String, Set<UUID>> activeViewers = new ConcurrentHashMap(16, 0.75F, 2);
   private final Map<UUID, AtomicBoolean> updateLocks = new ConcurrentHashMap(16, 0.75F, 2);
   private final Map<ItemStack, Optional<EntityType>> entityTypeCache = new WeakHashMap();
   private static final long CLICK_COOLDOWN = 200L;
   private static final long UPDATE_DELAY = 2L;

   public SpawnerStackerHandler(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
      this.spawnerItemFactory = plugin.getSpawnerItemFactory();
      this.spawnerMenuUI = plugin.getSpawnerMenuUI();
      this.locationLockManager = plugin.getSpawnerLocationLockManager();
      this.spawnerManager = plugin.getSpawnerManager();
      this.startCleanupTask();
   }

   private void startCleanupTask() {
      Scheduler.runTaskTimer(() -> {
         long now = System.currentTimeMillis();
         this.lastClickTime.entrySet().removeIf((entry) -> {
            return now - (Long)entry.getValue() > 5000L;
         });
         this.updateLocks.entrySet().removeIf((entry) -> {
            return !this.lastClickTime.containsKey(entry.getKey());
         });
         if (this.entityTypeCache.size() > 100) {
            this.entityTypeCache.clear();
         }

      }, 200L, 200L);
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onInventoryClick(InventoryClickEvent event) {
      HumanEntity var3 = event.getWhoClicked();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         InventoryHolder var4 = event.getInventory().getHolder(false);
         if (var4 instanceof SpawnerStackerHolder) {
            SpawnerStackerHolder holder = (SpawnerStackerHolder)var4;
            event.setCancelled(true);
            SpawnerData spawner = holder.getSpawnerData();
            UUID playerId = player.getUniqueId();
            Long lastClick = (Long)this.lastClickTime.get(playerId);
            if (lastClick == null || System.currentTimeMillis() - lastClick >= 200L) {
               ItemStack clickedItem = event.getCurrentItem();
               if (clickedItem != null && clickedItem.hasItemMeta()) {
                  if (clickedItem.getType() == Material.SPAWNER) {
                     if (this.isBedrockPlayer(player)) {
                        if (this.plugin.getSpawnerMenuFormUI() != null) {
                           this.plugin.getSpawnerMenuFormUI().openSpawnerForm(player, spawner);
                        } else {
                           this.spawnerMenuUI.openSpawnerMenu(player, spawner, true);
                        }
                     } else {
                        this.spawnerMenuUI.openSpawnerMenu(player, spawner, true);
                     }

                     player.playSound(player.getLocation(), CLICK_SOUND, 1.0F, 1.0F);
                  } else {
                     int slotIndex = event.getRawSlot();
                     String spawnerId;
                     Set viewers;
                     if (slotIndex == 4) {
                        this.lastClickTime.put(playerId, System.currentTimeMillis());
                        this.handleAddAll(player, spawner);
                        spawnerId = spawner.getSpawnerId();
                        viewers = (Set)this.activeViewers.get(spawnerId);
                        if (viewers != null && !viewers.isEmpty()) {
                           this.scheduleViewersUpdate(spawner);
                        }

                     } else if (slotIndex == 22) {
                        this.lastClickTime.put(playerId, System.currentTimeMillis());
                        this.handleRemoveAll(player, spawner);
                        spawnerId = spawner.getSpawnerId();
                        viewers = (Set)this.activeViewers.get(spawnerId);
                        if (viewers != null && !viewers.isEmpty()) {
                           this.scheduleViewersUpdate(spawner);
                        }

                     } else {
                        int changeAmount = this.determineChangeAmount(slotIndex);
                        if (changeAmount != 0) {
                           this.lastClickTime.put(playerId, System.currentTimeMillis());
                           this.processStackModification(player, spawner, changeAmount);
                           String spawnerId = spawner.getSpawnerId();
                           Set<UUID> viewers = (Set)this.activeViewers.get(spawnerId);
                           if (viewers != null && !viewers.isEmpty()) {
                              this.scheduleViewersUpdate(spawner);
                           }
                        }

                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void onInventoryDrag(InventoryDragEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof SpawnerStackerHolder) {
         SpawnerStackerHolder holder = (SpawnerStackerHolder)var3;
         event.setCancelled(true);
      }
   }

   @EventHandler
   public void onInventoryOpen(InventoryOpenEvent event) {
      HumanEntity var3 = event.getPlayer();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         InventoryHolder var4 = event.getInventory().getHolder(false);
         if (var4 instanceof SpawnerStackerHolder) {
            SpawnerStackerHolder holder = (SpawnerStackerHolder)var4;
            UUID playerId = player.getUniqueId();
            String spawnerId = holder.getSpawnerData().getSpawnerId();
            Set var6 = (Set)this.activeViewers.computeIfAbsent(spawnerId, (k) -> {
               return ConcurrentHashMap.newKeySet();
            });
            var6.add(playerId);
            this.updateLocks.putIfAbsent(playerId, new AtomicBoolean(false));
         }
      }
   }

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent event) {
      HumanEntity var3 = event.getPlayer();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         InventoryHolder var4 = event.getInventory().getHolder(false);
         if (var4 instanceof SpawnerStackerHolder) {
            SpawnerStackerHolder holder = (SpawnerStackerHolder)var4;
            String spawnerId = holder.getSpawnerData().getSpawnerId();
            UUID playerId = player.getUniqueId();
            Scheduler.runTaskLater(() -> {
               Inventory topInventory = player.getOpenInventory().getTopInventory();
               if (!(topInventory.getHolder(false) instanceof SpawnerStackerHolder)) {
                  this.removeViewer(spawnerId, playerId);
               }

            }, 1L);
         }
      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      UUID playerId = event.getPlayer().getUniqueId();
      this.cleanupPlayer(playerId);
   }

   private void cleanupPlayer(UUID playerId) {
      Scheduler.Task task = (Scheduler.Task)this.pendingUpdates.remove(playerId);
      if (task != null && !task.isCancelled()) {
         task.cancel();
      }

      this.lastClickTime.remove(playerId);
      this.updateLocks.remove(playerId);
      Iterator var3 = this.activeViewers.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Set<UUID>> entry = (Entry)var3.next();
         ((Set)entry.getValue()).remove(playerId);
         if (((Set)entry.getValue()).isEmpty()) {
            this.activeViewers.remove(entry.getKey());
         }
      }

   }

   public void cleanupAll() {
      this.pendingUpdates.values().forEach((task) -> {
         if (task != null && !task.isCancelled()) {
            task.cancel();
         }

      });
      this.pendingUpdates.clear();
      this.lastClickTime.clear();
      this.updateLocks.clear();
      this.activeViewers.clear();
      this.entityTypeCache.clear();
   }

   private void removeViewer(String spawnerId, UUID playerId) {
      Set<UUID> viewers = (Set)this.activeViewers.get(spawnerId);
      if (viewers != null) {
         viewers.remove(playerId);
         if (viewers.isEmpty()) {
            this.activeViewers.remove(spawnerId);
         }
      }

      Scheduler.Task task = (Scheduler.Task)this.pendingUpdates.remove(playerId);
      if (task != null && !task.isCancelled()) {
         task.cancel();
      }

   }

   private int determineChangeAmount(int slotIndex) {
      if (slotIndex >= 9 && slotIndex <= 11) {
         return -STACK_AMOUNTS[slotIndex - 9];
      } else {
         return slotIndex >= 15 && slotIndex <= 17 ? STACK_AMOUNTS[17 - slotIndex] : 0;
      }
   }

   private void processStackModification(Player player, SpawnerData spawner, int changeAmount) {
      if (changeAmount < 0) {
         this.handleStackDecrease(player, spawner, Math.abs(changeAmount));
      } else {
         this.handleStackIncrease(player, spawner, changeAmount);
      }

   }

   private void handleStackDecrease(Player player, SpawnerData spawner, int removeAmount) {
      Location location = spawner.getSpawnerLocation();
      if (!this.locationLockManager.tryLock(location)) {
         this.messageService.sendMessage(player, "action_in_progress");
      } else {
         try {
            int currentSize = spawner.getStackSize();
            if (currentSize == 1) {
               this.messageService.sendMessage(player, "spawner_cannot_remove_last");
               return;
            }

            int targetSize = Math.max(1, currentSize - removeAmount);
            int actualChange = currentSize - targetSize;
            if (actualChange > 0) {
               int inventoryCapacity = this.countAvailableSpawnerCapacity(player, spawner);
               if (inventoryCapacity <= 0) {
                  this.messageService.sendMessage(player, "inventory_full");
                  return;
               }

               actualChange = Math.min(actualChange, inventoryCapacity);
               targetSize = currentSize - actualChange;
               if (SpawnerRemoveEvent.getHandlerList().getRegisteredListeners().length != 0) {
                  SpawnerRemoveEvent e = new SpawnerRemoveEvent(player, spawner.getSpawnerLocation(), targetSize, actualChange);
                  Bukkit.getPluginManager().callEvent(e);
                  if (e.isCancelled()) {
                     return;
                  }
               }

               spawner.setStackSize(targetSize);
               this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
               if (spawner.isItemSpawner()) {
                  this.giveItemSpawnersToPlayer(player, actualChange, spawner.getSpawnedItemMaterial());
               } else {
                  this.giveSpawnersToPlayer(player, actualChange, spawner.getEntityType());
               }

               if (this.plugin.getSpawnerActionLogger() != null) {
                  this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_DESTACK_GUI, (builder) -> {
                     builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("amount_removed", actualChange).metadata("old_stack_size", currentSize).metadata("new_stack_size", targetSize);
                  });
               }

               player.playSound(player.getLocation(), STACK_SOUND, 1.0F, 1.0F);
               return;
            }

            Map<String, String> placeholders = new HashMap(2);
            placeholders.put("amount", String.valueOf(currentSize));
            this.messageService.sendMessage((Player)player, "spawner_stacker_minimum_reached", placeholders);
         } finally {
            this.locationLockManager.unlock(location);
         }

      }
   }

   private void handleStackIncrease(Player player, SpawnerData spawner, int changeAmount) {
      int currentSize = spawner.getStackSize();
      int maxStackSize = spawner.getMaxStackSize();
      int spaceLeft = maxStackSize - currentSize;
      if (spaceLeft <= 0) {
         Map<String, String> placeholders = new HashMap(2);
         placeholders.put("max", String.valueOf(maxStackSize));
         this.messageService.sendMessage((Player)player, "spawner_stack_full", placeholders);
      } else {
         int actualChange = Math.min(changeAmount, spaceLeft);
         InventoryScanResult scanResult;
         if (spawner.isItemSpawner()) {
            Material requiredItemMaterial = spawner.getSpawnedItemMaterial();
            scanResult = this.scanPlayerInventoryForItemSpawner(player, requiredItemMaterial);
         } else {
            EntityType requiredType = spawner.getEntityType();
            scanResult = this.scanPlayerInventory(player, requiredType);
         }

         if (scanResult.availableSpawners == 0 && scanResult.hasDifferentType) {
            this.messageService.sendMessage(player, "spawner_different");
         } else {
            HashMap placeholders;
            if (scanResult.availableSpawners < actualChange) {
               placeholders = new HashMap(4);
               placeholders.put("amountChange", String.valueOf(actualChange));
               placeholders.put("amountAvailable", String.valueOf(scanResult.availableSpawners));
               this.messageService.sendMessage((Player)player, "spawner_insufficient_quantity", placeholders);
            } else {
               if (SpawnerStackEvent.getHandlerList().getRegisteredListeners().length != 0) {
                  SpawnerStackEvent e = new SpawnerStackEvent(player, spawner.getSpawnerLocation(), spawner.getStackSize(), spawner.getStackSize() + actualChange, SpawnerStackEvent.StackSource.GUI);
                  Bukkit.getPluginManager().callEvent(e);
                  if (e.isCancelled()) {
                     return;
                  }
               }

               if (spawner.isItemSpawner()) {
                  this.removeValidItemSpawnersFromInventory(player, spawner.getSpawnedItemMaterial(), actualChange, scanResult.spawnerSlots);
               } else {
                  this.removeValidSpawnersFromInventory(player, spawner.getEntityType(), actualChange, scanResult.spawnerSlots);
               }

               spawner.setStackSize(currentSize + actualChange);
               this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
               if (actualChange < changeAmount) {
                  placeholders = new HashMap(2);
                  placeholders.put("amount", String.valueOf(actualChange));
                  this.messageService.sendMessage((Player)player, "spawner_stacker_minimum_reached", placeholders);
               }

               player.playSound(player.getLocation(), STACK_SOUND, 1.0F, 1.0F);
            }
         }
      }
   }

   private void handleAddAll(Player player, SpawnerData spawner) {
      int currentSize = spawner.getStackSize();
      int maxStackSize = spawner.getMaxStackSize();
      int spaceLeft = maxStackSize - currentSize;
      if (spaceLeft <= 0) {
         Map<String, String> placeholders = new HashMap(2);
         placeholders.put("max", String.valueOf(maxStackSize));
         this.messageService.sendMessage((Player)player, "spawner_stack_full", placeholders);
      } else {
         InventoryScanResult scanResult;
         if (spawner.isItemSpawner()) {
            scanResult = this.scanPlayerInventoryForItemSpawner(player, spawner.getSpawnedItemMaterial());
         } else {
            scanResult = this.scanPlayerInventory(player, spawner.getEntityType());
         }

         if (scanResult.availableSpawners == 0 && scanResult.hasDifferentType) {
            this.messageService.sendMessage(player, "spawner_different");
         } else {
            int actualChange = Math.min(spaceLeft, scanResult.availableSpawners);
            if (SpawnerStackEvent.getHandlerList().getRegisteredListeners().length != 0) {
               SpawnerStackEvent e = new SpawnerStackEvent(player, spawner.getSpawnerLocation(), spawner.getStackSize(), spawner.getStackSize() + actualChange, SpawnerStackEvent.StackSource.GUI);
               Bukkit.getPluginManager().callEvent(e);
               if (e.isCancelled()) {
                  return;
               }
            }

            if (spawner.isItemSpawner()) {
               this.removeValidItemSpawnersFromInventory(player, spawner.getSpawnedItemMaterial(), actualChange, scanResult.spawnerSlots);
            } else {
               this.removeValidSpawnersFromInventory(player, spawner.getEntityType(), actualChange, scanResult.spawnerSlots);
            }

            spawner.setStackSize(currentSize + actualChange);
            this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
            player.playSound(player.getLocation(), STACK_SOUND, 1.0F, 1.0F);
         }
      }
   }

   private void handleRemoveAll(Player player, SpawnerData spawner) {
      Location location = spawner.getSpawnerLocation();
      if (!this.locationLockManager.tryLock(location)) {
         this.messageService.sendMessage(player, "action_in_progress");
      } else {
         try {
            int currentSize = spawner.getStackSize();
            if (currentSize == 1) {
               this.messageService.sendMessage(player, "spawner_cannot_remove_last");
               return;
            }

            int actualChange = currentSize - 1;
            int inventoryCapacity = this.countAvailableSpawnerCapacity(player, spawner);
            if (inventoryCapacity > 0) {
               actualChange = Math.min(actualChange, inventoryCapacity);
               int newStackSize = currentSize - actualChange;
               if (SpawnerRemoveEvent.getHandlerList().getRegisteredListeners().length != 0) {
                  SpawnerRemoveEvent e = new SpawnerRemoveEvent(player, spawner.getSpawnerLocation(), newStackSize, actualChange);
                  Bukkit.getPluginManager().callEvent(e);
                  if (e.isCancelled()) {
                     return;
                  }
               }

               spawner.setStackSize(newStackSize);
               this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
               if (spawner.isItemSpawner()) {
                  this.giveItemSpawnersToPlayer(player, actualChange, spawner.getSpawnedItemMaterial());
               } else {
                  this.giveSpawnersToPlayer(player, actualChange, spawner.getEntityType());
               }

               if (this.plugin.getSpawnerActionLogger() != null) {
                  this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_DESTACK_GUI, (builder) -> {
                     builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("amount_removed", actualChange).metadata("old_stack_size", currentSize).metadata("new_stack_size", newStackSize);
                  });
               }

               player.playSound(player.getLocation(), STACK_SOUND, 1.0F, 1.0F);
               return;
            }

            this.messageService.sendMessage(player, "inventory_full");
         } finally {
            this.locationLockManager.unlock(location);
         }

      }
   }

   private int countAvailableSpawnerCapacity(Player player, SpawnerData spawner) {
      int MAX_STACK_SIZE = true;
      int capacity = 0;
      ItemStack[] contents = player.getInventory().getContents();

      for(int i = 0; i < 36; ++i) {
         ItemStack item = contents[i];
         if (item != null && item.getType() != Material.AIR) {
            if (item.getType() == Material.SPAWNER && !SpawnerTypeChecker.isVanillaSpawner(item)) {
               Optional<EntityType> itemEntityType = this.getSpawnerEntityTypeCached(item);
               boolean matches;
               if (spawner.isItemSpawner()) {
                  matches = false;
               } else {
                  matches = itemEntityType.isPresent() && itemEntityType.get() == spawner.getEntityType();
               }

               if (matches && item.getAmount() < 64) {
                  capacity += 64 - item.getAmount();
               }
            }
         } else {
            capacity += 64;
         }
      }

      return capacity;
   }

   private void scheduleViewersUpdate(SpawnerData spawner) {
      String spawnerId = spawner.getSpawnerId();
      Set<UUID> viewers = (Set)this.activeViewers.get(spawnerId);
      if (viewers != null && !viewers.isEmpty()) {
         Scheduler.Task task = Scheduler.runTaskLater(() -> {
            this.updateAllViewers(spawner, viewers);
         }, 2L);
         Iterator var5 = viewers.iterator();

         while(var5.hasNext()) {
            UUID viewerId = (UUID)var5.next();
            this.pendingUpdates.put(viewerId, task);
         }

      }
   }

   private void updateAllViewers(SpawnerData spawner, Set<UUID> viewers) {
      Iterator var3 = viewers.iterator();

      while(var3.hasNext()) {
         UUID viewerId = (UUID)var3.next();
         Player viewer = this.plugin.getServer().getPlayer(viewerId);
         if (viewer != null && viewer.isOnline()) {
            AtomicBoolean lock = (AtomicBoolean)this.updateLocks.get(viewerId);
            if (lock != null && lock.compareAndSet(false, true)) {
               try {
                  this.updateGui(viewer, spawner);
               } finally {
                  lock.set(false);
               }
            }
         }
      }

   }

   private void updateGui(Player player, SpawnerData spawner) {
      Inventory inv = player.getOpenInventory().getTopInventory();
      if (inv.getHolder(false) instanceof SpawnerStackerHolder) {
         Map<String, String> basePlaceholders = this.createBasePlaceholders(spawner);
         this.updateInfoItem(inv, basePlaceholders);

         int i;
         for(i = 0; i < DECREASE_SLOTS.length; ++i) {
            this.updateActionButton(inv, "remove", STACK_AMOUNTS[i], DECREASE_SLOTS[i], basePlaceholders);
         }

         for(i = 0; i < INCREASE_SLOTS.length; ++i) {
            this.updateActionButton(inv, "add", STACK_AMOUNTS[i], INCREASE_SLOTS[i], basePlaceholders);
         }

         this.updateAllActionButton(inv, "remove_all", 22, basePlaceholders);
         this.updateAllActionButton(inv, "add_all", 4, basePlaceholders);
         player.updateInventory();
      }
   }

   private Map<String, String> createBasePlaceholders(SpawnerData spawner) {
      Map<String, String> placeholders = new HashMap(8);
      placeholders.put("stack_size", String.valueOf(spawner.getStackSize()));
      placeholders.put("max_stack_size", String.valueOf(spawner.getMaxStackSize()));
      String entityName = this.languageManager.getFormattedMobName(spawner.getEntityType());
      placeholders.put("entity", entityName);
      placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps(entityName));
      return placeholders;
   }

   private void updateInfoItem(Inventory inventory, Map<String, String> basePlaceholders) {
      ItemStack infoItem = inventory.getItem(13);
      if (infoItem != null && infoItem.hasItemMeta()) {
         ItemMeta meta = infoItem.getItemMeta();
         Map<String, String> placeholders = new HashMap(basePlaceholders);
         String name = this.languageManager.getGuiItemName("button_spawner.name", placeholders);
         String[] lore = this.languageManager.getGuiItemLore("button_spawner.lore", placeholders);
         meta.setDisplayName(name);
         meta.setLore(Arrays.asList(lore));
         infoItem.setItemMeta(meta);
      }
   }

   private void updateActionButton(Inventory inventory, String action, int amount, int slot, Map<String, String> basePlaceholders) {
      ItemStack button = inventory.getItem(slot);
      if (button != null && button.hasItemMeta()) {
         ItemMeta meta = button.getItemMeta();
         Map<String, String> placeholders = new HashMap(basePlaceholders);
         placeholders.put("amount", String.valueOf(amount));
         placeholders.put("plural", amount > 1 ? "s" : "");
         String name = this.languageManager.getGuiItemName("button_" + action + ".name", placeholders);
         String[] lore = this.languageManager.getGuiItemLore("button_" + action + ".lore", placeholders);
         meta.setDisplayName(name);
         meta.setLore(Arrays.asList(lore));
         button.setItemMeta(meta);
      }
   }

   private void updateAllActionButton(Inventory inventory, String action, int slot, Map<String, String> basePlaceholders) {
      ItemStack button = inventory.getItem(slot);
      if (button != null && button.hasItemMeta()) {
         ItemMeta meta = button.getItemMeta();
         Map<String, String> placeholders = new HashMap(basePlaceholders);
         String name = this.languageManager.getGuiItemName("button_" + action + ".name", placeholders);
         String[] lore = this.languageManager.getGuiItemLore("button_" + action + ".lore", placeholders);
         meta.setDisplayName(name);
         meta.setLore(Arrays.asList(lore));
         button.setItemMeta(meta);
      }
   }

   private InventoryScanResult scanPlayerInventory(Player player, EntityType requiredType) {
      int count = 0;
      boolean hasDifferentType = false;
      List<SpawnerSlot> spawnerSlots = new ArrayList();
      ItemStack[] contents = player.getInventory().getContents();

      for(int i = 0; i < contents.length; ++i) {
         ItemStack item = contents[i];
         if (item != null && item.getType() == Material.SPAWNER && !SpawnerTypeChecker.isVanillaSpawner(item)) {
            Optional<EntityType> itemType = this.getSpawnerEntityTypeCached(item);
            if (itemType.isPresent()) {
               if (itemType.get() == requiredType) {
                  count += item.getAmount();
                  spawnerSlots.add(new SpawnerSlot(i, item.getAmount()));
               } else {
                  hasDifferentType = true;
               }
            }
         }
      }

      return new InventoryScanResult(count, hasDifferentType, spawnerSlots);
   }

   private InventoryScanResult scanPlayerInventoryForItemSpawner(Player player, Material requiredItemMaterial) {
      int count = 0;
      boolean hasDifferentType = false;
      List<SpawnerSlot> spawnerSlots = new ArrayList();
      ItemStack[] contents = player.getInventory().getContents();

      for(int i = 0; i < contents.length; ++i) {
         ItemStack item = contents[i];
         if (item != null && item.getType() == Material.SPAWNER && !SpawnerTypeChecker.isVanillaSpawner(item)) {
            if (SpawnerTypeChecker.isItemSpawner(item)) {
               Material itemMaterial = SpawnerTypeChecker.getItemSpawnerMaterial(item);
               if (itemMaterial == requiredItemMaterial) {
                  count += item.getAmount();
                  spawnerSlots.add(new SpawnerSlot(i, item.getAmount()));
               } else {
                  hasDifferentType = true;
               }
            } else {
               hasDifferentType = true;
            }
         }
      }

      return new InventoryScanResult(count, hasDifferentType, spawnerSlots);
   }

   private Optional<EntityType> getSpawnerEntityTypeCached(ItemStack item) {
      if (item != null && item.getType() == Material.SPAWNER) {
         Optional<EntityType> cachedType = (Optional)this.entityTypeCache.get(item);
         if (cachedType != null) {
            return cachedType;
         } else {
            Optional<EntityType> result = this.getEntityTypeFromItem(item);
            this.entityTypeCache.put(item, result);
            return result;
         }
      } else {
         return Optional.empty();
      }
   }

   public Optional<EntityType> getEntityTypeFromItem(ItemStack item) {
      ItemMeta meta = item.getItemMeta();
      if (meta == null) {
         return Optional.empty();
      } else {
         if (meta instanceof BlockStateMeta) {
            BlockStateMeta blockMeta = (BlockStateMeta)meta;
            if (blockMeta.hasBlockState()) {
               BlockState var5 = blockMeta.getBlockState();
               if (var5 instanceof CreatureSpawner) {
                  CreatureSpawner handSpawner = (CreatureSpawner)var5;
                  EntityType entityType = handSpawner.getSpawnedType();
                  if (entityType != null) {
                     return Optional.of(entityType);
                  }
               }
            }
         }

         return Optional.empty();
      }
   }

   private void removeValidSpawnersFromInventory(Player player, EntityType requiredType, int amountToRemove, List<SpawnerSlot> spawnerSlots) {
      int remainingToRemove = amountToRemove;
      Iterator var6 = spawnerSlots.iterator();

      while(var6.hasNext()) {
         SpawnerSlot slot = (SpawnerSlot)var6.next();
         if (remainingToRemove <= 0) {
            break;
         }

         ItemStack item = player.getInventory().getItem(slot.slotIndex);
         if (item != null && item.getType() == Material.SPAWNER) {
            Optional<EntityType> spawnerType = this.getSpawnerEntityTypeCached(item);
            if (spawnerType.isPresent() && spawnerType.get() == requiredType) {
               int itemAmount = item.getAmount();
               if (itemAmount <= remainingToRemove) {
                  player.getInventory().setItem(slot.slotIndex, (ItemStack)null);
                  remainingToRemove -= itemAmount;
               } else {
                  item.setAmount(itemAmount - remainingToRemove);
                  remainingToRemove = 0;
               }
            }
         }
      }

      player.updateInventory();
   }

   private void removeValidItemSpawnersFromInventory(Player player, Material requiredItemMaterial, int amountToRemove, List<SpawnerSlot> spawnerSlots) {
      int remainingToRemove = amountToRemove;
      Iterator var6 = spawnerSlots.iterator();

      while(var6.hasNext()) {
         SpawnerSlot slot = (SpawnerSlot)var6.next();
         if (remainingToRemove <= 0) {
            break;
         }

         ItemStack item = player.getInventory().getItem(slot.slotIndex);
         if (item != null && item.getType() == Material.SPAWNER && SpawnerTypeChecker.isItemSpawner(item)) {
            Material itemMaterial = SpawnerTypeChecker.getItemSpawnerMaterial(item);
            if (itemMaterial == requiredItemMaterial) {
               int itemAmount = item.getAmount();
               if (itemAmount <= remainingToRemove) {
                  player.getInventory().setItem(slot.slotIndex, (ItemStack)null);
                  remainingToRemove -= itemAmount;
               } else {
                  item.setAmount(itemAmount - remainingToRemove);
                  remainingToRemove = 0;
               }
            }
         }
      }

      player.updateInventory();
   }

   public void giveSpawnersToPlayer(Player player, int amount, EntityType entityType) {
      int MAX_STACK_SIZE = true;
      int remainingAmount = amount;
      ItemStack[] contents = player.getInventory().getContents();

      for(int i = 0; i < contents.length && remainingAmount > 0; ++i) {
         ItemStack item = contents[i];
         if (item != null && item.getType() == Material.SPAWNER && !SpawnerTypeChecker.isVanillaSpawner(item)) {
            Optional<EntityType> itemEntityType = this.getSpawnerEntityTypeCached(item);
            if (!itemEntityType.isEmpty() && itemEntityType.get() == entityType) {
               int currentAmount = item.getAmount();
               if (currentAmount < 64) {
                  int canAdd = Math.min(64 - currentAmount, remainingAmount);
                  item.setAmount(currentAmount + canAdd);
                  remainingAmount -= canAdd;
               }
            }
         }
      }

      if (remainingAmount > 0) {
         ArrayList newStacks;
         int stackSize;
         for(newStacks = new ArrayList(); remainingAmount > 0; remainingAmount -= stackSize) {
            stackSize = Math.min(64, remainingAmount);
            ItemStack spawnerItem = this.spawnerItemFactory.createSmartSpawnerItem(entityType, stackSize);
            newStacks.add(spawnerItem);
         }

         boolean allFit = true;
         Iterator var16 = newStacks.iterator();

         while(var16.hasNext()) {
            ItemStack stack = (ItemStack)var16.next();
            boolean addedSuccessfully = this.addItemAvoidingVanillaSpawners(player, stack);
            if (!addedSuccessfully) {
               player.getWorld().dropItemNaturally(player.getLocation(), stack);
               allFit = false;
            }
         }

         if (!allFit) {
            this.messageService.sendMessage(player, "inventory_full_items_dropped");
         }
      }

      player.updateInventory();
   }

   public void giveItemSpawnersToPlayer(Player player, int amount, Material itemMaterial) {
      int MAX_STACK_SIZE = true;
      int remainingAmount = amount;
      ItemStack[] contents = player.getInventory().getContents();

      for(int i = 0; i < contents.length && remainingAmount > 0; ++i) {
         ItemStack item = contents[i];
         if (item != null && item.getType() == Material.SPAWNER && !SpawnerTypeChecker.isVanillaSpawner(item) && SpawnerTypeChecker.isItemSpawner(item)) {
            Material itemSpawnerMaterial = SpawnerTypeChecker.getItemSpawnerMaterial(item);
            if (itemSpawnerMaterial == itemMaterial) {
               int currentAmount = item.getAmount();
               if (currentAmount < 64) {
                  int canAdd = Math.min(64 - currentAmount, remainingAmount);
                  item.setAmount(currentAmount + canAdd);
                  remainingAmount -= canAdd;
               }
            }
         }
      }

      if (remainingAmount > 0) {
         ArrayList newStacks;
         int stackSize;
         for(newStacks = new ArrayList(); remainingAmount > 0; remainingAmount -= stackSize) {
            stackSize = Math.min(64, remainingAmount);
            ItemStack spawnerItem = this.spawnerItemFactory.createItemSpawnerItem(itemMaterial, stackSize);
            newStacks.add(spawnerItem);
         }

         boolean allFit = true;
         Iterator var16 = newStacks.iterator();

         while(var16.hasNext()) {
            ItemStack stack = (ItemStack)var16.next();
            boolean addedSuccessfully = this.addItemAvoidingVanillaSpawners(player, stack);
            if (!addedSuccessfully) {
               player.getWorld().dropItemNaturally(player.getLocation(), stack);
               allFit = false;
            }
         }

         if (!allFit) {
            this.messageService.sendMessage(player, "inventory_full_items_dropped");
         }
      }

      player.updateInventory();
   }

   private boolean addItemAvoidingVanillaSpawners(Player player, ItemStack item) {
      if (item.getType() != Material.SPAWNER) {
         Map<Integer, ItemStack> failed = player.getInventory().addItem(new ItemStack[]{item});
         return failed.isEmpty();
      } else {
         Inventory inv = player.getInventory();

         for(int i = 0; i < 36; ++i) {
            ItemStack currentItem = inv.getItem(i);
            if (currentItem == null) {
               inv.setItem(i, item.clone());
               return true;
            }
         }

         return false;
      }
   }

   public void closeAllViewersInventory(String spawnerId) {
      Set<UUID> viewers = (Set)this.activeViewers.get(spawnerId);
      if (viewers != null && !viewers.isEmpty()) {
         Set<UUID> viewersCopy = new HashSet(viewers);
         Iterator var4 = viewersCopy.iterator();

         while(var4.hasNext()) {
            UUID viewerId = (UUID)var4.next();
            Player viewer = this.plugin.getServer().getPlayer(viewerId);
            if (viewer != null && viewer.isOnline()) {
               viewer.closeInventory();
            }
         }

      }
   }

   private boolean isBedrockPlayer(Player player) {
      return this.plugin.getIntegrationManager() != null && this.plugin.getIntegrationManager().getFloodgateHook() != null ? this.plugin.getIntegrationManager().getFloodgateHook().isBedrockPlayer(player) : false;
   }

   static {
      STACK_SOUND = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
      CLICK_SOUND = Sound.UI_BUTTON_CLICK;
      DECREASE_SLOTS = new int[]{9, 10, 11};
      INCREASE_SLOTS = new int[]{17, 16, 15};
      STACK_AMOUNTS = new int[]{64, 10, 1};
   }
}
