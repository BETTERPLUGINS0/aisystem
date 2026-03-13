package github.nighter.smartspawner.spawner.gui.storage;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayoutConfig;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerStorageUI {
   private static final int INVENTORY_SIZE = 54;
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private GuiLayoutConfig layoutConfig;
   private final Map<String, ItemStack> staticButtons;
   private final Map<String, ItemStack> navigationButtonCache;
   private final Map<String, ItemStack> pageIndicatorCache;
   private static final int MAX_CACHE_SIZE = 100;
   private String cachedStorageTitleFormat = null;
   private Scheduler.Task cleanupTask;

   public SpawnerStorageUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.layoutConfig = plugin.getGuiLayoutConfig();
      this.staticButtons = new HashMap(8);
      this.navigationButtonCache = new ConcurrentHashMap(16);
      this.pageIndicatorCache = new ConcurrentHashMap(16);
      this.initializeStaticButtons();
      this.startCleanupTask();
   }

   public void reload() {
      this.layoutConfig = this.plugin.getGuiLayoutConfig();
      this.navigationButtonCache.clear();
      this.pageIndicatorCache.clear();
      this.staticButtons.clear();
      this.cachedStorageTitleFormat = null;
      this.initializeStaticButtons();
   }

   private void initializeStaticButtons() {
      GuiLayout layout = this.layoutConfig.getCurrentStorageLayout();
      Iterator var2 = layout.getAllButtons().values().iterator();

      while(var2.hasNext()) {
         GuiButton button = (GuiButton)var2.next();
         String action = this.getAnyActionFromButton(button);
         if (action != null) {
            byte var6 = -1;
            switch(action.hashCode()) {
            case -1486471320:
               if (action.equals("return_main")) {
                  var6 = 0;
               }
               break;
            case -644398615:
               if (action.equals("take_all")) {
                  var6 = 1;
               }
               break;
            case -513611297:
               if (action.equals("drop_page")) {
                  var6 = 3;
               }
               break;
            case -427500787:
               if (action.equals("open_filter")) {
                  var6 = 4;
               }
               break;
            case -379776545:
               if (action.equals("sort_items")) {
                  var6 = 2;
               }
            }

            switch(var6) {
            case 0:
               this.staticButtons.put("return", this.createButton(button.getMaterial(), this.languageManager.getGuiItemName("return_button.name"), this.languageManager.getGuiItemLoreAsList("return_button.lore")));
               break;
            case 1:
               this.staticButtons.put("takeAll", this.createButton(button.getMaterial(), this.languageManager.getGuiItemName("take_all_button.name"), this.languageManager.getGuiItemLoreAsList("take_all_button.lore")));
            case 2:
            default:
               break;
            case 3:
               this.staticButtons.put("dropPage", this.createButton(button.getMaterial(), this.languageManager.getGuiItemName("drop_page_button.name"), this.languageManager.getGuiItemLoreAsList("drop_page_button.lore")));
               break;
            case 4:
               this.staticButtons.put("itemFilter", this.createButton(button.getMaterial(), this.languageManager.getGuiItemName("item_filter_button.name"), this.languageManager.getGuiItemLoreAsList("item_filter_button.lore")));
            }
         }
      }

   }

   private String getAnyActionFromButton(GuiButton button) {
      Map<String, String> actions = button.getActions();
      if (actions != null && !actions.isEmpty()) {
         String action = (String)actions.get("click");
         if (action != null && !action.isEmpty()) {
            return action;
         } else {
            action = (String)actions.get("left_click");
            if (action != null && !action.isEmpty()) {
               return action;
            } else {
               action = (String)actions.get("right_click");
               return action != null && !action.isEmpty() ? action : null;
            }
         }
      } else {
         return null;
      }
   }

   private String getStorageTitle(SpawnerData spawner, int page, int totalPages) {
      if (this.cachedStorageTitleFormat == null) {
         this.cachedStorageTitleFormat = this.languageManager.getGuiTitle("gui_title_storage");
      }

      Map<String, String> placeholders = new HashMap(4);
      placeholders.put("current_page", String.valueOf(page));
      placeholders.put("total_pages", String.valueOf(totalPages));
      if (this.cachedStorageTitleFormat.contains("{entity}") || this.cachedStorageTitleFormat.contains("{ᴇɴᴛɪᴛʏ}")) {
         String entityName;
         if (spawner.isItemSpawner()) {
            entityName = this.languageManager.getVanillaItemName(spawner.getSpawnedItemMaterial());
         } else {
            entityName = this.languageManager.getFormattedMobName(spawner.getEntityType());
         }

         if (this.cachedStorageTitleFormat.contains("{entity}")) {
            placeholders.put("entity", entityName);
         }

         if (this.cachedStorageTitleFormat.contains("{ᴇɴᴛɪᴛʏ}")) {
            placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps(entityName));
         }
      }

      if (this.cachedStorageTitleFormat.contains("{amount}")) {
         placeholders.put("amount", String.valueOf(spawner.getStackSize()));
      }

      return this.languageManager.getGuiTitle("gui_title_storage", placeholders);
   }

   public Inventory createStorageInventory(SpawnerData spawner, int page, int totalPages) {
      if (totalPages == -1) {
         totalPages = this.calculateTotalPages(spawner);
      }

      page = Math.max(1, Math.min(page, totalPages));
      Inventory pageInv = Bukkit.createInventory(new StoragePageHolder(spawner, page, totalPages), 54, this.getStorageTitle(spawner, page, totalPages));
      this.updateDisplay(pageInv, spawner, page, totalPages);
      return pageInv;
   }

   public void updateDisplay(Inventory inventory, SpawnerData spawner, int page, int totalPages) {
      if (!spawner.getInventoryLock().tryLock()) {
         if (this.plugin.isDebugMode()) {
            this.plugin.debug("Skipping GUI update - inventory operation in progress for spawner " + spawner.getSpawnerId());
         }

      } else {
         if (totalPages == -1) {
            totalPages = this.calculateTotalPages(spawner);
         }

         Map<Integer, ItemStack> updates = new HashMap();
         Set<Integer> slotsToEmpty = new HashSet();

         for(int i = 0; i < 45; ++i) {
            slotsToEmpty.add(i);
         }

         GuiLayout layout = this.layoutConfig.getCurrentLayout();
         if (layout != null) {
            slotsToEmpty.addAll(layout.getUsedSlots());
         }

         this.addPageItems(updates, slotsToEmpty, spawner, page);
         this.addNavigationButtons(updates, spawner, page, totalPages);
         Iterator var8 = slotsToEmpty.iterator();

         int oldUsedSlots;
         while(var8.hasNext()) {
            oldUsedSlots = (Integer)var8.next();
            if (!updates.containsKey(oldUsedSlots)) {
               inventory.setItem(oldUsedSlots, (ItemStack)null);
            }
         }

         var8 = updates.entrySet().iterator();

         while(var8.hasNext()) {
            Entry<Integer, ItemStack> entry = (Entry)var8.next();
            inventory.setItem((Integer)entry.getKey(), (ItemStack)entry.getValue());
         }

         if (this.plugin.getConfig().getBoolean("hologram.enabled", false)) {
            Location var10000 = spawner.getSpawnerLocation();
            Objects.requireNonNull(spawner);
            Scheduler.runLocationTask(var10000, spawner::updateHologramData);
         }

         StoragePageHolder holder = (StoragePageHolder)inventory.getHolder(false);

         assert holder != null;

         oldUsedSlots = holder.getOldUsedSlots();
         int currentUsedSlots = spawner.getVirtualInventory().getUsedSlots();
         if (oldUsedSlots != currentUsedSlots) {
            int newTotalPages = this.calculateTotalPages(spawner);
            holder.setTotalPages(newTotalPages);
            holder.updateOldUsedSlots();
         }

      }
   }

   private void addPageItems(Map<Integer, ItemStack> updates, Set<Integer> slotsToEmpty, SpawnerData spawner, int page) {
      try {
         VirtualInventory virtualInv = spawner.getVirtualInventory();
         Map<Integer, ItemStack> displayItems = virtualInv.getDisplayInventory();
         if (displayItems.isEmpty()) {
            return;
         }

         int startIndex = (page - 1) * 45;
         Iterator var8 = displayItems.entrySet().iterator();

         while(var8.hasNext()) {
            Entry<Integer, ItemStack> entry = (Entry)var8.next();
            int globalIndex = (Integer)entry.getKey();
            if (globalIndex >= startIndex && globalIndex < startIndex + 45) {
               int displaySlot = globalIndex - startIndex;
               updates.put(displaySlot, (ItemStack)entry.getValue());
               slotsToEmpty.remove(displaySlot);
            }
         }
      } finally {
         spawner.getInventoryLock().unlock();
      }

   }

   private void addNavigationButtons(Map<Integer, ItemStack> updates, SpawnerData spawner, int page, int totalPages) {
      if (totalPages == -1) {
         totalPages = this.calculateTotalPages(spawner);
      }

      GuiLayout layout = this.layoutConfig.getCurrentStorageLayout();
      Iterator var6 = layout.getAllButtons().values().iterator();

      while(true) {
         GuiButton button;
         do {
            do {
               if (!var6.hasNext()) {
                  return;
               }

               button = (GuiButton)var6.next();
            } while(!button.isEnabled());
         } while(button.hasCondition() && !this.evaluateButtonCondition(button));

         String action = this.getAnyActionFromButton(button);
         if (action != null) {
            ItemStack item = null;
            byte var11 = -1;
            switch(action.hashCode()) {
            case -1486471320:
               if (action.equals("return_main")) {
                  var11 = 6;
               }
               break;
            case -644398615:
               if (action.equals("take_all")) {
                  var11 = 2;
               }
               break;
            case -513611297:
               if (action.equals("drop_page")) {
                  var11 = 4;
               }
               break;
            case -427500787:
               if (action.equals("open_filter")) {
                  var11 = 5;
               }
               break;
            case -379776545:
               if (action.equals("sort_items")) {
                  var11 = 3;
               }
               break;
            case -266558761:
               if (action.equals("previous_page")) {
                  var11 = 0;
               }
               break;
            case 672232392:
               if (action.equals("sell_and_exp")) {
                  var11 = 8;
               }
               break;
            case 1197899572:
               if (action.equals("sell_all")) {
                  var11 = 7;
               }
               break;
            case 1217097819:
               if (action.equals("next_page")) {
                  var11 = 1;
               }
            }

            String cacheKey;
            switch(var11) {
            case 0:
               if (page > 1) {
                  cacheKey = "prev-" + (page - 1);
                  item = (ItemStack)this.navigationButtonCache.computeIfAbsent(cacheKey, (k) -> {
                     return this.createNavigationButton("previous", page - 1, button.getMaterial());
                  });
               }
               break;
            case 1:
               if (page < totalPages) {
                  cacheKey = "next-" + (page + 1);
                  item = (ItemStack)this.navigationButtonCache.computeIfAbsent(cacheKey, (k) -> {
                     return this.createNavigationButton("next", page + 1, button.getMaterial());
                  });
               }
               break;
            case 2:
               item = (ItemStack)this.staticButtons.get("takeAll");
               break;
            case 3:
               item = this.createSortButton(spawner, button.getMaterial());
               break;
            case 4:
               item = (ItemStack)this.staticButtons.get("dropPage");
               break;
            case 5:
               item = (ItemStack)this.staticButtons.get("itemFilter");
               break;
            case 6:
               item = (ItemStack)this.staticButtons.get("return");
               break;
            case 7:
               item = this.createSellButton(spawner, button.getMaterial());
               break;
            case 8:
               item = this.createSellAndExpButton(spawner, button.getMaterial());
            }

            if (item != null) {
               updates.put(button.getSlot(), item);
            }
         }
      }
   }

   private boolean evaluateButtonCondition(GuiButton button) {
      String condition = button.getCondition();
      if (condition != null && !condition.isEmpty()) {
         byte var4 = -1;
         switch(condition.hashCode()) {
         case -947888951:
            if (condition.equals("no_shop_integration")) {
               var4 = 1;
            }
            break;
         case 1040182795:
            if (condition.equals("shop_integration")) {
               var4 = 0;
            }
         }

         switch(var4) {
         case 0:
            return this.plugin.hasSellIntegration();
         case 1:
            return !this.plugin.hasSellIntegration();
         default:
            this.plugin.getLogger().warning("Unknown button condition: " + condition);
            return true;
         }
      } else {
         return true;
      }
   }

   private int calculateTotalPages(SpawnerData spawner) {
      int usedSlots = spawner.getVirtualInventory().getUsedSlots();
      return Math.max(1, (int)Math.ceil((double)usedSlots / 45.0D));
   }

   private ItemStack createButton(Material material, String name, List<String> lore) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(name);
         if (!lore.isEmpty()) {
            meta.setLore(lore);
         }

         item.setItemMeta(meta);
      }

      if (material == Material.BUNDLE) {
         VersionInitializer.hideTooltip(item);
      }

      return item;
   }

   private ItemStack createNavigationButton(String type, int targetPage, Material material) {
      Map<String, String> placeholders = new HashMap();
      placeholders.put("target_page", String.valueOf(targetPage));
      String buttonKey;
      if (type.equals("previous")) {
         buttonKey = "navigation_button_previous";
      } else {
         buttonKey = "navigation_button_next";
      }

      String buttonName = this.languageManager.getGuiItemName(buttonKey + ".name", placeholders);
      String[] buttonLore = this.languageManager.getGuiItemLore(buttonKey + ".lore", placeholders);
      return this.createButton(material, buttonName, Arrays.asList(buttonLore));
   }

   private ItemStack createSellButton(SpawnerData spawner, Material material) {
      Map<String, String> placeholders = new HashMap();
      if (spawner.isSellValueDirty()) {
         spawner.recalculateSellValue();
      }

      double totalSellPrice = spawner.getAccumulatedSellValue();
      placeholders.put("total_sell_price", this.languageManager.formatNumber(totalSellPrice));
      String name = this.languageManager.getGuiItemName("sell_button.name", placeholders);
      List<String> lore = this.languageManager.getGuiItemLoreAsList("sell_button.lore");
      return this.createButton(material, name, lore);
   }

   private ItemStack createSellAndExpButton(SpawnerData spawner, Material material) {
      Map<String, String> placeholders = new HashMap();
      if (spawner.isSellValueDirty()) {
         spawner.recalculateSellValue();
      }

      double totalSellPrice = spawner.getAccumulatedSellValue();
      placeholders.put("total_sell_price", this.languageManager.formatNumber(totalSellPrice));
      String name = this.languageManager.getGuiItemName("sell_and_exp_button.name", placeholders);
      List<String> lore = this.languageManager.getGuiItemLoreAsList("sell_and_exp_button.lore");
      return this.createButton(material, name, lore);
   }

   private ItemStack createSortButton(SpawnerData spawner, Material material) {
      Map<String, String> placeholders = new HashMap();
      Material currentSort = spawner.getPreferredSortItem();
      String selectedItemFormat = this.languageManager.getGuiItemName("sort_items_button.selected_item");
      String unselectedItemFormat = this.languageManager.getGuiItemName("sort_items_button.unselected_item");
      String noneText = this.languageManager.getGuiItemName("sort_items_button.no_item");
      StringBuilder availableItems = new StringBuilder();
      List lore;
      if (spawner.getLootConfig() != null && spawner.getLootConfig().getAllItems() != null) {
         boolean first = true;
         lore = spawner.getLootConfig().getAllItems().stream().sorted(Comparator.comparing((item) -> {
            return item.material().name();
         })).toList();

         for(Iterator var11 = lore.iterator(); var11.hasNext(); first = false) {
            LootItem lootItem = (LootItem)var11.next();
            if (!first) {
               availableItems.append("\n");
            }

            String itemName = this.languageManager.getVanillaItemName(lootItem.material());
            String format = currentSort == lootItem.material() ? selectedItemFormat : unselectedItemFormat;
            String formattedItem = format.replace("{item_name}", itemName);
            availableItems.append(formattedItem);
         }
      }

      if (availableItems.isEmpty()) {
         availableItems.append(noneText);
      }

      placeholders.put("available_items", availableItems.toString());
      String name = this.languageManager.getGuiItemName("sort_items_button.name", placeholders);
      lore = this.languageManager.getGuiItemLoreWithMultilinePlaceholders("sort_items_button.lore", placeholders);
      return this.createButton(material, name, lore);
   }

   private void startCleanupTask() {
      this.cleanupTask = Scheduler.runTaskTimer(this::cleanupCaches, 600L, 600L);
   }

   public void cancelTasks() {
      if (this.cleanupTask != null) {
         this.cleanupTask.cancel();
         this.cleanupTask = null;
      }

   }

   private void cleanupCaches() {
      int toRemove;
      ArrayList keysToRemove;
      int i;
      if (this.navigationButtonCache.size() > 100) {
         toRemove = this.navigationButtonCache.size() - 50;
         keysToRemove = new ArrayList(this.navigationButtonCache.keySet());

         for(i = 0; i < Math.min(toRemove, keysToRemove.size()); ++i) {
            this.navigationButtonCache.remove(keysToRemove.get(i));
         }
      }

      if (this.pageIndicatorCache.size() > 100) {
         toRemove = this.pageIndicatorCache.size() - 50;
         keysToRemove = new ArrayList(this.pageIndicatorCache.keySet());

         for(i = 0; i < Math.min(toRemove, keysToRemove.size()); ++i) {
            this.pageIndicatorCache.remove(keysToRemove.get(i));
         }
      }

   }

   public void cleanup() {
      this.navigationButtonCache.clear();
      this.pageIndicatorCache.clear();
      this.cachedStorageTitleFormat = null;
      this.cancelTasks();
      this.initializeStaticButtons();
   }

   @Generated
   public GuiLayoutConfig getLayoutConfig() {
      return this.layoutConfig;
   }
}
