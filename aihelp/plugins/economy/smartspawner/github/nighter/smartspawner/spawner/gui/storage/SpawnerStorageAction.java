package github.nighter.smartspawner.spawner.gui.storage;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.logging.SpawnerEventType;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayoutConfig;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuUI;
import github.nighter.smartspawner.spawner.gui.sell.SpawnerSellConfirmUI;
import github.nighter.smartspawner.spawner.gui.storage.filter.FilterConfigUI;
import github.nighter.smartspawner.spawner.gui.synchronization.SpawnerGuiViewManager;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class SpawnerStorageAction implements Listener {
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final SpawnerMenuUI spawnerMenuUI;
   private final SpawnerGuiViewManager spawnerGuiViewManager;
   private final MessageService messageService;
   private final FilterConfigUI filterConfigUI;
   private final SpawnerManager spawnerManager;
   private static final int INVENTORY_SIZE = 54;
   private static final int STORAGE_SLOTS = 45;
   private final Map<UUID, Long> lastItemClickTime = new ConcurrentHashMap();
   private final Map<UUID, Long> lastControlClickTime = new ConcurrentHashMap();
   private static final long ITEM_CLICK_DELAY_MS = 150L;
   private static final long CONTROL_CLICK_DELAY_MS = 300L;
   private final Random random = new Random();
   private GuiLayout layout;

   public SpawnerStorageAction(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.spawnerMenuUI = plugin.getSpawnerMenuUI();
      this.spawnerGuiViewManager = plugin.getSpawnerGuiViewManager();
      this.messageService = plugin.getMessageService();
      this.filterConfigUI = plugin.getFilterConfigUI();
      this.spawnerManager = plugin.getSpawnerManager();
      this.loadConfig();
   }

   public void loadConfig() {
      GuiLayoutConfig guiLayoutConfig = this.plugin.getGuiLayoutConfig();
      this.layout = guiLayoutConfig.getCurrentLayout();
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onInventoryClick(InventoryClickEvent event) {
      HumanEntity var4 = event.getWhoClicked();
      if (var4 instanceof Player) {
         Player player = (Player)var4;
         InventoryHolder var6 = event.getInventory().getHolder(false);
         if (var6 instanceof StoragePageHolder) {
            StoragePageHolder holder = (StoragePageHolder)var6;
            SpawnerData spawner = holder.getSpawnerData();
            int slot = event.getRawSlot();
            event.setCancelled(true);
            if (slot >= 0 && slot < 54) {
               if (this.isItemSlot(slot)) {
                  this.handleItemSlotClick(player, slot, holder, spawner, event);
                  return;
               }

               if (this.isControlSlot(slot)) {
                  this.handleControlSlotClick(player, slot, holder, spawner, event.getInventory(), event.getClick(), this.layout);
               }

               return;
            }

            return;
         }
      }

   }

   private void handleControlSlotClick(Player player, int slot, StoragePageHolder holder, SpawnerData spawner, Inventory inventory, ClickType clickType, GuiLayout layout) {
      Optional<GuiButton> buttonOpt = layout.getButtonAtSlot(slot);
      if (!buttonOpt.isEmpty()) {
         GuiButton button = (GuiButton)buttonOpt.get();
         String clickTypeString = this.getClickTypeString(clickType);
         String action = button.getActionWithFallback(clickTypeString);
         if (action != null && !action.isEmpty()) {
            byte var13 = -1;
            switch(action.hashCode()) {
            case -1486471320:
               if (action.equals("return_main")) {
                  var13 = 8;
               }
               break;
            case -644398615:
               if (action.equals("take_all")) {
                  var13 = 3;
               }
               break;
            case -513611297:
               if (action.equals("drop_page")) {
                  var13 = 5;
               }
               break;
            case -427500787:
               if (action.equals("open_filter")) {
                  var13 = 1;
               }
               break;
            case -379776545:
               if (action.equals("sort_items")) {
                  var13 = 0;
               }
               break;
            case -266558761:
               if (action.equals("previous_page")) {
                  var13 = 2;
               }
               break;
            case 672232392:
               if (action.equals("sell_and_exp")) {
                  var13 = 7;
               }
               break;
            case 1197899572:
               if (action.equals("sell_all")) {
                  var13 = 6;
               }
               break;
            case 1217097819:
               if (action.equals("next_page")) {
                  var13 = 4;
               }
            }

            switch(var13) {
            case 0:
               this.handleSortItemsClick(player, spawner, inventory);
               break;
            case 1:
               this.openFilterConfig(player, spawner);
               break;
            case 2:
               if (holder.getCurrentPage() > 1) {
                  this.updatePageContent(player, spawner, holder.getCurrentPage() - 1, inventory, true);
               }
               break;
            case 3:
               this.handleTakeAllItems(player, inventory);
               break;
            case 4:
               if (holder.getCurrentPage() < holder.getTotalPages()) {
                  this.updatePageContent(player, spawner, holder.getCurrentPage() + 1, inventory, true);
               }
               break;
            case 5:
               this.handleDropPageItems(player, spawner, inventory);
               break;
            case 6:
               this.handleSellAction(player, spawner, false);
               break;
            case 7:
               this.handleSellAction(player, spawner, true);
               break;
            case 8:
               this.handleReturnToMainMenu(player, spawner);
               break;
            default:
               this.plugin.getLogger().warning("Unknown storage action: " + action);
            }

         }
      }
   }

   private String getClickTypeString(ClickType clickType) {
      String var10000;
      switch(clickType) {
      case LEFT:
         var10000 = "left_click";
         break;
      case RIGHT:
         var10000 = "right_click";
         break;
      case SHIFT_LEFT:
         var10000 = "shift_left_click";
         break;
      case SHIFT_RIGHT:
         var10000 = "shift_right_click";
         break;
      default:
         var10000 = "left_click";
      }

      return var10000;
   }

   private void handleSellAction(Player player, SpawnerData spawner, boolean collectExp) {
      if (this.plugin.hasSellIntegration()) {
         if (!player.hasPermission("smartspawner.sellall")) {
            this.messageService.sendMessage(player, "no_permission");
         } else if (!this.isControlClickTooFrequent(player)) {
            if (spawner.getVirtualInventory().getUsedSlots() == 0) {
               if (collectExp) {
                  this.plugin.getSpawnerMenuAction().handleExpBottleClick(player, spawner, true);
               } else {
                  this.messageService.sendMessage(player, "no_items");
               }

            } else {
               player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
               this.plugin.getSpawnerSellConfirmUI().openSellConfirmGui(player, spawner, SpawnerSellConfirmUI.PreviousGui.STORAGE, collectExp);
            }
         }
      }
   }

   private void handleReturnToMainMenu(Player player, SpawnerData spawner) {
      player.closeInventory();
      this.spawnerMenuUI.openSpawnerMenu(player, spawner, false);
   }

   private boolean isControlSlot(int slot) {
      return this.layout != null && this.layout.isSlotUsed(slot);
   }

   private boolean isItemSlot(int slot) {
      return slot >= 0 && slot < 45 && !this.isControlSlot(slot);
   }

   private void handleItemSlotClick(Player player, int slot, StoragePageHolder holder, SpawnerData spawner, InventoryClickEvent event) {
      if (!this.isItemClickTooFrequent(player)) {
         Inventory inventory = event.getInventory();
         ItemStack clickedItem = inventory.getItem(slot);
         if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            ClickType clickType = event.getClick();
            int amountToTake;
            switch(clickType) {
            case LEFT:
               amountToTake = 1;
               break;
            case RIGHT:
               amountToTake = (int)Math.ceil((double)clickedItem.getAmount() / 2.0D);
               break;
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
               amountToTake = clickedItem.getAmount();
               break;
            default:
               return;
            }

            this.transferToPlayerInventory(player, clickedItem, amountToTake, inventory, spawner, holder);
         }
      }
   }

   private void transferToPlayerInventory(Player player, ItemStack clickedItem, int amountToTake, Inventory storageInv, SpawnerData spawner, StoragePageHolder holder) {
      PlayerInventory playerInv = player.getInventory();
      ItemStack toTransfer = clickedItem.clone();
      toTransfer.setAmount(amountToTake);
      int amountMoved = 0;
      int remaining = amountToTake;

      int i;
      ItemStack slot;
      int stackSize;
      for(i = 0; i < 36 && remaining > 0; ++i) {
         slot = playerInv.getItem(i);
         if (slot != null && slot.getType() != Material.AIR && slot.isSimilar(toTransfer)) {
            stackSize = slot.getMaxStackSize() - slot.getAmount();
            if (stackSize > 0) {
               int add = Math.min(stackSize, remaining);
               slot.setAmount(slot.getAmount() + add);
               amountMoved += add;
               remaining -= add;
            }
         }
      }

      for(i = 0; i < 36 && remaining > 0; ++i) {
         slot = playerInv.getItem(i);
         if (slot == null || slot.getType() == Material.AIR) {
            stackSize = Math.min(remaining, toTransfer.getMaxStackSize());
            ItemStack newStack = toTransfer.clone();
            newStack.setAmount(stackSize);
            playerInv.setItem(i, newStack);
            amountMoved += stackSize;
            remaining -= stackSize;
         }
      }

      if (amountMoved > 0) {
         ItemStack removed = toTransfer.clone();
         removed.setAmount(amountMoved);
         if (spawner.removeItemsAndUpdateSellValue(List.of(removed))) {
            this.updatePageAfterRemoval(player, storageInv, spawner, holder);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.0F);
            if (remaining > 0) {
               this.messageService.sendMessage(player, "inventory_full");
            }
         }
      } else {
         this.messageService.sendMessage(player, "inventory_full");
      }

   }

   private void updatePageAfterRemoval(Player player, Inventory inventory, SpawnerData spawner, StoragePageHolder holder) {
      int newTotalPages = this.calculateTotalPages(spawner);
      int currentPage = holder.getCurrentPage();
      int adjustedPage = Math.max(1, Math.min(currentPage, newTotalPages));
      holder.setTotalPages(newTotalPages);
      if (adjustedPage != currentPage) {
         holder.setCurrentPage(adjustedPage);
      }

      holder.updateOldUsedSlots();
      SpawnerStorageUI spawnerStorageUI = this.plugin.getSpawnerStorageUI();
      spawnerStorageUI.updateDisplay(inventory, spawner, adjustedPage, newTotalPages);
      if (newTotalPages != currentPage || adjustedPage != currentPage) {
         this.updateInventoryTitle(player, spawner, adjustedPage, newTotalPages);
      }

      spawner.updateHologramData();
      this.spawnerGuiViewManager.updateSpawnerMenuViewers(spawner);
      if (spawner.getMaxSpawnerLootSlots() > holder.getOldUsedSlots() && spawner.getIsAtCapacity()) {
         spawner.setIsAtCapacity(false);
      }

      if (!spawner.isInteracted()) {
         spawner.markInteracted();
      }

   }

   private void handleDropPageItems(Player player, SpawnerData spawner, Inventory inventory) {
      if (!this.isControlClickTooFrequent(player)) {
         StoragePageHolder holder = (StoragePageHolder)inventory.getHolder(false);
         if (holder != null) {
            List<ItemStack> pageItems = new ArrayList();
            int itemsFoundCount = 0;

            for(int i = 0; i < 45; ++i) {
               ItemStack item = inventory.getItem(i);
               if (item != null && item.getType() != Material.AIR) {
                  pageItems.add(item.clone());
                  itemsFoundCount += item.getAmount();
                  inventory.setItem(i, (ItemStack)null);
               }
            }

            if (pageItems.isEmpty()) {
               this.messageService.sendMessage(player, "no_items_to_drop");
            } else {
               spawner.removeItemsAndUpdateSellValue(pageItems);
               this.dropItemsInDirection(player, pageItems);
               int newTotalPages = this.calculateTotalPages(spawner);
               if (holder.getCurrentPage() > newTotalPages) {
                  holder.setCurrentPage(Math.max(1, newTotalPages));
               }

               holder.setTotalPages(newTotalPages);
               holder.updateOldUsedSlots();
               spawner.updateHologramData();
               this.spawnerGuiViewManager.updateSpawnerMenuViewers(spawner);
               if (spawner.getMaxSpawnerLootSlots() > holder.getOldUsedSlots() && spawner.getIsAtCapacity()) {
                  spawner.setIsAtCapacity(false);
               }

               if (!spawner.isInteracted()) {
                  spawner.markInteracted();
               }

               if (this.plugin.getSpawnerActionLogger() != null) {
                  this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_DROP_PAGE_ITEMS, (builder) -> {
                     builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("items_dropped", itemsFoundCount).metadata("page_number", holder.getCurrentPage());
                  });
               }

               this.updatePageContent(player, spawner, holder.getCurrentPage(), inventory, false);
               player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.8F, 0.8F);
            }
         }
      }
   }

   private void dropItemsInDirection(Player player, List<ItemStack> items) {
      if (!items.isEmpty()) {
         Location playerLoc = player.getLocation();
         World world = player.getWorld();
         UUID playerUUID = player.getUniqueId();
         double yaw = Math.toRadians((double)playerLoc.getYaw());
         double pitch = Math.toRadians((double)playerLoc.getPitch());
         double sinYaw = -Math.sin(yaw);
         double cosYaw = Math.cos(yaw);
         double cosPitch = Math.cos(pitch);
         double sinPitch = -Math.sin(pitch);
         Location dropLocation = playerLoc.clone();
         dropLocation.add(sinYaw * 0.3D, 1.2D, cosYaw * 0.3D);
         Iterator var19 = items.iterator();

         while(var19.hasNext()) {
            ItemStack item = (ItemStack)var19.next();
            Item droppedItem = world.dropItem(dropLocation, item, (drop) -> {
               drop.setThrower(playerUUID);
               drop.setPickupDelay(40);
            });
            Vector velocity = new Vector(sinYaw * cosPitch * 0.3D + (this.random.nextDouble() - 0.5D) * 0.1D, sinPitch * 0.3D + 0.1D + (this.random.nextDouble() - 0.5D) * 0.1D, cosYaw * cosPitch * 0.3D + (this.random.nextDouble() - 0.5D) * 0.1D);
            droppedItem.setVelocity(velocity);
         }

      }
   }

   private void openFilterConfig(Player player, SpawnerData spawner) {
      if (!this.isControlClickTooFrequent(player)) {
         this.filterConfigUI.openFilterConfigGUI(player, spawner);
      }
   }

   private void updatePageContent(Player player, SpawnerData spawner, int newPage, Inventory inventory, boolean uiClickSound) {
      SpawnerStorageUI spawnerStorageUI = this.plugin.getSpawnerStorageUI();
      StoragePageHolder holder = (StoragePageHolder)inventory.getHolder(false);
      int totalPages = this.calculateTotalPages(spawner);

      assert holder != null;

      holder.setTotalPages(totalPages);
      holder.setCurrentPage(newPage);
      holder.updateOldUsedSlots();
      spawnerStorageUI.updateDisplay(inventory, spawner, newPage, totalPages);
      this.updateInventoryTitle(player, spawner, newPage, totalPages);
      if (uiClickSound) {
         player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      }

   }

   private int calculateTotalPages(SpawnerData spawner) {
      int usedSlots = spawner.getVirtualInventory().getUsedSlots();
      return Math.max(1, (int)Math.ceil((double)usedSlots / 45.0D));
   }

   private void updateInventoryTitle(Player player, SpawnerData spawner, int page, int totalPages) {
      Map<String, String> placeholders = new HashMap(5);
      placeholders.put("current_page", String.valueOf(page));
      placeholders.put("total_pages", String.valueOf(totalPages));
      String titleFormat = this.languageManager.getGuiTitle("gui_title_storage");
      String entityName;
      if (titleFormat.contains("{entity}") || titleFormat.contains("{ᴇɴᴛɪᴛʏ}")) {
         if (spawner.isItemSpawner()) {
            entityName = this.languageManager.getVanillaItemName(spawner.getSpawnedItemMaterial());
         } else {
            entityName = this.languageManager.getFormattedMobName(spawner.getEntityType());
         }

         if (titleFormat.contains("{entity}")) {
            placeholders.put("entity", entityName);
         }

         if (titleFormat.contains("{ᴇɴᴛɪᴛʏ}")) {
            placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps(entityName));
         }
      }

      if (titleFormat.contains("{amount}")) {
         placeholders.put("amount", String.valueOf(spawner.getStackSize()));
      }

      entityName = this.languageManager.getGuiTitle("gui_title_storage", placeholders);

      try {
         player.getOpenInventory().setTitle(entityName);
      } catch (Exception var9) {
         this.openLootPage(player, spawner, page);
      }

   }

   private boolean isItemClickTooFrequent(Player player) {
      long now = System.currentTimeMillis();
      long last = (Long)this.lastItemClickTime.getOrDefault(player.getUniqueId(), 0L);
      this.lastItemClickTime.put(player.getUniqueId(), now);
      if (now - last < 150L) {
         this.messageService.sendMessage(player, "click_too_fast");
         return true;
      } else {
         return false;
      }
   }

   private boolean isControlClickTooFrequent(Player player) {
      long now = System.currentTimeMillis();
      long last = (Long)this.lastControlClickTime.getOrDefault(player.getUniqueId(), 0L);
      this.lastControlClickTime.put(player.getUniqueId(), now);
      return now - last < 300L;
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      UUID playerId = event.getPlayer().getUniqueId();
      this.lastItemClickTime.remove(playerId);
      this.lastControlClickTime.remove(playerId);
   }

   private void openMainMenu(Player player, SpawnerData spawner) {
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      if (spawner.isInteracted()) {
         this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
         spawner.clearInteracted();
      }

      if (this.plugin.getGuiLayoutConfig().isSkipMainGui()) {
         player.closeInventory();
      } else {
         if (this.isBedrockPlayer(player)) {
            if (this.plugin.getSpawnerMenuFormUI() != null) {
               this.plugin.getSpawnerMenuFormUI().openSpawnerForm(player, spawner);
            } else {
               this.spawnerMenuUI.openSpawnerMenu(player, spawner, true);
            }
         } else {
            this.spawnerMenuUI.openSpawnerMenu(player, spawner, true);
         }

      }
   }

   private boolean isBedrockPlayer(Player player) {
      return this.plugin.getIntegrationManager() != null && this.plugin.getIntegrationManager().getFloodgateHook() != null ? this.plugin.getIntegrationManager().getFloodgateHook().isBedrockPlayer(player) : false;
   }

   private void handleSortItemsClick(Player player, SpawnerData spawner, Inventory inventory) {
      if (!this.isControlClickTooFrequent(player)) {
         if (spawner.getLootConfig() != null && spawner.getLootConfig().getAllItems() != null) {
            List<LootItem> lootItems = spawner.getLootConfig().getAllItems();
            if (!lootItems.isEmpty()) {
               Material currentSort = spawner.getPreferredSortItem();
               List<Material> sortedLoot = lootItems.stream().map(LootItem::material).distinct().sorted(Comparator.comparing(Enum::name)).toList();
               if (!sortedLoot.isEmpty()) {
                  Material nextSort;
                  if (currentSort == null) {
                     nextSort = (Material)sortedLoot.getFirst();
                  } else {
                     int currentIndex = sortedLoot.indexOf(currentSort);
                     if (currentIndex == -1) {
                        nextSort = (Material)sortedLoot.getFirst();
                     } else {
                        int nextIndex = (currentIndex + 1) % sortedLoot.size();
                        nextSort = (Material)sortedLoot.get(nextIndex);
                     }
                  }

                  spawner.setPreferredSortItem(nextSort);
                  if (!spawner.isInteracted()) {
                     spawner.markInteracted();
                  }

                  this.spawnerManager.queueSpawnerForSaving(spawner.getSpawnerId());
                  spawner.getVirtualInventory().sortItems(nextSort);
                  StoragePageHolder holder = (StoragePageHolder)inventory.getHolder(false);
                  if (holder != null) {
                     this.updatePageContent(player, spawner, holder.getCurrentPage(), inventory, false);
                  }

                  player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.2F);
                  if (this.plugin.getSpawnerActionLogger() != null) {
                     this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_ITEMS_SORT, (builder) -> {
                        builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("sort_item", nextSort.name()).metadata("previous_sort", currentSort != null ? currentSort.name() : "none");
                     });
                  }

               }
            }
         }
      }
   }

   private void openLootPage(Player player, SpawnerData spawner, int page) {
      SpawnerStorageUI spawnerStorageUI = this.plugin.getSpawnerStorageUI();
      int totalPages = this.calculateTotalPages(spawner);
      int finalPage = Math.max(1, Math.min(page, totalPages));
      Inventory pageInventory = spawnerStorageUI.createStorageInventory(spawner, finalPage, totalPages);
      if (this.plugin.getSpawnerActionLogger() != null) {
         this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_STORAGE_OPEN, (builder) -> {
            builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("page", finalPage).metadata("total_pages", totalPages);
         });
      }

      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      player.openInventory(pageInventory);
   }

   public void handleTakeAllItems(Player player, Inventory sourceInventory) {
      if (!this.isControlClickTooFrequent(player)) {
         StoragePageHolder holder = (StoragePageHolder)sourceInventory.getHolder(false);
         SpawnerData spawner = holder.getSpawnerData();
         VirtualInventory virtualInv = spawner.getVirtualInventory();
         Map<Integer, ItemStack> sourceItems = new HashMap();

         for(int i = 0; i < 45; ++i) {
            ItemStack item = sourceInventory.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
               sourceItems.put(i, item.clone());
            }
         }

         if (sourceItems.isEmpty()) {
            this.messageService.sendMessage(player, "no_items_to_take");
         } else {
            SpawnerStorageAction.TransferResult result = this.transferItems(player, sourceInventory, sourceItems, virtualInv);
            this.sendTransferMessage(player, result);
            player.updateInventory();
            if (result.anyItemMoved) {
               int newTotalPages = this.calculateTotalPages(spawner);
               int currentPage = holder.getCurrentPage();
               int adjustedPage = Math.max(1, Math.min(currentPage, newTotalPages));
               holder.setTotalPages(newTotalPages);
               if (adjustedPage != currentPage) {
                  holder.setCurrentPage(adjustedPage);
                  SpawnerStorageUI spawnerStorageUI = this.plugin.getSpawnerStorageUI();
                  spawnerStorageUI.updateDisplay(sourceInventory, spawner, adjustedPage, newTotalPages);
               }

               this.updateInventoryTitle(player, spawner, adjustedPage, newTotalPages);
               this.spawnerGuiViewManager.updateSpawnerMenuViewers(spawner);
               if (spawner.getMaxSpawnerLootSlots() > holder.getOldUsedSlots() && spawner.getIsAtCapacity()) {
                  spawner.setIsAtCapacity(false);
               }

               if (!spawner.isInteracted()) {
                  spawner.markInteracted();
               }

               if (this.plugin.getSpawnerActionLogger() != null) {
                  int itemsLeft = spawner.getVirtualInventory().getUsedSlots();
                  this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_ITEM_TAKE_ALL, (builder) -> {
                     builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("items_taken", result.totalMoved).metadata("items_left", itemsLeft);
                  });
               }
            }

         }
      }
   }

   private SpawnerStorageAction.TransferResult transferItems(Player player, Inventory sourceInventory, Map<Integer, ItemStack> sourceItems, VirtualInventory virtualInv) {
      boolean anyItemMoved = false;
      boolean inventoryFull = false;
      PlayerInventory playerInv = player.getInventory();
      int totalAmountMoved = 0;
      List<ItemStack> itemsToRemove = new ArrayList();
      Iterator var10 = sourceItems.entrySet().iterator();

      while(var10.hasNext()) {
         Entry<Integer, ItemStack> entry = (Entry)var10.next();
         int sourceSlot = (Integer)entry.getKey();
         ItemStack itemToMove = (ItemStack)entry.getValue();
         int amountToMove = itemToMove.getAmount();
         int amountMoved = 0;

         ItemStack targetItem;
         for(int i = 0; i < 36 && amountToMove > 0; ++i) {
            targetItem = playerInv.getItem(i);
            if (targetItem != null && targetItem.getType() != Material.AIR) {
               if (targetItem.isSimilar(itemToMove)) {
                  int spaceInStack = targetItem.getMaxStackSize() - targetItem.getAmount();
                  if (spaceInStack > 0) {
                     int addAmount = Math.min(spaceInStack, amountToMove);
                     targetItem.setAmount(targetItem.getAmount() + addAmount);
                     amountMoved += addAmount;
                     amountToMove -= addAmount;
                     anyItemMoved = true;
                  }
               }
            } else {
               ItemStack newStack = itemToMove.clone();
               newStack.setAmount(Math.min(amountToMove, itemToMove.getMaxStackSize()));
               playerInv.setItem(i, newStack);
               amountMoved += newStack.getAmount();
               amountToMove -= newStack.getAmount();
               anyItemMoved = true;
            }
         }

         if (amountMoved > 0) {
            totalAmountMoved += amountMoved;
            ItemStack movedItem = itemToMove.clone();
            movedItem.setAmount(amountMoved);
            itemsToRemove.add(movedItem);
            if (amountMoved == itemToMove.getAmount()) {
               sourceInventory.setItem(sourceSlot, (ItemStack)null);
            } else {
               targetItem = itemToMove.clone();
               targetItem.setAmount(itemToMove.getAmount() - amountMoved);
               sourceInventory.setItem(sourceSlot, targetItem);
               inventoryFull = true;
            }
         }

         if (inventoryFull) {
            break;
         }
      }

      if (!itemsToRemove.isEmpty()) {
         StoragePageHolder holder = (StoragePageHolder)sourceInventory.getHolder(false);
         SpawnerData spawnerData = holder.getSpawnerData();
         spawnerData.removeItemsAndUpdateSellValue(itemsToRemove);
         spawnerData.updateHologramData();
         holder.updateOldUsedSlots();
      }

      return new SpawnerStorageAction.TransferResult(anyItemMoved, inventoryFull, totalAmountMoved);
   }

   private void sendTransferMessage(Player player, SpawnerStorageAction.TransferResult result) {
      if (!result.anyItemMoved) {
         this.messageService.sendMessage(player, "inventory_full");
      } else {
         Map<String, String> placeholders = new HashMap();
         placeholders.put("amount", String.valueOf(result.totalMoved));
         this.messageService.sendMessage((Player)player, "take_all_items", placeholders);
      }

   }

   @EventHandler
   public void onInventoryDrag(InventoryDragEvent event) {
      if (event.getInventory().getHolder(false) instanceof StoragePageHolder) {
         event.setCancelled(true);
      }
   }

   @EventHandler
   public void onInventoryClose(InventoryCloseEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof StoragePageHolder) {
         StoragePageHolder holder = (StoragePageHolder)var3;
         SpawnerData spawner = holder.getSpawnerData();
         if (spawner.isInteracted()) {
            this.plugin.getSpawnerManager().markSpawnerModified(spawner.getSpawnerId());
            spawner.clearInteracted();
         }

      }
   }

   private static record TransferResult(boolean anyItemMoved, boolean inventoryFull, int totalMoved) {
      private TransferResult(boolean anyItemMoved, boolean inventoryFull, int totalMoved) {
         this.anyItemMoved = anyItemMoved;
         this.inventoryFull = inventoryFull;
         this.totalMoved = totalMoved;
      }

      public boolean anyItemMoved() {
         return this.anyItemMoved;
      }

      public boolean inventoryFull() {
         return this.inventoryFull;
      }

      public int totalMoved() {
         return this.totalMoved;
      }
   }
}
