package github.nighter.smartspawner.spawner.item;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.nms.VersionInitializer;
import github.nighter.smartspawner.spawner.lootgen.loot.EntityLootConfig;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerItemFactory {
   private static final long CACHE_EXPIRY_TIME_MS;
   private static final int MAX_CACHE_SIZE = 100;
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private static NamespacedKey VANILLA_SPAWNER_KEY;
   private final Map<EntityType, ItemStack> spawnerItemCache = new HashMap();
   private final Map<EntityType, Long> cacheTimestamps = new HashMap();
   private long lastCacheCleanup = System.currentTimeMillis();

   public SpawnerItemFactory(SmartSpawner plugin) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      VANILLA_SPAWNER_KEY = new NamespacedKey(plugin, "vanilla_spawner");
   }

   public void reload() {
      this.clearAllCaches();
   }

   public void clearAllCaches() {
      this.spawnerItemCache.clear();
      this.cacheTimestamps.clear();
      this.lastCacheCleanup = System.currentTimeMillis();
   }

   private void cleanupCacheIfNeeded() {
      long currentTime = System.currentTimeMillis();
      if (currentTime - this.lastCacheCleanup >= TimeUnit.MINUTES.toMillis(1L)) {
         this.lastCacheCleanup = currentTime;
         Iterator iterator = this.cacheTimestamps.entrySet().iterator();

         while(iterator.hasNext()) {
            Entry<EntityType, Long> entry = (Entry)iterator.next();
            if (currentTime - (Long)entry.getValue() > CACHE_EXPIRY_TIME_MS) {
               EntityType type = (EntityType)entry.getKey();
               this.spawnerItemCache.remove(type);
               iterator.remove();
            }
         }

      }
   }

   public ItemStack createSmartSpawnerItem(EntityType entityType) {
      return this.createSmartSpawnerItem(entityType, 1);
   }

   public ItemStack createSmartSpawnerItem(EntityType entityType, int amount) {
      this.cleanupCacheIfNeeded();
      ItemStack spawner;
      if (amount == 1) {
         spawner = (ItemStack)this.spawnerItemCache.get(entityType);
         if (spawner != null) {
            return spawner.clone();
         }
      }

      spawner = new ItemStack(Material.SPAWNER, amount);
      ItemMeta meta = spawner.getItemMeta();
      if (meta != null && entityType != null && entityType != EntityType.UNKNOWN) {
         if (meta instanceof BlockStateMeta) {
            BlockStateMeta blockMeta = (BlockStateMeta)meta;
            BlockState blockState = blockMeta.getBlockState();
            if (blockState instanceof CreatureSpawner) {
               CreatureSpawner cs = (CreatureSpawner)blockState;
               cs.setSpawnedType(entityType);
               blockMeta.setBlockState(cs);
            }
         }

         String entityTypeName = this.languageManager.getFormattedMobName(entityType);
         String entityTypeNameSmallCaps = this.languageManager.getSmallCaps(entityTypeName);
         EntityLootConfig lootConfig = this.plugin.getSpawnerSettingsConfig().getLootConfig(entityType);
         List<LootItem> lootItems = lootConfig != null ? lootConfig.getAllItems() : Collections.emptyList();
         Map<String, String> placeholders = new HashMap();
         placeholders.put("entity", entityTypeName);
         placeholders.put("ᴇɴᴛɪᴛʏ", entityTypeNameSmallCaps);
         placeholders.put("exp", String.valueOf(lootConfig != null ? lootConfig.experience() : 0));
         List<LootItem> sortedLootItems = new ArrayList(lootItems);
         sortedLootItems.sort(Comparator.comparing((itemx) -> {
            return itemx.material().name();
         }));
         String lootFormat;
         if (sortedLootItems.isEmpty()) {
            placeholders.put("loot_items", this.languageManager.getItemName("custom_item.spawner.loot_items_empty", placeholders));
         } else {
            lootFormat = this.languageManager.getItemName("custom_item.spawner.loot_items", placeholders);
            StringBuilder lootItemsBuilder = new StringBuilder();
            Iterator var13 = sortedLootItems.iterator();

            while(var13.hasNext()) {
               LootItem item = (LootItem)var13.next();
               String itemName = this.languageManager.getVanillaItemName(item.material());
               String itemNameSmallCaps = this.languageManager.getSmallCaps(itemName);
               String amountRange = item.minAmount() == item.maxAmount() ? String.valueOf(item.minAmount()) : item.minAmount() + "-" + item.maxAmount();
               String chance = String.format("%.1f", item.chance());
               Map<String, String> itemPlaceholders = new HashMap(placeholders);
               itemPlaceholders.put("item_name", itemName);
               itemPlaceholders.put("ɪᴛᴇᴍ_ɴᴀᴍᴇ", itemNameSmallCaps);
               itemPlaceholders.put("amount", amountRange);
               itemPlaceholders.put("chance", chance);
               String formattedItem = this.languageManager.applyPlaceholdersAndColors(lootFormat, itemPlaceholders);
               lootItemsBuilder.append(formattedItem).append("\n");
            }

            if (!lootItemsBuilder.isEmpty()) {
               lootItemsBuilder.setLength(lootItemsBuilder.length() - 1);
            }

            placeholders.put("loot_items", lootItemsBuilder.toString());
         }

         lootFormat = this.languageManager.getItemName("custom_item.spawner.name", placeholders);
         meta.setDisplayName(lootFormat);
         List<String> lore = this.languageManager.getItemLoreWithMultilinePlaceholders("custom_item.spawner.lore", placeholders);
         if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore);
         }

         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         spawner.setItemMeta(meta);
      }

      VersionInitializer.hideTooltip(spawner);
      if (amount == 1) {
         this.spawnerItemCache.put(entityType, spawner.clone());
         this.cacheTimestamps.put(entityType, System.currentTimeMillis());
         if (this.spawnerItemCache.size() > 100) {
            EntityType oldestEntity = null;
            long oldestTime = Long.MAX_VALUE;
            Iterator var26 = this.cacheTimestamps.entrySet().iterator();

            while(var26.hasNext()) {
               Entry<EntityType, Long> entry = (Entry)var26.next();
               if ((Long)entry.getValue() < oldestTime) {
                  oldestTime = (Long)entry.getValue();
                  oldestEntity = (EntityType)entry.getKey();
               }
            }

            if (oldestEntity != null) {
               this.spawnerItemCache.remove(oldestEntity);
               this.cacheTimestamps.remove(oldestEntity);
            }
         }
      }

      return spawner;
   }

   public ItemStack createVanillaSpawnerItem(EntityType entityType) {
      return this.createVanillaSpawnerItem(entityType, 1);
   }

   public ItemStack createVanillaSpawnerItem(EntityType entityType, int amount) {
      ItemStack spawner = new ItemStack(Material.SPAWNER, amount);
      ItemMeta meta = spawner.getItemMeta();
      if (meta != null && entityType != null && entityType != EntityType.UNKNOWN) {
         if (meta instanceof BlockStateMeta) {
            BlockStateMeta blockMeta = (BlockStateMeta)meta;
            BlockState blockState = blockMeta.getBlockState();
            if (blockState instanceof CreatureSpawner) {
               CreatureSpawner cs = (CreatureSpawner)blockState;
               cs.setSpawnedType(entityType);
               blockMeta.setBlockState(cs);
            }
         }

         String entityTypeName = this.languageManager.getFormattedMobName(entityType);
         Map<String, String> placeholders = new HashMap();
         placeholders.put("entity", entityTypeName);
         placeholders.put("ᴇɴᴛɪᴛʏ", this.languageManager.getSmallCaps(entityTypeName));
         String displayName = this.languageManager.getItemName("custom_item.vanilla_spawner.name", placeholders);
         if (displayName != null && !displayName.isEmpty() && !displayName.equals("custom_item.vanilla_spawner.name")) {
            meta.setDisplayName(displayName);
         }

         List<String> lore = this.languageManager.getItemLoreWithMultilinePlaceholders("custom_item.vanilla_spawner.lore", placeholders);
         if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore);
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
            VersionInitializer.hideTooltip(spawner);
         }

         meta.getPersistentDataContainer().set(VANILLA_SPAWNER_KEY, PersistentDataType.BOOLEAN, true);
         spawner.setItemMeta(meta);
      }

      return spawner;
   }

   public ItemStack createItemSpawnerItem(Material itemMaterial) {
      return this.createItemSpawnerItem(itemMaterial, 1);
   }

   public ItemStack createItemSpawnerItem(Material itemMaterial, int amount) {
      ItemStack spawner = new ItemStack(Material.SPAWNER, amount);
      ItemMeta meta = spawner.getItemMeta();
      if (meta != null && itemMaterial != null) {
         if (meta instanceof BlockStateMeta) {
            BlockStateMeta blockMeta = (BlockStateMeta)meta;
            BlockState blockState = blockMeta.getBlockState();
            if (blockState instanceof CreatureSpawner) {
               CreatureSpawner cs = (CreatureSpawner)blockState;
               cs.setSpawnedType(EntityType.ITEM);
               blockMeta.setBlockState(cs);
            }
         }

         String itemName = this.languageManager.getVanillaItemName(itemMaterial);
         String itemNameSmallCaps = this.languageManager.getSmallCaps(itemName);
         EntityLootConfig lootConfig = this.plugin.getItemSpawnerSettingsConfig().getLootConfig(itemMaterial);
         List<LootItem> lootItems = lootConfig != null ? lootConfig.getAllItems() : Collections.emptyList();
         Map<String, String> placeholders = new HashMap();
         placeholders.put("entity", itemName);
         placeholders.put("ᴇɴᴛɪᴛʏ", itemNameSmallCaps);
         placeholders.put("exp", String.valueOf(lootConfig != null ? lootConfig.experience() : 0));
         List<LootItem> sortedLootItems = new ArrayList(lootItems);
         sortedLootItems.sort(Comparator.comparing((itemx) -> {
            return itemx.material().name();
         }));
         String displayName;
         if (sortedLootItems.isEmpty()) {
            placeholders.put("loot_items", this.languageManager.getItemName("custom_item.item_spawner.loot_items_empty", placeholders));
         } else {
            displayName = this.languageManager.getItemName("custom_item.item_spawner.loot_items", placeholders);
            StringBuilder lootItemsBuilder = new StringBuilder();
            Iterator var13 = sortedLootItems.iterator();

            while(var13.hasNext()) {
               LootItem item = (LootItem)var13.next();
               String lootItemName = this.languageManager.getVanillaItemName(item.material());
               String lootItemNameSmallCaps = this.languageManager.getSmallCaps(lootItemName);
               String amountRange = item.minAmount() == item.maxAmount() ? String.valueOf(item.minAmount()) : item.minAmount() + "-" + item.maxAmount();
               String chance = String.format("%.1f", item.chance());
               Map<String, String> itemPlaceholders = new HashMap(placeholders);
               itemPlaceholders.put("item_name", lootItemName);
               itemPlaceholders.put("ɪᴛᴇᴍ_ɴᴀᴍᴇ", lootItemNameSmallCaps);
               itemPlaceholders.put("amount", amountRange);
               itemPlaceholders.put("chance", chance);
               String formattedItem = this.languageManager.applyPlaceholdersAndColors(displayName, itemPlaceholders);
               lootItemsBuilder.append(formattedItem).append("\n");
            }

            if (!lootItemsBuilder.isEmpty()) {
               lootItemsBuilder.setLength(lootItemsBuilder.length() - 1);
            }

            placeholders.put("loot_items", lootItemsBuilder.toString());
         }

         displayName = this.languageManager.getItemName("custom_item.item_spawner.name", placeholders);
         if (displayName == null || displayName.isEmpty() || displayName.equals("custom_item.item_spawner.name")) {
            displayName = "§6" + itemName + " Spawner";
         }

         meta.setDisplayName(displayName);
         List<String> lore = this.languageManager.getItemLoreWithMultilinePlaceholders("custom_item.item_spawner.lore", placeholders);
         if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore);
         }

         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
         meta.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "item_spawner_material"), PersistentDataType.STRING, itemMaterial.name());
         spawner.setItemMeta(meta);
      }

      VersionInitializer.hideTooltip(spawner);
      return spawner;
   }

   static {
      CACHE_EXPIRY_TIME_MS = TimeUnit.MINUTES.toMillis(30L);
   }
}
