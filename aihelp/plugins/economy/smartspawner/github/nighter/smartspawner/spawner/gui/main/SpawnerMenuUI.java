package github.nighter.smartspawner.spawner.gui.main;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerOpenGUIEvent;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.config.SpawnerMobHeadTexture;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import github.nighter.smartspawner.spawner.lootgen.loot.EntityLootConfig;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerMenuUI {
   private static final int INVENTORY_SIZE = 27;
   private static final int TICKS_PER_SECOND = 20;
   private static final Map<String, String> EMPTY_PLACEHOLDERS = Collections.emptyMap();
   private static final String LOOT_ITEM_FORMAT_KEY = "spawner_storage_item.loot_items";
   private static final String EMPTY_LOOT_MESSAGE_KEY = "spawner_storage_item.loot_items_empty";
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private String lootItemFormat;
   private String emptyLootMessage;
   private Material cachedStorageMaterial;
   private Material cachedExpMaterial;
   private final Map<String, ItemStack> itemCache;
   private final Map<String, Long> cacheTimestamps;
   private static final long CACHE_EXPIRY_TIME_MS = 30000L;

   public SpawnerMenuUI(SmartSpawner plugin) {
      this.cachedStorageMaterial = Material.CHEST;
      this.cachedExpMaterial = Material.EXPERIENCE_BOTTLE;
      this.itemCache = new ConcurrentHashMap();
      this.cacheTimestamps = new ConcurrentHashMap();
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.loadConfig();
   }

   public void loadConfig() {
      this.clearCache();
      this.lootItemFormat = this.languageManager.getGuiItemName("spawner_storage_item.loot_items", EMPTY_PLACEHOLDERS);
      this.emptyLootMessage = this.languageManager.getGuiItemName("spawner_storage_item.loot_items_empty", EMPTY_PLACEHOLDERS);
      GuiLayout layout = this.plugin.getGuiLayoutConfig().getCurrentMainLayout();
      Material storageMaterial = Material.CHEST;
      Material expMaterial = Material.EXPERIENCE_BOTTLE;
      Iterator var4 = layout.getAllButtons().values().iterator();

      while(var4.hasNext()) {
         GuiButton button = (GuiButton)var4.next();
         String action = button.getDefaultAction();
         if (action != null) {
            if ("open_storage".equals(action)) {
               storageMaterial = button.getMaterial();
            } else if ("collect_exp".equals(action)) {
               expMaterial = button.getMaterial();
            }
         }
      }

      this.cachedStorageMaterial = storageMaterial;
      this.cachedExpMaterial = expMaterial;
   }

   public void clearCache() {
      this.itemCache.clear();
      this.cacheTimestamps.clear();
   }

   public void invalidateSpawnerCache(String spawnerId) {
      this.itemCache.entrySet().removeIf((entry) -> {
         return ((String)entry.getKey()).startsWith(spawnerId + "|");
      });
      this.cacheTimestamps.entrySet().removeIf((entry) -> {
         return ((String)entry.getKey()).startsWith(spawnerId + "|");
      });
   }

   private boolean isCacheEntryExpired(String cacheKey) {
      Long timestamp = (Long)this.cacheTimestamps.get(cacheKey);
      return timestamp == null || System.currentTimeMillis() - timestamp > 30000L;
   }

   public void openSpawnerMenu(Player player, SpawnerData spawner, boolean refresh) {
      if (SpawnerOpenGUIEvent.getHandlerList().getRegisteredListeners().length != 0) {
         SpawnerOpenGUIEvent openEvent = new SpawnerOpenGUIEvent(player, spawner.getSpawnerLocation(), spawner.getEntityType(), spawner.getStackSize(), refresh);
         Bukkit.getPluginManager().callEvent(openEvent);
         if (openEvent.isCancelled()) {
            return;
         }
      }

      Inventory menu = this.createMenu(spawner);
      GuiLayout layout = this.plugin.getGuiLayoutConfig().getCurrentMainLayout();
      ItemStack[] items = new ItemStack[27];
      Iterator var7 = layout.getAllButtons().values().iterator();

      while(true) {
         GuiButton button;
         ItemStack item;
         label93:
         while(true) {
            String action;
            do {
               do {
                  do {
                     do {
                        if (!var7.hasNext()) {
                           for(int i = 0; i < items.length; ++i) {
                              if (items[i] != null) {
                                 menu.setItem(i, items[i]);
                              }
                           }

                           player.openInventory(menu);
                           if (!refresh) {
                              player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
                           }

                           if (this.plugin.getSpawnerGuiViewManager().isTimerPlaceholdersEnabled() && spawner.getSpawnerStop().get()) {
                              this.plugin.getSpawnerGuiViewManager().forceTimerUpdateInactive(player, spawner);
                           }

                           return;
                        }

                        button = (GuiButton)var7.next();
                     } while(!button.isEnabled());
                  } while(button.hasCondition() && !this.evaluateButtonCondition(button, player));

                  action = this.getAnyActionFromButton(button);
               } while(action == null);
            } while(action.isEmpty());

            item = null;
            byte var12 = -1;
            switch(action.hashCode()) {
            case -1410715584:
               if (action.equals("open_stacker")) {
                  var12 = 1;
               }
               break;
            case -1397348986:
               if (action.equals("open_storage")) {
                  var12 = 0;
               }
               break;
            case 3387192:
               if (action.equals("none")) {
                  var12 = 3;
               }
               break;
            case 672232392:
               if (action.equals("sell_and_exp")) {
                  var12 = 2;
               }
               break;
            case 1853584776:
               if (action.equals("collect_exp")) {
                  var12 = 4;
               }
            }

            switch(var12) {
            case 0:
               item = this.createLootStorageItem(spawner);
               break label93;
            case 1:
            case 2:
            case 3:
               item = this.createSpawnerInfoItem(player, spawner);
               break label93;
            case 4:
               item = this.createExpItem(spawner);
               break label93;
            default:
               this.plugin.getLogger().warning("Unknown action in main GUI: " + action);
            }
         }

         if (item != null) {
            items[button.getSlot()] = item;
         }
      }
   }

   private Inventory createMenu(SpawnerData spawner) {
      String entityName;
      if (spawner.isItemSpawner()) {
         entityName = this.languageManager.getVanillaItemName(spawner.getSpawnedItemMaterial());
      } else {
         entityName = this.languageManager.getFormattedMobName(spawner.getEntityType());
      }

      String entityNameSmallCaps = this.languageManager.getSmallCaps(entityName);
      Map<String, String> placeholders = new HashMap(4);
      placeholders.put("entity", entityName);
      placeholders.put("ᴇɴᴛɪᴛʏ", entityNameSmallCaps);
      placeholders.put("amount", String.valueOf(spawner.getStackSize()));
      String title;
      if (spawner.getStackSize() > 1) {
         title = this.languageManager.getGuiTitle("gui_title_main.stacked_spawner", placeholders);
      } else {
         title = this.languageManager.getGuiTitle("gui_title_main.single_spawner", placeholders);
      }

      return Bukkit.createInventory(new SpawnerMenuHolder(spawner), 27, title);
   }

   public ItemStack createLootStorageItem(SpawnerData spawner) {
      VirtualInventory virtualInventory = spawner.getVirtualInventory();
      int currentItems = virtualInventory.getUsedSlots();
      int maxSlots = spawner.getMaxSpawnerLootSlots();
      String cacheKey = spawner.getSpawnerId() + "|storage|" + currentItems + "|" + maxSlots + "|" + virtualInventory.hashCode();
      ItemStack cachedItem = (ItemStack)this.itemCache.get(cacheKey);
      if (cachedItem != null && !this.isCacheEntryExpired(cacheKey)) {
         return cachedItem.clone();
      } else {
         ItemStack chestItem = new ItemStack(this.cachedStorageMaterial);
         ItemMeta chestMeta = chestItem.getItemMeta();
         if (chestMeta == null) {
            return chestItem;
         } else {
            String nameTemplate = this.languageManager.getGuiItemName("spawner_storage_item.name", EMPTY_PLACEHOLDERS);
            List<String> loreTemplate = this.languageManager.getGuiItemLoreAsList("spawner_storage_item.lore", EMPTY_PLACEHOLDERS);
            Set<String> availablePlaceholders = Set.of("max_slots", "current_items", "percent_storage_rounded", "loot_items");
            Set<String> usedPlaceholders = new HashSet();
            usedPlaceholders.addAll(this.detectUsedPlaceholders(nameTemplate, availablePlaceholders));
            usedPlaceholders.addAll(this.detectUsedPlaceholders(loreTemplate, availablePlaceholders));
            Map<String, String> placeholders = new HashMap();
            if (usedPlaceholders.contains("max_slots")) {
               placeholders.put("max_slots", this.languageManager.formatNumber((double)maxSlots));
            }

            if (usedPlaceholders.contains("current_items")) {
               placeholders.put("current_items", String.valueOf(currentItems));
            }

            if (usedPlaceholders.contains("percent_storage_rounded")) {
               int percentStorage = this.calculatePercentage((long)currentItems, (long)maxSlots);
               placeholders.put("percent_storage_rounded", String.valueOf(percentStorage));
            }

            if (usedPlaceholders.contains("loot_items")) {
               Map<VirtualInventory.ItemSignature, Long> storedItems = virtualInventory.getConsolidatedItems();
               String lootItemsText = this.buildLootItemsText(spawner.getEntityType(), storedItems);
               placeholders.put("loot_items", lootItemsText);
            }

            chestMeta.setDisplayName(this.languageManager.getGuiItemName("spawner_storage_item.name", placeholders));
            List<String> lore = this.languageManager.getGuiItemLoreWithMultilinePlaceholders("spawner_storage_item.lore", placeholders);
            chestMeta.setLore(lore);
            chestItem.setItemMeta(chestMeta);
            if (this.cachedStorageMaterial == Material.BUNDLE) {
               VersionInitializer.hideTooltip(chestItem);
            }

            this.itemCache.put(cacheKey, chestItem.clone());
            this.cacheTimestamps.put(cacheKey, System.currentTimeMillis());
            return chestItem;
         }
      }
   }

   private String buildLootItemsText(EntityType entityType, Map<VirtualInventory.ItemSignature, Long> storedItems) {
      Map<Material, Long> materialAmountMap = new HashMap();
      Iterator var4 = storedItems.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var4.next();
         Material material = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef().getType();
         materialAmountMap.merge(material, (Long)entry.getValue(), Long::sum);
      }

      EntityLootConfig lootConfig = this.plugin.getSpawnerSettingsConfig().getLootConfig(entityType);
      List<LootItem> possibleLootItems = lootConfig != null ? lootConfig.getAllItems() : Collections.emptyList();
      if (possibleLootItems.isEmpty() && storedItems.isEmpty()) {
         return this.emptyLootMessage;
      } else {
         StringBuilder builder = new StringBuilder(Math.max(possibleLootItems.size(), storedItems.size()) * 40);
         String materialName;
         String materialNameSmallCaps;
         String formattedAmount;
         if (!possibleLootItems.isEmpty()) {
            possibleLootItems.sort(Comparator.comparing((item) -> {
               return this.languageManager.getVanillaItemName(item.material());
            }));
            Iterator var7 = possibleLootItems.iterator();

            while(var7.hasNext()) {
               LootItem lootItem = (LootItem)var7.next();
               Material material = lootItem.material();
               long amount = (Long)materialAmountMap.getOrDefault(material, 0L);
               String materialName = this.languageManager.getVanillaItemName(material);
               String materialNameSmallCaps = this.languageManager.getSmallCaps(this.languageManager.getVanillaItemName(material));
               materialName = this.languageManager.formatNumber((double)amount);
               Object[] var10001 = new Object[]{lootItem.chance()};
               materialNameSmallCaps = String.format("%.1f", var10001) + "%";
               formattedAmount = this.lootItemFormat.replace("{item_name}", materialName).replace("{ɪᴛᴇᴍ_ɴᴀᴍᴇ}", materialNameSmallCaps).replace("{amount}", materialName).replace("{raw_amount}", String.valueOf(amount)).replace("{chance}", materialNameSmallCaps);
               builder.append(formattedAmount).append('\n');
            }
         } else if (!storedItems.isEmpty()) {
            List<Entry<VirtualInventory.ItemSignature, Long>> sortedItems = new ArrayList(storedItems.entrySet());
            sortedItems.sort(Comparator.comparing((e) -> {
               return ((VirtualInventory.ItemSignature)e.getKey()).getMaterialName();
            }));
            Iterator var23 = sortedItems.iterator();

            while(var23.hasNext()) {
               Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var23.next();
               ItemStack templateItem = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef();
               Material material = templateItem.getType();
               long amount = (Long)entry.getValue();
               materialName = this.languageManager.getVanillaItemName(material);
               materialNameSmallCaps = this.languageManager.getSmallCaps(this.languageManager.getVanillaItemName(material));
               formattedAmount = this.languageManager.formatNumber((double)amount);
               String line = this.lootItemFormat.replace("{item_name}", materialName).replace("{ɪᴛᴇᴍ_ɴᴀᴍᴇ}", materialNameSmallCaps).replace("{amount}", formattedAmount).replace("{raw_amount}", String.valueOf(amount)).replace("{chance}", "");
               builder.append(line).append('\n');
            }
         }

         int length = builder.length();
         if (length > 0 && builder.charAt(length - 1) == '\n') {
            builder.setLength(length - 1);
         }

         return builder.toString();
      }
   }

   public ItemStack createSpawnerInfoItem(Player player, SpawnerData spawner) {
      GuiLayout layout = this.plugin.getGuiLayoutConfig().getCurrentMainLayout();
      GuiButton spawnerInfoButton = null;
      Iterator var5 = layout.getAllButtons().values().iterator();

      while(var5.hasNext()) {
         GuiButton button = (GuiButton)var5.next();
         if (button.isInfoButton()) {
            spawnerInfoButton = button;
            break;
         }

         String action = this.getAnyActionFromButton(button);
         if (action != null && (action.equals("open_stacker") || action.equals("sell_and_exp") || action.equals("none")) && (!button.hasCondition() || this.evaluateButtonCondition(button, player))) {
            spawnerInfoButton = button;
            break;
         }
      }

      EntityType entityType = spawner.getEntityType();
      int stackSize = spawner.getStackSize();
      VirtualInventory virtualInventory = spawner.getVirtualInventory();
      int currentItems = virtualInventory.getUsedSlots();
      int maxSlots = spawner.getMaxSpawnerLootSlots();
      long currentExp = (long)spawner.getSpawnerExp();
      long maxExp = (long)spawner.getMaxStoredExp();
      boolean hasShopPermission = this.plugin.hasSellIntegration() && player.hasPermission("smartspawner.sellall");
      String nameTemplate = this.languageManager.getGuiItemName("spawner_info_item.name", EMPTY_PLACEHOLDERS);
      String loreKey = hasShopPermission ? "spawner_info_item.lore" : "spawner_info_item.lore_no_shop";
      List<String> loreTemplate = this.languageManager.getGuiItemLoreAsList(loreKey, EMPTY_PLACEHOLDERS);
      Set<String> availablePlaceholders = Set.of(new String[]{"entity", "ᴇɴᴛɪᴛʏ", "stack_size", "range", "delay", "min_mobs", "max_mobs", "current_items", "max_items", "percent_storage_decimal", "percent_storage_rounded", "current_exp", "max_exp", "raw_current_exp", "raw_max_exp", "percent_exp_decimal", "percent_exp_rounded", "total_sell_price", "time"});
      Set<String> usedPlaceholders = new HashSet();
      usedPlaceholders.addAll(this.detectUsedPlaceholders(nameTemplate, availablePlaceholders));
      usedPlaceholders.addAll(this.detectUsedPlaceholders(loreTemplate, availablePlaceholders));
      Map<String, String> placeholders = new HashMap();
      String timerValue;
      if (usedPlaceholders.contains("entity") || usedPlaceholders.contains("ᴇɴᴛɪᴛʏ")) {
         if (spawner.isItemSpawner()) {
            timerValue = this.languageManager.getVanillaItemName(spawner.getSpawnedItemMaterial());
         } else {
            timerValue = this.languageManager.getFormattedMobName(entityType);
         }

         if (usedPlaceholders.contains("entity")) {
            placeholders.put("entity", timerValue);
         }

         if (usedPlaceholders.contains("ᴇɴᴛɪᴛʏ")) {
            placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps(timerValue));
         }
      }

      if (usedPlaceholders.contains("stack_size")) {
         placeholders.put("stack_size", String.valueOf(stackSize));
      }

      if (usedPlaceholders.contains("range")) {
         placeholders.put("range", String.valueOf(spawner.getSpawnerRange()));
      }

      if (usedPlaceholders.contains("delay")) {
         long delaySeconds = spawner.getSpawnDelay() / 20L;
         placeholders.put("delay", String.valueOf(delaySeconds));
      }

      if (usedPlaceholders.contains("min_mobs")) {
         placeholders.put("min_mobs", String.valueOf(spawner.getMinMobs()));
      }

      if (usedPlaceholders.contains("max_mobs")) {
         placeholders.put("max_mobs", String.valueOf(spawner.getMaxMobs()));
      }

      if (usedPlaceholders.contains("current_items")) {
         placeholders.put("current_items", String.valueOf(currentItems));
      }

      if (usedPlaceholders.contains("max_items")) {
         placeholders.put("max_items", this.languageManager.formatNumber((double)maxSlots));
      }

      String formattedPercentExp;
      double totalSellPrice;
      int percentExpRounded;
      if (usedPlaceholders.contains("percent_storage_decimal") || usedPlaceholders.contains("percent_storage_rounded")) {
         totalSellPrice = maxSlots > 0 ? (double)currentItems / (double)maxSlots * 100.0D : 0.0D;
         if (usedPlaceholders.contains("percent_storage_decimal")) {
            formattedPercentExp = String.format("%.1f", totalSellPrice);
            placeholders.put("percent_storage_decimal", formattedPercentExp);
         }

         if (usedPlaceholders.contains("percent_storage_rounded")) {
            percentExpRounded = (int)Math.round(totalSellPrice);
            placeholders.put("percent_storage_rounded", String.valueOf(percentExpRounded));
         }
      }

      if (usedPlaceholders.contains("current_exp")) {
         placeholders.put("current_exp", this.languageManager.formatNumber((double)currentExp));
      }

      if (usedPlaceholders.contains("max_exp")) {
         placeholders.put("max_exp", this.languageManager.formatNumber((double)maxExp));
      }

      if (usedPlaceholders.contains("raw_current_exp")) {
         placeholders.put("raw_current_exp", String.valueOf(currentExp));
      }

      if (usedPlaceholders.contains("raw_max_exp")) {
         placeholders.put("raw_max_exp", String.valueOf(maxExp));
      }

      if (usedPlaceholders.contains("percent_exp_decimal") || usedPlaceholders.contains("percent_exp_rounded")) {
         totalSellPrice = maxExp > 0L ? (double)currentExp / (double)maxExp * 100.0D : 0.0D;
         if (usedPlaceholders.contains("percent_exp_decimal")) {
            formattedPercentExp = String.format("%.1f", totalSellPrice);
            placeholders.put("percent_exp_decimal", formattedPercentExp);
         }

         if (usedPlaceholders.contains("percent_exp_rounded")) {
            percentExpRounded = (int)Math.round(totalSellPrice);
            placeholders.put("percent_exp_rounded", String.valueOf(percentExpRounded));
         }
      }

      if (usedPlaceholders.contains("total_sell_price")) {
         if (spawner.isSellValueDirty()) {
            spawner.recalculateSellValue();
         }

         totalSellPrice = spawner.getAccumulatedSellValue();
         placeholders.put("total_sell_price", this.languageManager.formatNumber(totalSellPrice));
      }

      if (usedPlaceholders.contains("time")) {
         timerValue = this.plugin.getSpawnerGuiViewManager().calculateTimerDisplay(spawner, player);
         placeholders.put("time", timerValue);
      }

      Consumer<ItemMeta> metaModifier = (meta) -> {
         meta.setDisplayName(this.languageManager.getGuiItemName("spawner_info_item.name", placeholders));
         List<String> lore = this.languageManager.getGuiItemLoreWithMultilinePlaceholders(loreKey, placeholders);
         meta.setLore(lore);
      };
      ItemStack spawnerItem;
      if (spawner.isItemSpawner()) {
         spawnerItem = SpawnerMobHeadTexture.getItemSpawnerHead(spawner.getSpawnedItemMaterial(), player, metaModifier);
      } else if (spawnerInfoButton != null && spawnerInfoButton.getMaterial() == Material.PLAYER_HEAD) {
         spawnerItem = SpawnerMobHeadTexture.getCustomHead(entityType, player, metaModifier);
      } else if (spawnerInfoButton != null) {
         spawnerItem = new ItemStack(spawnerInfoButton.getMaterial());
         spawnerItem.editMeta(metaModifier);
      } else {
         spawnerItem = SpawnerMobHeadTexture.getCustomHead(entityType, player, metaModifier);
      }

      if (spawnerItem.getType() == Material.SPAWNER) {
         VersionInitializer.hideTooltip(spawnerItem);
      }

      return spawnerItem;
   }

   public ItemStack createExpItem(SpawnerData spawner) {
      long currentExp = (long)spawner.getSpawnerExp();
      long maxExp = (long)spawner.getMaxStoredExp();
      int percentExp = this.calculatePercentage(currentExp, maxExp);
      String cacheKey = spawner.getSpawnerId() + "|exp|" + currentExp + "|" + maxExp;
      ItemStack cachedItem = (ItemStack)this.itemCache.get(cacheKey);
      if (cachedItem != null && !this.isCacheEntryExpired(cacheKey)) {
         return cachedItem.clone();
      } else {
         ItemStack expItem = new ItemStack(this.cachedExpMaterial);
         ItemMeta expMeta = expItem.getItemMeta();
         if (expMeta == null) {
            return expItem;
         } else {
            String formattedExp = this.languageManager.formatNumber((double)currentExp);
            String formattedMaxExp = this.languageManager.formatNumber((double)maxExp);
            Map<String, String> placeholders = new HashMap(5);
            placeholders.put("current_exp", formattedExp);
            placeholders.put("raw_current_exp", String.valueOf(currentExp));
            placeholders.put("max_exp", formattedMaxExp);
            placeholders.put("percent_exp_rounded", String.valueOf(percentExp));
            placeholders.put("u_max_exp", String.valueOf(maxExp));
            expMeta.setDisplayName(this.languageManager.getGuiItemName("exp_info_item.name", placeholders));
            List<String> loreExp = this.languageManager.getGuiItemLoreAsList("exp_info_item.lore", placeholders);
            expMeta.setLore(loreExp);
            expItem.setItemMeta(expMeta);
            if (this.cachedExpMaterial == Material.BUNDLE) {
               VersionInitializer.hideTooltip(expItem);
            }

            this.itemCache.put(cacheKey, expItem.clone());
            this.cacheTimestamps.put(cacheKey, System.currentTimeMillis());
            return expItem;
         }
      }
   }

   private int calculatePercentage(long current, long maximum) {
      return maximum > 0L ? (int)((double)current / (double)maximum * 100.0D) : 0;
   }

   private Set<String> detectUsedPlaceholders(String text, Set<String> availablePlaceholders) {
      Set<String> usedPlaceholders = new HashSet();
      if (text != null && !text.isEmpty()) {
         Iterator var4 = availablePlaceholders.iterator();

         while(var4.hasNext()) {
            String placeholder = (String)var4.next();
            if (text.contains("{" + placeholder + "}")) {
               usedPlaceholders.add(placeholder);
            }
         }

         return usedPlaceholders;
      } else {
         return usedPlaceholders;
      }
   }

   private Set<String> detectUsedPlaceholders(List<String> textList, Set<String> availablePlaceholders) {
      Set<String> usedPlaceholders = new HashSet();
      if (textList != null && !textList.isEmpty()) {
         Iterator var4 = textList.iterator();

         while(var4.hasNext()) {
            String text = (String)var4.next();
            Iterator var6 = availablePlaceholders.iterator();

            while(var6.hasNext()) {
               String placeholder = (String)var6.next();
               if (text.contains("{" + placeholder + "}")) {
                  usedPlaceholders.add(placeholder);
               }
            }
         }

         return usedPlaceholders;
      } else {
         return usedPlaceholders;
      }
   }

   private boolean evaluateButtonCondition(GuiButton button, Player player) {
      String condition = button.getCondition();
      if (condition != null && !condition.isEmpty()) {
         byte var5 = -1;
         switch(condition.hashCode()) {
         case -792184729:
            if (condition.equals("sell_integration")) {
               var5 = 0;
            }
            break;
         case 1514710821:
            if (condition.equals("no_sell_integration")) {
               var5 = 1;
            }
         }

         switch(var5) {
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

   private String getAnyActionFromButton(GuiButton button) {
      String action = button.getDefaultAction();
      if (action != null && !action.isEmpty()) {
         return action;
      } else {
         action = button.getAction("left_click");
         if (action != null && !action.isEmpty()) {
            return action;
         } else {
            action = button.getAction("right_click");
            return action != null && !action.isEmpty() ? action : null;
         }
      }
   }
}
