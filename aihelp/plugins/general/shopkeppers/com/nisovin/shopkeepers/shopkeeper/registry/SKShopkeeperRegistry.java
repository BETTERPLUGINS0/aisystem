package com.nisovin.shopkeepers.shopkeeper.registry;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeeperAddedEvent;
import com.nisovin.shopkeepers.api.events.ShopkeeperRemoveEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.ShopType;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperCreateException;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.activation.ShopkeeperChunkActivator;
import com.nisovin.shopkeepers.shopkeeper.player.AbstractPlayerShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.spawning.ShopkeeperSpawner;
import com.nisovin.shopkeepers.shopkeeper.ticking.ShopkeeperTicker;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.shopobjects.block.BlockShopObjectIds;
import com.nisovin.shopkeepers.shopobjects.entity.EntityShopObjectIds;
import com.nisovin.shopkeepers.storage.SKShopkeeperStorage;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKShopkeeperRegistry implements ShopkeeperRegistry {
   private final SKShopkeepersPlugin plugin;
   private final Map<UUID, AbstractShopkeeper> shopkeepersByUUID = new LinkedHashMap();
   private final Collection<? extends AbstractShopkeeper> allShopkeepersView;
   private final Map<Integer, AbstractShopkeeper> shopkeepersById;
   private final Set<AbstractShopkeeper> virtualShopkeepers;
   private final Collection<? extends AbstractShopkeeper> virtualShopkeepersView;
   private final ShopkeeperChunkMap chunkMap;
   private final ShopkeeperChunkMap.ChangeListener chunkMapChangeListener;
   private int playerShopCount;
   private final Set<? extends AbstractPlayerShopkeeper> allPlayerShopkeepersView;
   private final ShopObjectRegistry shopObjectRegistry;
   private final ShopkeeperTicker shopkeeperTicker;
   private final ShopkeeperSpawner shopkeeperSpawner;
   private final ShopkeeperChunkActivator chunkActivator;
   private final ActiveChunkQueries activeChunkQueries;

   public SKShopkeeperRegistry(SKShopkeepersPlugin plugin) {
      this.allShopkeepersView = Collections.unmodifiableCollection(this.shopkeepersByUUID.values());
      this.shopkeepersById = new HashMap();
      this.virtualShopkeepers = new LinkedHashSet();
      this.virtualShopkeepersView = Collections.unmodifiableCollection(this.virtualShopkeepers);
      this.chunkMapChangeListener = new ShopkeeperChunkMap.ChangeListener() {
         public void onShopkeeperAdded(AbstractShopkeeper shopkeeper, ChunkShopkeepers chunkShopkeepers) {
         }

         public void onShopkeeperRemoved(AbstractShopkeeper shopkeeper, ChunkShopkeepers chunkShopkeepers) {
         }

         public void onWorldAdded(WorldShopkeepers worldShopkeepers) {
         }

         public void onWorldRemoved(WorldShopkeepers worldShopkeepers) {
            Unsafe.assertNonNull(SKShopkeeperRegistry.this.shopkeeperSpawner);
            SKShopkeeperRegistry.this.shopkeeperSpawner.onShopkeeperWorldRemoved(worldShopkeepers.getWorldName());
         }

         public void onChunkAdded(ChunkShopkeepers chunkShopkeepers) {
            Unsafe.assertNonNull(SKShopkeeperRegistry.this.chunkActivator);
            SKShopkeeperRegistry.this.chunkActivator.onShopkeeperChunkAdded(chunkShopkeepers.getChunkCoords());
         }

         public void onChunkRemoved(ChunkShopkeepers chunkShopkeepers) {
            Unsafe.assertNonNull(SKShopkeeperRegistry.this.chunkActivator);
            SKShopkeeperRegistry.this.chunkActivator.onShopkeeperChunkRemoved(chunkShopkeepers.getChunkCoords());
         }
      };
      this.playerShopCount = 0;
      this.allPlayerShopkeepersView = new AbstractSet<AbstractPlayerShopkeeper>() {
         public Iterator<AbstractPlayerShopkeeper> iterator() {
            return this.isEmpty() ? Collections.emptyIterator() : ((SKShopkeeperRegistry)Unsafe.initialized(SKShopkeeperRegistry.this)).getAllShopkeepers().stream().filter((shopkeeper) -> {
               return shopkeeper instanceof PlayerShopkeeper;
            }).map((shopkeeper) -> {
               return (AbstractPlayerShopkeeper)shopkeeper;
            }).iterator();
         }

         public int size() {
            return SKShopkeeperRegistry.this.playerShopCount;
         }
      };
      this.shopObjectRegistry = new ShopObjectRegistry();
      this.plugin = plugin;
      this.chunkMap = new ShopkeeperChunkMap(this.chunkMapChangeListener);
      this.shopkeeperTicker = new ShopkeeperTicker(plugin);
      this.shopkeeperSpawner = new ShopkeeperSpawner(plugin, (SKShopkeeperRegistry)Unsafe.initialized(this));
      this.chunkActivator = new ShopkeeperChunkActivator(plugin, (SKShopkeeperRegistry)Unsafe.initialized(this), this.shopkeeperTicker, this.shopkeeperSpawner);
      this.activeChunkQueries = new ActiveChunkQueries(this.chunkMap, this.chunkActivator);
   }

   public void onEnable() {
      this.shopObjectRegistry.onEnable();
      this.chunkActivator.onEnable();
      this.shopkeeperSpawner.onEnable();
      this.shopkeeperTicker.onEnable();
   }

   public void onDisable() {
      this.unloadAllShopkeepers();

      assert this.getAllShopkeepers().isEmpty();

      this.ensureEmpty();
      this.shopkeeperTicker.onDisable();
      this.shopkeeperSpawner.onDisable();
      this.chunkActivator.onDisable();
      this.shopObjectRegistry.onDisable();
   }

   private void ensureEmpty() {
      if (!this.shopkeepersByUUID.isEmpty() || !this.shopkeepersById.isEmpty() || !this.virtualShopkeepers.isEmpty() || this.playerShopCount != 0) {
         Log.warning("Some shopkeepers were not properly unregistered!");
         this.shopkeepersByUUID.clear();
         this.shopkeepersById.clear();
         this.virtualShopkeepers.clear();
         this.playerShopCount = 0;
      }

      this.chunkMap.ensureEmpty();
   }

   public ShopkeeperSpawner getShopkeeperSpawner() {
      return this.shopkeeperSpawner;
   }

   public ShopkeeperChunkActivator getChunkActivator() {
      return this.chunkActivator;
   }

   private SKShopkeeperStorage getShopkeeperStorage() {
      return this.plugin.getShopkeeperStorage();
   }

   public AbstractShopkeeper createShopkeeper(ShopCreationData creationData) throws ShopkeeperCreateException {
      Validate.notNull(creationData, (String)"creationData is null");
      ShopType<?> shopType = creationData.getShopType();

      assert shopType != null;

      Validate.isTrue(shopType instanceof AbstractShopType, "shopType is not of type AbstractShopType, but: " + shopType.getClass().getName());
      AbstractShopType<?> abstractShopType = (AbstractShopType)shopType;
      SKShopkeeperStorage shopkeeperStorage = this.getShopkeeperStorage();
      int id = shopkeeperStorage.getNextShopkeeperId();
      AbstractShopkeeper shopkeeper = abstractShopType.createShopkeeper(id, creationData);

      assert shopkeeper != null;

      try {
         this.validateUnusedShopkeeperIds(shopkeeper);
      } catch (RuntimeException var8) {
         throw new ShopkeeperCreateException(var8.getMessage(), var8);
      }

      this.addShopkeeper(shopkeeper, ShopkeeperAddedEvent.Cause.CREATED);
      return shopkeeper;
   }

   public AbstractShopkeeper loadShopkeeper(ShopkeeperData shopkeeperData) throws InvalidDataException {
      Validate.notNull(shopkeeperData, (String)"shopkeeperData is null");
      AbstractShopType<?> shopType = (AbstractShopType)shopkeeperData.get(AbstractShopkeeper.SHOP_TYPE);

      assert shopType != null;

      AbstractShopkeeper shopkeeper = shopType.loadShopkeeper(shopkeeperData);

      assert shopkeeper != null;

      try {
         this.validateUnusedShopkeeperIds(shopkeeper);
      } catch (RuntimeException var5) {
         throw new InvalidDataException(var5.getMessage(), var5);
      }

      this.addShopkeeper(shopkeeper, ShopkeeperAddedEvent.Cause.LOADED);
      return shopkeeper;
   }

   private void validateUnusedShopkeeperIds(Shopkeeper shopkeeper) {
      Validate.isTrue(this.getShopkeeperById(shopkeeper.getId()) == null, () -> {
         return "There already exists a shopkeeper with the same id: " + shopkeeper.getId();
      });
      Validate.isTrue(this.getShopkeeperByUniqueId(shopkeeper.getUniqueId()) == null, () -> {
         return "There already exists a shopkeeper with the same unique id: " + String.valueOf(shopkeeper.getUniqueId());
      });
   }

   private void addShopkeeper(AbstractShopkeeper shopkeeper, ShopkeeperAddedEvent.Cause cause) {
      assert shopkeeper != null && !shopkeeper.isValid();

      assert !this.shopkeepersByUUID.containsKey(shopkeeper.getUniqueId());

      assert !this.shopkeepersById.containsKey(shopkeeper.getId());

      UUID shopkeeperUniqueId = shopkeeper.getUniqueId();
      int shopkeeperId = shopkeeper.getId();
      this.shopkeepersByUUID.put(shopkeeperUniqueId, shopkeeper);
      this.shopkeepersById.put(shopkeeperId, shopkeeper);
      SKShopkeeperStorage shopkeeperStorage = this.getShopkeeperStorage();
      shopkeeperStorage.onShopkeeperIdUsed(shopkeeperId);
      if (shopkeeper.isVirtual()) {
         this.virtualShopkeepers.add(shopkeeper);
      } else {
         this.chunkMap.addShopkeeper(shopkeeper);
      }

      if (shopkeeper instanceof PlayerShopkeeper) {
         ++this.playerShopCount;
      }

      AbstractShopType<?> shopType = shopkeeper.getType();
      String var10000;
      if (!shopType.isEnabled()) {
         var10000 = shopkeeper.getLogPrefix();
         Log.warning(var10000 + "Shop type '" + shopType.getIdentifier() + "' is disabled! Consider deleting this shopkeeper.");
      }

      AbstractShopObjectType<?> shopObjectType = shopkeeper.getShopObject().getType();
      if (!shopObjectType.isEnabled()) {
         var10000 = shopkeeper.getLogPrefix();
         Log.warning(var10000 + "Object type '" + shopObjectType.getIdentifier() + "' is disabled! Consider changing the object type.");
      }

      shopkeeper.informAdded(cause);
      Bukkit.getPluginManager().callEvent(new ShopkeeperAddedEvent(shopkeeper, cause));
      if (shopkeeper.isValid()) {
         this.chunkActivator.checkShopkeeperActivation(shopkeeper);
      }
   }

   private void removeShopkeeper(AbstractShopkeeper shopkeeper, ShopkeeperRemoveEvent.Cause cause) {
      assert shopkeeper != null && shopkeeper.isValid() && cause != null;

      Bukkit.getPluginManager().callEvent(new ShopkeeperRemoveEvent(shopkeeper, cause));
      if (!shopkeeper.isValid()) {
         Log.warning(shopkeeper.getLogPrefix() + "Aborting removal, because already removed during ShopkeeperRemoveEvent!");
      } else {
         shopkeeper.abortUISessionsDelayed();
         this.chunkActivator.deactivateShopkeeper(shopkeeper);
         shopkeeper.informRemoval(cause);
         if (this.shopObjectRegistry.isRegistered(shopkeeper)) {
            String var10000 = shopkeeper.getLogPrefix();
            Log.warning(var10000 + "Shop object of type '" + shopkeeper.getShopObject().getType().getIdentifier() + "' did not unregister itself during shopkeeper removal!");
         }

         UUID shopkeeperUniqueId = shopkeeper.getUniqueId();
         this.shopkeepersByUUID.remove(shopkeeperUniqueId);
         this.shopkeepersById.remove(shopkeeper.getId());
         if (shopkeeper.isVirtual()) {
            this.virtualShopkeepers.remove(shopkeeper);
         } else {
            this.chunkMap.removeShopkeeper(shopkeeper);
         }

         if (shopkeeper instanceof PlayerShopkeeper) {
            --this.playerShopCount;
         }

         if (cause == ShopkeeperRemoveEvent.Cause.DELETE) {
            this.getShopkeeperStorage().deleteShopkeeper(shopkeeper);
         }

      }
   }

   public void onShopkeeperMoved(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.isTrue(shopkeeper.isValid(), "shopkeeper is not valid");
      Validate.isTrue(!shopkeeper.isVirtual(), "shopkeeper is virtual");
      ChunkCoords oldChunk = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getLastChunkCoords());
      if (this.chunkMap.moveShopkeeper(shopkeeper)) {
         this.chunkActivator.onShopkeeperMoved(shopkeeper, oldChunk);
      }
   }

   private void unloadShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null && shopkeeper.isValid();

      this.removeShopkeeper(shopkeeper, ShopkeeperRemoveEvent.Cause.UNLOAD);
   }

   public void unloadAllShopkeepers() {
      (new ArrayList(this.getAllShopkeepers())).forEach(this::unloadShopkeeper);
   }

   public void deleteShopkeeper(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.isTrue(shopkeeper.isValid(), "shopkeeper is invalid");
      this.removeShopkeeper(shopkeeper, ShopkeeperRemoveEvent.Cause.DELETE);
   }

   public void deleteAllShopkeepers() {
      (new ArrayList(this.getAllShopkeepers())).forEach(this::deleteShopkeeper);
   }

   public Collection<? extends AbstractShopkeeper> getAllShopkeepers() {
      return this.allShopkeepersView;
   }

   public Collection<? extends AbstractShopkeeper> getVirtualShopkeepers() {
      return this.virtualShopkeepersView;
   }

   @Nullable
   public AbstractShopkeeper getShopkeeperByUniqueId(UUID shopkeeperUniqueId) {
      return (AbstractShopkeeper)this.shopkeepersByUUID.get(shopkeeperUniqueId);
   }

   @Nullable
   public AbstractShopkeeper getShopkeeperById(int shopkeeperId) {
      return (AbstractShopkeeper)this.shopkeepersById.get(shopkeeperId);
   }

   public Collection<? extends AbstractPlayerShopkeeper> getAllPlayerShopkeepers() {
      return this.allPlayerShopkeepersView;
   }

   public Collection<? extends AbstractPlayerShopkeeper> getPlayerShopkeepersByOwner(final UUID ownerUUID) {
      Validate.notNull(ownerUUID, (String)"ownerUUID is null");
      return new AbstractSet<AbstractPlayerShopkeeper>() {
         private Stream<? extends AbstractPlayerShopkeeper> createStream() {
            return SKShopkeeperRegistry.this.allPlayerShopkeepersView.stream().filter((shopkeeper) -> {
               return shopkeeper.getOwnerUUID().equals(ownerUUID);
            });
         }

         public Iterator<AbstractPlayerShopkeeper> iterator() {
            return SKShopkeeperRegistry.this.allPlayerShopkeepersView.isEmpty() ? Collections.emptyIterator() : (Iterator)Unsafe.cast(this.createStream().iterator());
         }

         public int size() {
            return SKShopkeeperRegistry.this.allPlayerShopkeepersView.isEmpty() ? 0 : this.createStream().mapToInt((shopkeeper) -> {
               return 1;
            }).sum();
         }
      };
   }

   public Stream<? extends AbstractShopkeeper> getShopkeepersByName(String shopName) {
      String normalizedShopName = StringUtils.normalize(TextUtils.stripColor(shopName));
      return StringUtils.isEmpty(normalizedShopName) ? Stream.empty() : this.getAllShopkeepers().stream().filter((shopkeeper) -> {
         String shopkeeperName = shopkeeper.getName();
         if (shopkeeperName.isEmpty()) {
            return false;
         } else {
            shopkeeperName = TextUtils.stripColor(shopkeeperName);
            shopkeeperName = StringUtils.normalize(shopkeeperName);
            return shopkeeperName.equals(normalizedShopName);
         }
      });
   }

   public Stream<? extends AbstractShopkeeper> getShopkeepersByNamePrefix(String shopNamePrefix) {
      String normalizedShopNamePrefix = StringUtils.normalize(TextUtils.stripColor(shopNamePrefix));
      return StringUtils.isEmpty(normalizedShopNamePrefix) ? Stream.empty() : this.getAllShopkeepers().stream().filter((shopkeeper) -> {
         String shopkeeperName = shopkeeper.getName();
         if (shopkeeperName.isEmpty()) {
            return false;
         } else {
            shopkeeperName = TextUtils.stripColor(shopkeeperName);
            shopkeeperName = StringUtils.normalize(shopkeeperName);
            return shopkeeperName.startsWith(normalizedShopNamePrefix);
         }
      });
   }

   public Collection<? extends String> getWorldsWithShopkeepers() {
      return this.chunkMap.getWorldsWithShopkeepers();
   }

   public Collection<? extends AbstractShopkeeper> getShopkeepersInWorld(String worldName) {
      WorldShopkeepers worldShopkeepers = this.chunkMap.getWorldShopkeepers(worldName);
      return worldShopkeepers == null ? Collections.emptySet() : worldShopkeepers.getShopkeepers();
   }

   public Map<? extends ChunkCoords, ? extends Collection<? extends AbstractShopkeeper>> getShopkeepersByChunks(String worldName) {
      WorldShopkeepers worldShopkeepers = this.chunkMap.getWorldShopkeepers(worldName);
      return worldShopkeepers == null ? Collections.emptyMap() : worldShopkeepers.getShopkeepersByChunk();
   }

   public Collection<? extends ChunkCoords> getActiveChunks(String worldName) {
      return this.activeChunkQueries.getActiveChunks(worldName);
   }

   public boolean isChunkActive(ChunkCoords chunkCoords) {
      return this.chunkActivator.isChunkActive(chunkCoords);
   }

   public Collection<? extends AbstractShopkeeper> getActiveShopkeepers() {
      return this.activeChunkQueries.getShopkeepersInActiveChunks();
   }

   public Collection<? extends AbstractShopkeeper> getActiveShopkeepers(String worldName) {
      return this.activeChunkQueries.getShopkeepersInActiveChunks(worldName);
   }

   public Collection<? extends AbstractShopkeeper> getShopkeepersInChunk(ChunkCoords chunkCoords) {
      Validate.notNull(chunkCoords, (String)"chunkCoords is null");
      ChunkShopkeepers chunkShopkeepers = this.chunkMap.getChunkShopkeepers(chunkCoords);
      return (Collection)(chunkShopkeepers == null ? Collections.emptySet() : chunkShopkeepers.getShopkeepers());
   }

   public Collection<? extends AbstractShopkeeper> getShopkeepersInChunkSnapshot(ChunkCoords chunkCoords) {
      Validate.notNull(chunkCoords, (String)"chunkCoords is null");
      ChunkShopkeepers chunkShopkeepers = this.chunkMap.getChunkShopkeepers(chunkCoords);
      return (Collection)(chunkShopkeepers == null ? Collections.emptySet() : chunkShopkeepers.getShopkeepersSnapshot());
   }

   public Collection<? extends AbstractShopkeeper> getShopkeepersAtLocation(Location location) {
      World world = LocationUtils.getWorld(location);
      String worldName = world.getName();
      int x = location.getBlockX();
      int y = location.getBlockY();
      int z = location.getBlockZ();
      List<AbstractShopkeeper> shopkeepers = new ArrayList();
      ChunkCoords chunkCoords = ChunkCoords.fromBlock(worldName, x, z);
      this.getShopkeepersInChunk(chunkCoords).forEach((shopkeeper) -> {
         assert worldName.equals(shopkeeper.getWorldName());

         if (shopkeeper.getX() == x && shopkeeper.getY() == y && shopkeeper.getZ() == z) {
            shopkeepers.add(shopkeeper);
         }

      });
      return shopkeepers;
   }

   public ShopObjectRegistry getShopObjectRegistry() {
      return this.shopObjectRegistry;
   }

   @Nullable
   public AbstractShopkeeper getShopkeeperByEntity(Entity entity) {
      Validate.notNull(entity, (String)"entity is null");
      Object objectId = EntityShopObjectIds.getObjectId(entity);
      return this.shopObjectRegistry.getShopkeeperByObjectId(objectId);
   }

   public boolean isShopkeeper(Entity entity) {
      return this.getShopkeeperByEntity(entity) != null;
   }

   @Nullable
   public AbstractShopkeeper getShopkeeperByBlock(Block block) {
      Validate.notNull(block, (String)"block is null");
      return this.getShopkeeperByBlock(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   @Nullable
   public AbstractShopkeeper getShopkeeperByBlock(String worldName, int x, int y, int z) {
      Object objectId = BlockShopObjectIds.getSharedObjectId(worldName, x, y, z);
      return this.shopObjectRegistry.getShopkeeperByObjectId(objectId);
   }

   public boolean isShopkeeper(Block block) {
      return this.getShopkeeperByBlock(block) != null;
   }
}
