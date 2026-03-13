package github.nighter.smartspawner.spawner.properties;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.hologram.SpawnerHologram;
import github.nighter.smartspawner.spawner.lootgen.loot.EntityLootConfig;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import github.nighter.smartspawner.spawner.sell.SellResult;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SpawnerData {
   private final SmartSpawner plugin;
   private String spawnerId;
   private final Location spawnerLocation;
   private final ReentrantLock inventoryLock = new ReentrantLock();
   private final ReentrantLock lootGenerationLock = new ReentrantLock();
   private final ReentrantLock sellLock = new ReentrantLock();
   private final ReentrantLock dataLock = new ReentrantLock();
   private int baseMaxStoredExp;
   private int baseMaxStoragePages;
   private int baseMinMobs;
   private int baseMaxMobs;
   private Integer spawnerExp;
   private Boolean spawnerActive;
   private Integer spawnerRange;
   private AtomicBoolean spawnerStop;
   private Boolean isAtCapacity;
   private Long lastSpawnTime;
   private long spawnDelay;
   private EntityType entityType;
   private EntityLootConfig lootConfig;
   private Material spawnedItemMaterial;
   private int maxStoragePages;
   private int maxSpawnerLootSlots;
   private int maxStoredExp;
   private int minMobs;
   private int maxMobs;
   private int stackSize;
   private int maxStackSize;
   private VirtualInventory virtualInventory;
   private final Set<Material> filteredItems = new HashSet();
   private final AtomicBoolean interacted = new AtomicBoolean(false);
   private String lastInteractedPlayer;
   private SellResult lastSellResult;
   private boolean lastSellProcessed;
   private volatile double accumulatedSellValue;
   private volatile boolean sellValueDirty;
   private SpawnerHologram hologram;
   private long cachedSpawnDelay;
   private Material preferredSortItem;
   private volatile List<ItemStack> preGeneratedItems;
   private volatile int preGeneratedExperience;
   private volatile boolean isPreGenerating;
   private volatile Boolean cachedHasNoLoot = null;

   public SpawnerData(String id, Location location, EntityType type, SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerId = id;
      this.spawnerLocation = location;
      this.entityType = type;
      this.spawnedItemMaterial = null;
      this.initializeDefaults();
      this.loadConfigurationValues();
      this.calculateStackBasedValues();
      this.initializeComponents();
   }

   public SpawnerData(String id, Location location, Material itemMaterial, SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerId = id;
      this.spawnerLocation = location;
      this.entityType = EntityType.ITEM;
      this.spawnedItemMaterial = itemMaterial;
      this.initializeDefaults();
      this.loadConfigurationValues();
      this.calculateStackBasedValues();
      this.initializeComponents();
   }

   private void initializeDefaults() {
      this.spawnerExp = 0;
      this.spawnerActive = true;
      this.spawnerStop = new AtomicBoolean(true);
      this.isAtCapacity = false;
      this.stackSize = 1;
      this.lastSpawnTime = System.currentTimeMillis();
      this.preferredSortItem = null;
      this.accumulatedSellValue = 0.0D;
      this.sellValueDirty = true;
   }

   public void loadConfigurationValues() {
      this.baseMaxStoredExp = this.plugin.getConfig().getInt("spawner_properties.default.max_stored_exp", 1000);
      this.baseMaxStoragePages = this.plugin.getConfig().getInt("spawner_properties.default.max_storage_pages", 1);
      this.baseMinMobs = this.plugin.getConfig().getInt("spawner_properties.default.min_mobs", 1);
      this.baseMaxMobs = this.plugin.getConfig().getInt("spawner_properties.default.max_mobs", 4);
      this.maxStackSize = this.plugin.getConfig().getInt("spawner_properties.default.max_stack_size", 1000);
      this.spawnDelay = this.plugin.getTimeFromConfig("spawner_properties.default.delay", "25s");
      this.cachedSpawnDelay = (this.spawnDelay + 20L) * 50L;
      this.spawnerRange = this.plugin.getConfig().getInt("spawner_properties.default.range", 16);
      if (this.isItemSpawner() && this.spawnedItemMaterial != null) {
         this.lootConfig = this.plugin.getItemSpawnerSettingsConfig().getLootConfig(this.spawnedItemMaterial);
      } else {
         this.lootConfig = this.plugin.getSpawnerSettingsConfig().getLootConfig(this.entityType);
      }

   }

   public void recalculateAfterConfigReload() {
      this.calculateStackBasedValues();
      if (this.virtualInventory != null && this.virtualInventory.getMaxSlots() != this.maxSpawnerLootSlots) {
         this.recreateVirtualInventory();
      }

      this.sellValueDirty = true;
      this.updateHologramData();
      if (this.plugin.getSpawnerMenuUI() != null) {
         this.plugin.getSpawnerMenuUI().invalidateSpawnerCache(this.spawnerId);
      }

      if (this.plugin.getSpawnerMenuFormUI() != null) {
         this.plugin.getSpawnerMenuFormUI().invalidateSpawnerCache(this.spawnerId);
      }

   }

   public void recalculateAfterAPIModification() {
      this.calculateStackBasedValues();
      if (this.virtualInventory != null && this.virtualInventory.getMaxSlots() != this.maxSpawnerLootSlots) {
         this.recreateVirtualInventory();
      }

      this.updateHologramData();
      if (this.plugin.getSpawnerMenuUI() != null) {
         this.plugin.getSpawnerMenuUI().invalidateSpawnerCache(this.spawnerId);
      }

      if (this.plugin.getSpawnerMenuFormUI() != null) {
         this.plugin.getSpawnerMenuFormUI().invalidateSpawnerCache(this.spawnerId);
      }

   }

   private void calculateStackBasedValues() {
      this.maxStoredExp = this.baseMaxStoredExp * this.stackSize;
      this.maxStoragePages = this.baseMaxStoragePages * this.stackSize;
      this.maxSpawnerLootSlots = this.maxStoragePages * 45;
      this.minMobs = this.baseMinMobs * this.stackSize;
      this.maxMobs = this.baseMaxMobs * this.stackSize;
      this.spawnerExp = Math.min(this.spawnerExp, this.maxStoredExp);
   }

   public void setSpawnDelay(long baseSpawnerDelay) {
      this.spawnDelay = baseSpawnerDelay > 0L ? baseSpawnerDelay : 500L;
      if (baseSpawnerDelay <= 0L) {
         this.plugin.getLogger().warning("Invalid spawner delay value. Setting to default: 500 ticks (25s)");
      }

   }

   public void setSpawnDelayFromConfig() {
      long delay = this.plugin.getTimeFromConfig("spawner_properties.default.delay", "25s");
      if (delay <= 0L) {
         this.plugin.getLogger().warning("Invalid spawner delay value in config. Setting to default: 500 ticks (25s)");
         delay = 500L;
      }

      this.setSpawnDelay(delay);
   }

   private void initializeComponents() {
      this.virtualInventory = new VirtualInventory(this.maxSpawnerLootSlots);
      if (this.plugin.getConfig().getBoolean("hologram.enabled", false)) {
         this.createHologram();
      }

      if (this.preferredSortItem == null && this.lootConfig != null && this.lootConfig.getAllItems() != null) {
         List<LootItem> lootItems = this.lootConfig.getAllItems();
         if (!lootItems.isEmpty()) {
            List<Material> sortedLoot = lootItems.stream().map(LootItem::material).distinct().sorted(Comparator.comparing(Enum::name)).toList();
            if (!sortedLoot.isEmpty()) {
               this.preferredSortItem = (Material)sortedLoot.getFirst();
            }
         }
      }

      this.virtualInventory.sortItems(this.preferredSortItem);
   }

   private void createHologram() {
      this.hologram = new SpawnerHologram(this.spawnerLocation);
      this.hologram.createHologram();
      this.updateHologramData();
   }

   public void setStackSize(int stackSize) {
      this.setStackSize(stackSize, true);
   }

   public void setStackSize(int stackSize, boolean restartHopper) {
      this.dataLock.lock();

      try {
         this.inventoryLock.lock();

         try {
            this.updateStackSize(stackSize, restartHopper);
         } finally {
            this.inventoryLock.unlock();
         }
      } finally {
         this.dataLock.unlock();
      }

   }

   private void updateStackSize(int newStackSize, boolean restartHopper) {
      if (newStackSize <= 0) {
         this.stackSize = 1;
         this.plugin.getLogger().warning("Invalid stack size. Setting to 1");
      } else if (newStackSize > this.maxStackSize) {
         this.stackSize = this.maxStackSize;
         this.plugin.getLogger().warning("Stack size exceeds maximum. Setting to " + this.stackSize);
      } else {
         this.stackSize = newStackSize;
         this.calculateStackBasedValues();
         this.virtualInventory.resize(this.maxSpawnerLootSlots);
         this.lastSpawnTime = System.currentTimeMillis();
         this.updateHologramData();
         if (this.plugin.getSpawnerMenuUI() != null) {
            this.plugin.getSpawnerMenuUI().invalidateSpawnerCache(this.spawnerId);
         }

         if (this.plugin.getSpawnerMenuFormUI() != null) {
            this.plugin.getSpawnerMenuFormUI().invalidateSpawnerCache(this.spawnerId);
         }

      }
   }

   private void recreateVirtualInventory() {
      if (this.virtualInventory != null) {
         this.virtualInventory.resize(this.maxSpawnerLootSlots);
      }
   }

   public void setSpawnerExp(int exp) {
      this.spawnerExp = Math.min(Math.max(0, exp), this.maxStoredExp);
      this.updateHologramData();
      if (this.plugin.getSpawnerMenuUI() != null) {
         this.plugin.getSpawnerMenuUI().invalidateSpawnerCache(this.spawnerId);
      }

      if (this.plugin.getSpawnerMenuFormUI() != null) {
         this.plugin.getSpawnerMenuFormUI().invalidateSpawnerCache(this.spawnerId);
      }

   }

   public void setSpawnerExpData(int exp) {
      this.spawnerExp = exp;
   }

   public void updateHologramData() {
      if (this.hologram != null) {
         this.hologram.updateData(this.stackSize, this.entityType, this.spawnerExp, this.maxStoredExp, this.virtualInventory.getUsedSlots(), this.maxSpawnerLootSlots);
      }

   }

   public void reloadHologramData() {
      if (this.hologram != null) {
         this.hologram.remove();
         this.createHologram();
      }

   }

   public void refreshHologram() {
      if (this.plugin.getConfig().getBoolean("hologram.enabled", false)) {
         if (this.hologram == null) {
            this.createHologram();
         }
      } else if (this.hologram != null) {
         this.removeHologram();
      }

   }

   public void removeHologram() {
      if (this.hologram != null) {
         this.hologram.remove();
         this.hologram = null;
      }

   }

   public boolean isCompletelyFull() {
      return this.virtualInventory.getUsedSlots() >= this.maxSpawnerLootSlots && this.spawnerExp >= this.maxStoredExp;
   }

   public boolean updateCapacityStatus() {
      boolean newStatus = this.isCompletelyFull();
      if (newStatus != this.isAtCapacity) {
         this.isAtCapacity = newStatus;
         return true;
      } else {
         return false;
      }
   }

   public void setEntityType(EntityType newType) {
      this.entityType = newType;
      this.lootConfig = this.plugin.getSpawnerSettingsConfig().getLootConfig(newType);
      this.sellValueDirty = true;
      this.updateHologramData();
   }

   public boolean toggleItemFilter(Material material) {
      boolean wasFiltered = this.filteredItems.contains(material);
      if (wasFiltered) {
         this.filteredItems.remove(material);
      } else {
         this.filteredItems.add(material);
      }

      return !wasFiltered;
   }

   public List<LootItem> getValidLootItems() {
      return this.lootConfig == null ? Collections.emptyList() : (List)this.lootConfig.getAllItems().stream().filter(this::isLootItemValid).collect(Collectors.toList());
   }

   private boolean isLootItemValid(LootItem item) {
      ItemStack example = item.createItemStack(new Random());
      return example != null && !this.filteredItems.contains(example.getType());
   }

   public int getEntityExperienceValue() {
      return this.lootConfig != null ? this.lootConfig.experience() : 0;
   }

   public boolean hasNoLootOrExperience() {
      if (this.cachedHasNoLoot != null) {
         return this.cachedHasNoLoot;
      } else {
         boolean result = this.lootConfig == null || this.lootConfig.experience() == 0 && this.getValidLootItems().isEmpty();
         this.cachedHasNoLoot = result;
         return result;
      }
   }

   public void setLootConfig() {
      if (this.isItemSpawner() && this.spawnedItemMaterial != null) {
         this.lootConfig = this.plugin.getItemSpawnerSettingsConfig().getLootConfig(this.spawnedItemMaterial);
      } else {
         this.lootConfig = this.plugin.getSpawnerSettingsConfig().getLootConfig(this.entityType);
      }

      this.sellValueDirty = true;
      this.cachedHasNoLoot = null;
   }

   public void setLastSellResult(SellResult sellResult) {
      this.lastSellResult = sellResult;
      this.lastSellProcessed = false;
   }

   public void markLastSellAsProcessed() {
      this.lastSellProcessed = true;
   }

   public boolean isInteracted() {
      return this.interacted.get();
   }

   public void markInteracted() {
      this.interacted.compareAndSet(false, true);
   }

   public void clearInteracted() {
      this.interacted.compareAndSet(true, false);
   }

   public void updateLastInteractedPlayer(String playerName) {
      this.lastInteractedPlayer = playerName;
      if (System.currentTimeMillis() - this.lastSpawnTime < 50L) {
         this.markInteracted();
      }

   }

   public void markSellValueDirty() {
      this.sellValueDirty = true;
   }

   public void incrementSellValue(Map<VirtualInventory.ItemSignature, Long> itemsAdded, Map<String, Double> priceCache) {
      if (itemsAdded != null && !itemsAdded.isEmpty()) {
         double addedValue = 0.0D;
         Iterator var5 = itemsAdded.entrySet().iterator();

         while(var5.hasNext()) {
            Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var5.next();
            ItemStack template = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef();
            long amount = (Long)entry.getValue();
            double itemPrice = this.findItemPrice(template, priceCache);
            if (itemPrice > 0.0D) {
               addedValue += itemPrice * (double)amount;
            }
         }

         this.accumulatedSellValue += addedValue;
         this.sellValueDirty = false;
      }
   }

   public void decrementSellValue(List<ItemStack> itemsRemoved, Map<String, Double> priceCache) {
      if (itemsRemoved != null && !itemsRemoved.isEmpty()) {
         Map<VirtualInventory.ItemSignature, Long> consolidated = new HashMap();
         Iterator var4 = itemsRemoved.iterator();

         while(var4.hasNext()) {
            ItemStack item = (ItemStack)var4.next();
            if (item != null && item.getAmount() > 0) {
               VirtualInventory.ItemSignature sig = VirtualInventory.getSignature(item);
               consolidated.merge(sig, (long)item.getAmount(), Long::sum);
            }
         }

         double removedValue = 0.0D;
         Iterator var14 = consolidated.entrySet().iterator();

         while(var14.hasNext()) {
            Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var14.next();
            ItemStack template = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef();
            long amount = (Long)entry.getValue();
            double itemPrice = this.findItemPrice(template, priceCache);
            if (itemPrice > 0.0D) {
               removedValue += itemPrice * (double)amount;
            }
         }

         this.accumulatedSellValue = Math.max(0.0D, this.accumulatedSellValue - removedValue);
      }
   }

   public void recalculateSellValue() {
      if (this.lootConfig == null) {
         this.accumulatedSellValue = 0.0D;
         this.sellValueDirty = false;
      } else {
         Map<String, Double> priceCache = this.createPriceCache();
         Map<VirtualInventory.ItemSignature, Long> items = this.virtualInventory.getConsolidatedItems();
         double totalValue = 0.0D;
         Iterator var5 = items.entrySet().iterator();

         while(var5.hasNext()) {
            Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var5.next();
            ItemStack template = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef();
            long amount = (Long)entry.getValue();
            double itemPrice = this.findItemPrice(template, priceCache);
            if (itemPrice > 0.0D) {
               totalValue += itemPrice * (double)amount;
            }
         }

         this.accumulatedSellValue = totalValue;
         this.sellValueDirty = false;
      }
   }

   public Map<String, Double> createPriceCache() {
      if (this.lootConfig == null) {
         return new HashMap();
      } else {
         Map<String, Double> cache = new HashMap();
         List<LootItem> allLootItems = this.lootConfig.getAllItems();
         Iterator var3 = allLootItems.iterator();

         while(var3.hasNext()) {
            LootItem lootItem = (LootItem)var3.next();
            if (lootItem.sellPrice() > 0.0D) {
               ItemStack template = lootItem.createItemStack(new Random());
               if (template != null) {
                  String key = this.createItemKey(template);
                  cache.put(key, lootItem.sellPrice());
               }
            }
         }

         return cache;
      }
   }

   private double findItemPrice(ItemStack item, Map<String, Double> priceCache) {
      if (item != null && priceCache != null) {
         String itemKey = this.createItemKey(item);
         Double price = (Double)priceCache.get(itemKey);
         return price != null ? price : 0.0D;
      } else {
         return 0.0D;
      }
   }

   private String createItemKey(ItemStack item) {
      if (item == null) {
         return "null";
      } else {
         StringBuilder key = new StringBuilder();
         key.append(item.getType().name());
         if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            key.append("_enchants:");
            item.getItemMeta().getEnchants().entrySet().stream().sorted(Entry.comparingByKey(Comparator.comparing((enchantment) -> {
               return enchantment.getKey().toString();
            }))).forEach((entry) -> {
               key.append(((Enchantment)entry.getKey()).getKey()).append(":").append(entry.getValue()).append(",");
            });
         }

         if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
            key.append("_cmd:").append(item.getItemMeta().getCustomModelData());
         }

         if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            key.append("_name:").append(item.getItemMeta().getDisplayName());
         }

         return key.toString();
      }
   }

   public void addItemsAndUpdateSellValue(List<ItemStack> items) {
      if (items != null && !items.isEmpty()) {
         this.inventoryLock.lock();

         try {
            Map<VirtualInventory.ItemSignature, Long> itemsToAdd = new HashMap();
            Iterator var3 = items.iterator();

            while(var3.hasNext()) {
               ItemStack item = (ItemStack)var3.next();
               if (item != null && item.getAmount() > 0) {
                  VirtualInventory.ItemSignature sig = VirtualInventory.getSignature(item);
                  itemsToAdd.merge(sig, (long)item.getAmount(), Long::sum);
               }
            }

            this.virtualInventory.addItems(items);
            if (!this.sellValueDirty) {
               Map<String, Double> priceCache = this.createPriceCache();
               this.incrementSellValue(itemsToAdd, priceCache);
            }
         } finally {
            this.inventoryLock.unlock();
         }

      }
   }

   public boolean removeItemsAndUpdateSellValue(List<ItemStack> items) {
      if (items != null && !items.isEmpty()) {
         this.inventoryLock.lock();

         boolean var7;
         try {
            boolean removed = this.virtualInventory.removeItems(items);
            if (removed && !this.sellValueDirty) {
               Map<String, Double> priceCache = this.createPriceCache();
               this.decrementSellValue(items, priceCache);
            }

            var7 = removed;
         } finally {
            this.inventoryLock.unlock();
         }

         return var7;
      } else {
         return true;
      }
   }

   public synchronized void storePreGeneratedLoot(List<ItemStack> items, int experience) {
      this.preGeneratedItems = items;
      this.preGeneratedExperience = experience;
   }

   public synchronized List<ItemStack> getAndClearPreGeneratedItems() {
      List<ItemStack> items = this.preGeneratedItems;
      this.preGeneratedItems = null;
      return items;
   }

   public synchronized int getAndClearPreGeneratedExperience() {
      int exp = this.preGeneratedExperience;
      this.preGeneratedExperience = 0;
      return exp;
   }

   public synchronized boolean hasPreGeneratedLoot() {
      return this.preGeneratedItems != null && !this.preGeneratedItems.isEmpty() || this.preGeneratedExperience > 0;
   }

   public synchronized void setPreGenerating(boolean generating) {
      this.isPreGenerating = generating;
   }

   public synchronized boolean isPreGenerating() {
      return this.isPreGenerating;
   }

   public synchronized void clearPreGeneratedLoot() {
      this.preGeneratedItems = null;
      this.preGeneratedExperience = 0;
      this.isPreGenerating = false;
   }

   public boolean isItemSpawner() {
      return this.entityType == EntityType.ITEM && this.spawnedItemMaterial != null;
   }

   @Generated
   public SmartSpawner getPlugin() {
      return this.plugin;
   }

   @Generated
   public String getSpawnerId() {
      return this.spawnerId;
   }

   @Generated
   public void setSpawnerId(String spawnerId) {
      this.spawnerId = spawnerId;
   }

   @Generated
   public Location getSpawnerLocation() {
      return this.spawnerLocation;
   }

   @Generated
   public ReentrantLock getInventoryLock() {
      return this.inventoryLock;
   }

   @Generated
   public ReentrantLock getLootGenerationLock() {
      return this.lootGenerationLock;
   }

   @Generated
   public ReentrantLock getSellLock() {
      return this.sellLock;
   }

   @Generated
   public ReentrantLock getDataLock() {
      return this.dataLock;
   }

   @Generated
   public int getBaseMaxStoredExp() {
      return this.baseMaxStoredExp;
   }

   @Generated
   public void setBaseMaxStoredExp(int baseMaxStoredExp) {
      this.baseMaxStoredExp = baseMaxStoredExp;
   }

   @Generated
   public int getBaseMaxStoragePages() {
      return this.baseMaxStoragePages;
   }

   @Generated
   public void setBaseMaxStoragePages(int baseMaxStoragePages) {
      this.baseMaxStoragePages = baseMaxStoragePages;
   }

   @Generated
   public int getBaseMinMobs() {
      return this.baseMinMobs;
   }

   @Generated
   public void setBaseMinMobs(int baseMinMobs) {
      this.baseMinMobs = baseMinMobs;
   }

   @Generated
   public int getBaseMaxMobs() {
      return this.baseMaxMobs;
   }

   @Generated
   public void setBaseMaxMobs(int baseMaxMobs) {
      this.baseMaxMobs = baseMaxMobs;
   }

   @Generated
   public Integer getSpawnerExp() {
      return this.spawnerExp;
   }

   @Generated
   public Boolean getSpawnerActive() {
      return this.spawnerActive;
   }

   @Generated
   public void setSpawnerActive(Boolean spawnerActive) {
      this.spawnerActive = spawnerActive;
   }

   @Generated
   public Integer getSpawnerRange() {
      return this.spawnerRange;
   }

   @Generated
   public void setSpawnerRange(Integer spawnerRange) {
      this.spawnerRange = spawnerRange;
   }

   @Generated
   public AtomicBoolean getSpawnerStop() {
      return this.spawnerStop;
   }

   @Generated
   public Boolean getIsAtCapacity() {
      return this.isAtCapacity;
   }

   @Generated
   public void setIsAtCapacity(Boolean isAtCapacity) {
      this.isAtCapacity = isAtCapacity;
   }

   @Generated
   public Long getLastSpawnTime() {
      return this.lastSpawnTime;
   }

   @Generated
   public void setLastSpawnTime(Long lastSpawnTime) {
      this.lastSpawnTime = lastSpawnTime;
   }

   @Generated
   public long getSpawnDelay() {
      return this.spawnDelay;
   }

   @Generated
   public EntityType getEntityType() {
      return this.entityType;
   }

   @Generated
   public EntityLootConfig getLootConfig() {
      return this.lootConfig;
   }

   @Generated
   public void setLootConfig(EntityLootConfig lootConfig) {
      this.lootConfig = lootConfig;
   }

   @Generated
   public Material getSpawnedItemMaterial() {
      return this.spawnedItemMaterial;
   }

   @Generated
   public void setSpawnedItemMaterial(Material spawnedItemMaterial) {
      this.spawnedItemMaterial = spawnedItemMaterial;
   }

   @Generated
   public int getMaxStoragePages() {
      return this.maxStoragePages;
   }

   @Generated
   public int getMaxSpawnerLootSlots() {
      return this.maxSpawnerLootSlots;
   }

   @Generated
   public void setMaxSpawnerLootSlots(int maxSpawnerLootSlots) {
      this.maxSpawnerLootSlots = maxSpawnerLootSlots;
   }

   @Generated
   public int getMaxStoredExp() {
      return this.maxStoredExp;
   }

   @Generated
   public void setMaxStoredExp(int maxStoredExp) {
      this.maxStoredExp = maxStoredExp;
   }

   @Generated
   public int getMinMobs() {
      return this.minMobs;
   }

   @Generated
   public void setMinMobs(int minMobs) {
      this.minMobs = minMobs;
   }

   @Generated
   public int getMaxMobs() {
      return this.maxMobs;
   }

   @Generated
   public void setMaxMobs(int maxMobs) {
      this.maxMobs = maxMobs;
   }

   @Generated
   public int getStackSize() {
      return this.stackSize;
   }

   @Generated
   public int getMaxStackSize() {
      return this.maxStackSize;
   }

   @Generated
   public void setMaxStackSize(int maxStackSize) {
      this.maxStackSize = maxStackSize;
   }

   @Generated
   public VirtualInventory getVirtualInventory() {
      return this.virtualInventory;
   }

   @Generated
   public void setVirtualInventory(VirtualInventory virtualInventory) {
      this.virtualInventory = virtualInventory;
   }

   @Generated
   public Set<Material> getFilteredItems() {
      return this.filteredItems;
   }

   @Generated
   public String getLastInteractedPlayer() {
      return this.lastInteractedPlayer;
   }

   @Generated
   public void setLastInteractedPlayer(String lastInteractedPlayer) {
      this.lastInteractedPlayer = lastInteractedPlayer;
   }

   @Generated
   public SellResult getLastSellResult() {
      return this.lastSellResult;
   }

   @Generated
   public boolean isLastSellProcessed() {
      return this.lastSellProcessed;
   }

   @Generated
   public double getAccumulatedSellValue() {
      return this.accumulatedSellValue;
   }

   @Generated
   public boolean isSellValueDirty() {
      return this.sellValueDirty;
   }

   @Generated
   public long getCachedSpawnDelay() {
      return this.cachedSpawnDelay;
   }

   @Generated
   public void setCachedSpawnDelay(long cachedSpawnDelay) {
      this.cachedSpawnDelay = cachedSpawnDelay;
   }

   @Generated
   public Material getPreferredSortItem() {
      return this.preferredSortItem;
   }

   @Generated
   public void setPreferredSortItem(Material preferredSortItem) {
      this.preferredSortItem = preferredSortItem;
   }
}
