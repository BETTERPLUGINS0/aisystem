package github.nighter.smartspawner.spawner.gui.storage.filter;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.logging.SpawnerEventType;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.gui.storage.SpawnerStorageUI;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FilterConfigUI implements Listener {
   private static final int INVENTORY_SIZE = 27;
   private static final Set<Integer> DIVIDER_SLOTS = Set.of(4, 13, 22);
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final SpawnerStorageUI storageUI;
   private final Map<String, ItemStack> staticButtons;
   private final Map<UUID, Long> lastItemClickTime = new ConcurrentHashMap();

   public FilterConfigUI(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.storageUI = plugin.getSpawnerStorageUI();
      this.staticButtons = new HashMap();
      this.initializeStaticButtons();
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }

   public void reload() {
      this.staticButtons.clear();
      this.initializeStaticButtons();
   }

   private void initializeStaticButtons() {
      this.staticButtons.put("divider", this.createButton(Material.CYAN_STAINED_GLASS_PANE, this.languageManager.getGuiItemName("filter_divider.name"), this.languageManager.getGuiItemLoreAsList("filter_divider.lore")));
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

      return item;
   }

   private boolean isClickTooFrequent(Player player) {
      long now = System.currentTimeMillis();
      long last = (Long)this.lastItemClickTime.getOrDefault(player.getUniqueId(), 0L);
      this.lastItemClickTime.put(player.getUniqueId(), now);
      return now - last < 200L;
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      UUID playerId = event.getPlayer().getUniqueId();
      this.lastItemClickTime.remove(playerId);
   }

   public void openFilterConfigGUI(Player player, SpawnerData spawner) {
      String title = this.languageManager.getGuiTitle("gui_title_filter_config");
      Inventory filterInventory = Bukkit.createInventory(new FilterConfigHolder(spawner), 27, title);
      this.setupFilterInventory(filterInventory, spawner);
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
      player.openInventory(filterInventory);
   }

   private void setupFilterInventory(Inventory inventory, SpawnerData spawner) {
      inventory.clear();
      List allLootItems;
      if (spawner.getLootConfig() == null) {
         allLootItems = Collections.emptyList();
      } else {
         allLootItems = spawner.getLootConfig().getAllItems();
      }

      Set<Material> filteredItems = spawner.getFilteredItems();
      ItemStack divider = (ItemStack)this.staticButtons.get("divider");
      Iterator var6 = DIVIDER_SLOTS.iterator();

      while(var6.hasNext()) {
         Integer slot = (Integer)var6.next();
         inventory.setItem(slot, divider);
      }

      List<ItemStack> allowedItems = new ArrayList();
      List<ItemStack> blockedItems = new ArrayList();
      Iterator var8 = allLootItems.iterator();

      while(var8.hasNext()) {
         LootItem lootItem = (LootItem)var8.next();
         ItemStack displayItem = lootItem.createItemStack(new Random());
         if (displayItem != null) {
            Material itemType = displayItem.getType();
            if (filteredItems.contains(itemType)) {
               displayItem = this.addFilterMarkerToItem(displayItem, true);
               blockedItems.add(displayItem);
            } else {
               displayItem = this.addFilterMarkerToItem(displayItem, false);
               allowedItems.add(displayItem);
            }
         }
      }

      this.placeItemsInSection(inventory, allowedItems, 0, 4);
      this.placeItemsInSection(inventory, blockedItems, 5, 4);
   }

   private ItemStack addFilterMarkerToItem(ItemStack item, boolean isFiltered) {
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         String itemName = this.languageManager.getVanillaItemName(item.getType());
         Map<String, String> placeholders = new HashMap();
         placeholders.put("item_name", itemName);
         placeholders.put("ɪᴛᴇᴍ_ɴᴀᴍᴇ", this.languageManager.getSmallCaps(itemName));
         String nameKey = isFiltered ? "filter_item_blocked.name" : "filter_item_allowed.name";
         meta.setDisplayName(this.languageManager.getGuiItemName(nameKey, placeholders));
         String loreKey = isFiltered ? "filter_item_blocked.lore" : "filter_item_allowed.lore";
         meta.setLore(this.languageManager.getGuiItemLoreAsList(loreKey, placeholders));
         item.setItemMeta(meta);
      }

      return item;
   }

   private void placeItemsInSection(Inventory inventory, List<ItemStack> items, int startColumn, int columnsSpan) {
      int index = 0;

      for(Iterator var6 = items.iterator(); var6.hasNext(); ++index) {
         ItemStack item = (ItemStack)var6.next();
         int row = index / columnsSpan;
         int column = startColumn + index % columnsSpan;
         int slot = row * 9 + column;
         if (slot < 27 && !DIVIDER_SLOTS.contains(slot)) {
            inventory.setItem(slot, item);
         }
      }

   }

   @EventHandler
   public void onFilterInventoryClick(InventoryClickEvent event) {
      InventoryHolder var3 = event.getInventory().getHolder(false);
      if (var3 instanceof FilterConfigHolder) {
         FilterConfigHolder holder = (FilterConfigHolder)var3;
         event.setCancelled(true);
         HumanEntity var4 = event.getWhoClicked();
         if (var4 instanceof Player) {
            Player player = (Player)var4;
            SpawnerData spawner = holder.getSpawnerData();
            if (!this.isSpawnerValid(spawner)) {
               player.closeInventory();
            } else if (!this.isClickTooFrequent(player)) {
               int slot = event.getRawSlot();
               if (DIVIDER_SLOTS.contains(slot)) {
                  this.returnToStorage(player, spawner);
               } else {
                  ItemStack clickedItem = event.getCurrentItem();
                  if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                     boolean wasUpdated = this.toggleItemFilter(player, spawner, clickedItem);
                     if (wasUpdated) {
                        this.setupFilterInventory(event.getInventory(), spawner);
                     }

                  }
               }
            }
         }
      }
   }

   private boolean isSpawnerValid(SpawnerData spawner) {
      if (spawner == null) {
         return false;
      } else {
         SpawnerManager spawnerManager = this.plugin.getSpawnerManager();
         SpawnerData current = spawnerManager.getSpawnerById(spawner.getSpawnerId());
         return current != null && current == spawner;
      }
   }

   private void returnToStorage(Player player, SpawnerData spawner) {
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.8F, 1.0F);
      player.closeInventory();
      Inventory pageInventory = this.storageUI.createStorageInventory(spawner, 1, -1);
      player.openInventory(pageInventory);
   }

   private boolean toggleItemFilter(Player player, SpawnerData spawner, ItemStack clickedItem) {
      Material itemType = clickedItem.getType();
      boolean wasFiltered = spawner.toggleItemFilter(itemType);
      Sound sound = wasFiltered ? Sound.BLOCK_NOTE_BLOCK_PLING : Sound.UI_BUTTON_CLICK;
      player.playSound(player.getLocation(), sound, 0.5F, 1.0F);
      if (this.plugin.getSpawnerActionLogger() != null) {
         this.plugin.getSpawnerActionLogger().log(SpawnerEventType.SPAWNER_ITEM_FILTER, (builder) -> {
            builder.player(player.getName(), player.getUniqueId()).location(spawner.getSpawnerLocation()).entityType(spawner.getEntityType()).metadata("item_type", itemType.name()).metadata("is_filtered", wasFiltered);
         });
      }

      return true;
   }
}
