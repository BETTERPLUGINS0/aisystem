package com.nisovin.shopkeepers.shopkeeper.spawning;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import com.nisovin.shopkeepers.util.taskqueue.TaskQueueStatistics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperSpawner {
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final WorldSaveDespawner worldSaveDespawner;
   private final ShopkeeperSpawnerWorldListener listener;
   private final ShopkeeperSpawnQueue spawnQueue;
   private final Map<String, WorldData> worlds = new HashMap();
   private static final Predicate<AbstractShopkeeper> IS_SPAWNING = ShopkeeperSpawner::isSpawning;
   private static final Predicate<AbstractShopkeeper> IS_DESPAWNING = ShopkeeperSpawner::isDespawning;

   public ShopkeeperSpawner(SKShopkeepersPlugin plugin, SKShopkeeperRegistry shopkeeperRegistry) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(shopkeeperRegistry, (String)"shopkeeperRegistry is null");
      this.plugin = plugin;
      this.shopkeeperRegistry = shopkeeperRegistry;
      this.worldSaveDespawner = new WorldSaveDespawner((ShopkeeperSpawner)Unsafe.initialized(this), plugin, shopkeeperRegistry);
      this.listener = new ShopkeeperSpawnerWorldListener((ShopkeeperSpawner)Unsafe.initialized(this), this.worldSaveDespawner);
      ShopkeeperSpawner var10004 = (ShopkeeperSpawner)Unsafe.initialized(this);
      Objects.requireNonNull(var10004);
      this.spawnQueue = new ShopkeeperSpawnQueue(plugin, var10004::doSpawnShopkeeper);
   }

   public void onEnable() {
      this.spawnQueue.start();
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
      Bukkit.getScheduler().runTaskLater(this.plugin, new ShopkeeperSpawner.CheckUnspawnableShopkeepersTask(), 5L);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this.listener);
      this.spawnQueue.shutdown();
      this.worlds.values().forEach(WorldData::cleanUp);
      this.worlds.clear();
   }

   @Nullable
   WorldData getWorldData(String worldName) {
      assert worldName != null;

      return (WorldData)this.worlds.get(worldName);
   }

   WorldData getOrCreateWorldData(String worldName) {
      assert worldName != null;

      WorldData worldData = (WorldData)this.worlds.computeIfAbsent(worldName, WorldData::new);

      assert worldData != null;

      return worldData;
   }

   @Nullable
   private WorldData removeWorldData(String worldName) {
      assert worldName != null;

      WorldData worldData = (WorldData)this.worlds.remove(worldName);
      if (worldData != null) {
         worldData.cleanUp();
      }

      return worldData;
   }

   public void onShopkeeperWorldRemoved(String worldName) {
      assert worldName != null;

      if (Bukkit.getWorld(worldName) == null) {
         this.removeWorldData(worldName);
      }

   }

   void onWorldUnload(World world) {
      assert world != null;

      String worldName = world.getName();
      if (this.shopkeeperRegistry.getShopkeepersInWorld(worldName).isEmpty()) {
         this.removeWorldData(worldName);
      }

   }

   private static ShopkeeperSpawnState.State getSpawnState(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      return ((ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class)).getState();
   }

   private static boolean isSpawning(AbstractShopkeeper shopkeeper) {
      return getSpawnState(shopkeeper) == ShopkeeperSpawnState.State.SPAWNING;
   }

   private static boolean isDespawning(AbstractShopkeeper shopkeeper) {
      return getSpawnState(shopkeeper) == ShopkeeperSpawnState.State.DESPAWNING;
   }

   private void updateSpawnState(AbstractShopkeeper shopkeeper, ShopkeeperSpawnState.State newState) {
      assert shopkeeper != null;

      ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);
      if (spawnState.getState() == ShopkeeperSpawnState.State.QUEUED) {
         this.spawnQueue.remove(shopkeeper);
      }

      spawnState.setState(newState);
   }

   public void spawnShopkeeperImmediately(AbstractShopkeeper shopkeeper) {
      this.spawnShopkeeper(shopkeeper, true);
   }

   private SpawnResult spawnShopkeeper(AbstractShopkeeper shopkeeper, boolean spawnImmediately) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.isTrue(shopkeeper.isValid(), "shopkeeper is invalid");
      AbstractShopObject shopObject = shopkeeper.getShopObject();
      AbstractShopObjectType<?> shopObjectType = shopObject.getType();
      if (!shopObjectType.mustBeSpawned()) {
         return SpawnResult.IGNORED;
      } else if (!shopkeeper.isActive()) {
         return SpawnResult.IGNORED_INACTIVE;
      } else {
         boolean alreadySpawned = shopObject.isSpawned();
         String worldName = (String)Unsafe.assertNonNull(shopkeeper.getWorldName());
         WorldData worldData = this.getOrCreateWorldData(worldName);

         assert worldData != null;

         if (worldData.isWorldSaveRespawnPending() && shopObjectType.mustDespawnDuringWorldSave()) {
            SpawnResult result;
            if (alreadySpawned) {
               Log.debug(DebugOptions.shopkeeperActivation, () -> {
                  return shopkeeper.getLocatedLogPrefix() + "Despawn due to pending respawn after world save.";
               });
               this.doDespawnShopkeeper(shopkeeper);
               result = SpawnResult.DESPAWNED_AND_AWAITING_WORLD_SAVE_RESPAWN;
            } else {
               Log.debug(DebugOptions.shopkeeperActivation, () -> {
                  return shopkeeper.getLocatedLogPrefix() + "Skipping spawning due to pending respawn after world save.";
               });
               result = SpawnResult.AWAITING_WORLD_SAVE_RESPAWN;
            }

            this.updateSpawnState(shopkeeper, ShopkeeperSpawnState.State.PENDING_WORLD_SAVE_RESPAWN);
            return result;
         } else if (alreadySpawned) {
            this.updateSpawnState(shopkeeper, ShopkeeperSpawnState.State.SPAWNED);
            return SpawnResult.ALREADY_SPAWNED;
         } else if (spawnImmediately) {
            return this.doSpawnShopkeeper(shopkeeper) ? SpawnResult.SPAWNED : SpawnResult.SPAWNING_FAILED;
         } else {
            this.updateSpawnState(shopkeeper, ShopkeeperSpawnState.State.DESPAWNED);
            this.spawnQueue.add(shopkeeper);
            return SpawnResult.QUEUED;
         }
      }
   }

   public void despawnShopkeeper(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.isTrue(shopkeeper.isValid(), "shopkeeper is invalid");
      AbstractShopObject shopObject = shopkeeper.getShopObject();
      if (shopObject.getType().mustBeSpawned()) {
         this.doDespawnShopkeeper(shopkeeper);
      }
   }

   public void onShopkeeperMoved(AbstractShopkeeper shopkeeper, ChunkCoords oldChunkCoords, boolean activationStateChanged) {
      assert shopkeeper != null && oldChunkCoords != null;

      if (!activationStateChanged) {
         if (shopkeeper.isActive()) {
            this.spawnShopkeeper(shopkeeper, true);
         }
      }
   }

   private boolean doSpawnShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      AbstractShopObject shopObject = shopkeeper.getShopObject();
      AbstractShopObjectType<?> shopObjectType = shopObject.getType();

      assert shopObjectType.mustBeSpawned();

      this.updateSpawnState(shopkeeper, ShopkeeperSpawnState.State.DESPAWNED);
      if (!shopObjectType.isEnabled()) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            String var10000 = shopkeeper.getLogPrefix();
            return var10000 + "Object type '" + shopObjectType.getIdentifier() + "' is disabled. Skipping spawning.";
         });
         return false;
      } else {
         ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);
         spawnState.setState(ShopkeeperSpawnState.State.SPAWNED);
         boolean spawned = false;

         try {
            spawned = shopObject.spawn();
         } catch (Throwable var7) {
            Log.severe(shopkeeper.getLogPrefix() + "Error during spawning!", var7);
         }

         if (spawned) {
            Object objectId = shopObject.getId();
            if (objectId == null) {
               Log.warning(shopkeeper.getLogPrefix() + "Successfully spawned, but provides no object id!");
            } else if (!objectId.equals(shopObject.getLastId())) {
               Log.warning(shopkeeper.getLogPrefix() + "Successfully spawned, but object not registered!");
            }

            return true;
         } else {
            spawnState.setState(ShopkeeperSpawnState.State.DESPAWNED);
            Log.debug(shopkeeper.getLocatedLogPrefix() + "Spawning failed!");
            return false;
         }
      }
   }

   private void doDespawnShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      AbstractShopObject shopObject = shopkeeper.getShopObject();

      assert shopObject.getType().mustBeSpawned();

      this.updateSpawnState(shopkeeper, ShopkeeperSpawnState.State.DESPAWNED);

      try {
         shopObject.despawn();
      } catch (Throwable var4) {
         Log.severe(shopkeeper.getLogPrefix() + "Error during despawning!", var4);
      }

   }

   public TaskQueueStatistics getSpawnQueueStatistics() {
      return this.spawnQueue;
   }

   public void spawnChunkShopkeepers(ChunkCoords chunkCoords, String spawnReason, Predicate<? super AbstractShopkeeper> filter, boolean spawnImmediately) {
      Collection<? extends AbstractShopkeeper> shopkeepers = this.shopkeeperRegistry.getShopkeepersInChunkSnapshot(chunkCoords);
      this.spawnChunkShopkeepers(chunkCoords, spawnReason, shopkeepers, filter, spawnImmediately);
   }

   public void spawnChunkShopkeepers(ChunkCoords chunkCoords, String spawnReason, Collection<? extends AbstractShopkeeper> shopkeepers, Predicate<? super AbstractShopkeeper> filter, boolean spawnImmediately) {
      assert chunkCoords != null && spawnReason != null && shopkeepers != null && filter != null;

      if (!shopkeepers.isEmpty()) {
         if (this.shopkeeperRegistry.isChunkActive(chunkCoords)) {
            Log.debug(DebugOptions.shopkeeperActivation, () -> {
               int var10000 = shopkeepers.size();
               return "Spawning " + var10000 + " shopkeepers in chunk " + TextUtils.getChunkString(chunkCoords) + (spawnReason.isEmpty() ? "" : " (" + spawnReason + ")");
            });
            shopkeepers.forEach((shopkeeperx) -> {
               AbstractShopObject shopObject = shopkeeperx.getShopObject();
               AbstractShopObjectType<?> objectType = shopObject.getType();
               if (objectType.mustBeSpawned()) {
                  if (filter.test(shopkeeperx)) {
                     this.updateSpawnState(shopkeeperx, ShopkeeperSpawnState.State.SPAWNING);
                  }
               }
            });
            int spawned = 0;
            int awaitingWorldSaveRespawn = 0;
            boolean dirty = false;
            Iterator var9 = shopkeepers.iterator();

            while(var9.hasNext()) {
               AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var9.next();
               AbstractShopObject shopObject = shopkeeper.getShopObject();
               AbstractShopObjectType<?> objectType = shopObject.getType();
               if (objectType.mustBeSpawned() && filter.test(shopkeeper)) {
                  ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);
                  if (spawnState.getState() != ShopkeeperSpawnState.State.SPAWNING) {
                     Log.debug(() -> {
                        return shopkeeper.getLogPrefix() + "  Skipping spawning because superseded by another spawn or despawn request.";
                     });
                  } else {
                     assert chunkCoords.equals(shopkeeper.getLastChunkCoords());

                     assert shopkeeper.isActive();

                     SpawnResult result = this.spawnShopkeeper(shopkeeper, spawnImmediately);
                     switch(result) {
                     case SPAWNED:
                     case QUEUED:
                        ++spawned;
                        break;
                     case AWAITING_WORLD_SAVE_RESPAWN:
                     case DESPAWNED_AND_AWAITING_WORLD_SAVE_RESPAWN:
                        ++awaitingWorldSaveRespawn;
                     }

                     if (shopkeeper.isDirty()) {
                        dirty = true;
                     }
                  }
               }
            }

            Log.debug(DebugOptions.shopkeeperActivation, () -> {
               return "  Actually spawned: " + spawned + (spawnImmediately ? "" : " (queued)");
            });
            if (awaitingWorldSaveRespawn > 0) {
               Log.debug(DebugOptions.shopkeeperActivation, () -> {
                  return "  Skipped due to a pending respawn after world save: " + awaitingWorldSaveRespawn;
               });
            }

            if (dirty) {
               this.plugin.getShopkeeperStorage().saveDelayed();
            }

         }
      }
   }

   public void despawnChunkShopkeepers(ChunkCoords chunkCoords, String despawnReason, Predicate<? super AbstractShopkeeper> filter, @Nullable Consumer<? super AbstractShopkeeper> onDespawned) {
      Collection<? extends AbstractShopkeeper> shopkeepers = this.shopkeeperRegistry.getShopkeepersInChunkSnapshot(chunkCoords);
      this.despawnChunkShopkeepers(chunkCoords, despawnReason, shopkeepers, filter, onDespawned);
   }

   public void despawnChunkShopkeepers(ChunkCoords chunkCoords, String despawnReason, Collection<? extends AbstractShopkeeper> shopkeepers, Predicate<? super AbstractShopkeeper> filter, @Nullable Consumer<? super AbstractShopkeeper> onDespawned) {
      assert chunkCoords != null && despawnReason != null && shopkeepers != null && filter != null;

      if (!shopkeepers.isEmpty()) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            int var10000 = shopkeepers.size();
            return "Despawning " + var10000 + " shopkeepers in chunk " + TextUtils.getChunkString(chunkCoords) + (despawnReason.isEmpty() ? "" : " (" + despawnReason + ")");
         });
         shopkeepers.forEach((shopkeeperx) -> {
            AbstractShopObject shopObject = shopkeeperx.getShopObject();
            AbstractShopObjectType<?> objectType = shopObject.getType();
            if (objectType.mustBeSpawned()) {
               if (filter.test(shopkeeperx)) {
                  this.updateSpawnState(shopkeeperx, ShopkeeperSpawnState.State.DESPAWNING);
               }
            }
         });
         boolean initialChunkActivationState = this.shopkeeperRegistry.isChunkActive(chunkCoords);
         int despawned = 0;
         boolean dirty = false;
         Iterator var9 = shopkeepers.iterator();

         while(var9.hasNext()) {
            AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var9.next();
            AbstractShopObject shopObject = shopkeeper.getShopObject();
            AbstractShopObjectType<?> objectType = shopObject.getType();
            if (objectType.mustBeSpawned() && filter.test(shopkeeper)) {
               ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);
               if (spawnState.getState() != ShopkeeperSpawnState.State.DESPAWNING) {
                  Log.debug(() -> {
                     return shopkeeper.getLogPrefix() + "  Skipping despawning because superseded by another spawn or despawn request.";
                  });
               } else {
                  assert chunkCoords.equals(shopkeeper.getLastChunkCoords());

                  assert initialChunkActivationState == shopkeeper.isActive();

                  this.despawnShopkeeper(shopkeeper);
                  ++despawned;

                  assert spawnState.getState() == ShopkeeperSpawnState.State.DESPAWNED;

                  if (onDespawned != null) {
                     onDespawned.accept(shopkeeper);
                  }

                  if (shopkeeper.isDirty()) {
                     dirty = true;
                  }
               }
            }
         }

         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            return "  Actually despawned: " + despawned;
         });
         if (dirty) {
            this.plugin.getShopkeeperStorage().saveDelayed();
         }

      }
   }

   void spawnShopkeepersInWorld(String worldName, String spawnReason, Predicate<? super AbstractShopkeeper> shopkeeperFilter, boolean spawnImmediately) {
      assert worldName != null && spawnReason != null && shopkeeperFilter != null;

      Log.debug(DebugOptions.shopkeeperActivation, () -> {
         int var10000 = this.shopkeeperRegistry.getShopkeepersInWorld(worldName).size();
         return "Spawning " + var10000 + " shopkeepers in world '" + worldName + "'" + (spawnReason.isEmpty() ? "" : " (" + spawnReason + ")");
      });
      List<? extends ChunkCoords> chunks = new ArrayList(this.shopkeeperRegistry.getShopkeepersByChunks(worldName).keySet());
      chunks.forEach((chunkCoords) -> {
         if (this.shopkeeperRegistry.isChunkActive(chunkCoords)) {
            Collection<? extends AbstractShopkeeper> chunkShopkeepers = this.shopkeeperRegistry.getShopkeepersInChunk(chunkCoords);
            chunkShopkeepers.forEach((shopkeeper) -> {
               if (shopkeeperFilter.test(shopkeeper)) {
                  this.updateSpawnState(shopkeeper, ShopkeeperSpawnState.State.SPAWNING);
               }
            });
         }
      });
      Predicate<? super AbstractShopkeeper> newShopkeeperFilter = IS_SPAWNING.and(shopkeeperFilter);

      assert newShopkeeperFilter != null;

      chunks.forEach((chunkCoords) -> {
         this.spawnChunkShopkeepers(chunkCoords, spawnReason, newShopkeeperFilter, spawnImmediately);
      });
   }

   void despawnShopkeepersInWorld(String worldName, String despawnReason, Predicate<? super AbstractShopkeeper> shopkeeperFilter, @Nullable Consumer<? super AbstractShopkeeper> onDespawned) {
      assert worldName != null && despawnReason != null && shopkeeperFilter != null;

      Log.debug(DebugOptions.shopkeeperActivation, () -> {
         int var10000 = this.shopkeeperRegistry.getShopkeepersInWorld(worldName).size();
         return "Despawning " + var10000 + " shopkeepers in world '" + worldName + "'" + (despawnReason.isEmpty() ? "" : " (" + despawnReason + ")");
      });
      List<? extends ChunkCoords> chunks = new ArrayList(this.shopkeeperRegistry.getShopkeepersByChunks(worldName).keySet());
      chunks.forEach((chunkCoords) -> {
         if (this.shopkeeperRegistry.isChunkActive(chunkCoords)) {
            Collection<? extends AbstractShopkeeper> chunkShopkeepers = this.shopkeeperRegistry.getShopkeepersInChunk(chunkCoords);
            chunkShopkeepers.forEach((shopkeeper) -> {
               if (shopkeeperFilter.test(shopkeeper)) {
                  this.updateSpawnState(shopkeeper, ShopkeeperSpawnState.State.DESPAWNING);
               }
            });
         }
      });
      Predicate<? super AbstractShopkeeper> newShopkeeperFilter = IS_DESPAWNING.and(shopkeeperFilter);

      assert newShopkeeperFilter != null;

      chunks.forEach((chunkCoords) -> {
         if (this.shopkeeperRegistry.isChunkActive(chunkCoords)) {
            this.despawnChunkShopkeepers(chunkCoords, despawnReason, newShopkeeperFilter, onDespawned);
         }
      });
   }

   public List<Shopkeeper> checkUnspawnableShopkeepers(boolean deleteUnspawnableShopkeepers, boolean silent) {
      SKShopkeeperRegistry shopkeeperRegistry = this.plugin.getShopkeeperRegistry();
      List<Shopkeeper> unspawnableShopkeepers = new ArrayList();
      shopkeeperRegistry.getAllShopkeepers().forEach((shopkeeperx) -> {
         AbstractShopObject shopObject = shopkeeperx.getShopObject();
         if (shopObject.getType().mustBeSpawned()) {
            if (shopObject.isLastSpawnFailed()) {
               unspawnableShopkeepers.add(shopkeeperx);
               if (!silent) {
                  Log.warning(shopkeeperx.getLogPrefix() + "The last spawn attempt failed.");
               }

            }
         }
      });
      if (!unspawnableShopkeepers.isEmpty()) {
         if (deleteUnspawnableShopkeepers) {
            Iterator var5 = unspawnableShopkeepers.iterator();

            while(var5.hasNext()) {
               Shopkeeper shopkeeper = (Shopkeeper)var5.next();
               shopkeeper.delete();
            }

            this.plugin.getShopkeeperStorage().save();
            if (!silent) {
               Log.warning("Deleted " + unspawnableShopkeepers.size() + " shopkeepers that previously failed to spawn!");
            }
         } else if (!silent) {
            Log.warning("Found " + unspawnableShopkeepers.size() + " shopkeepers that previously failed to spawn! Either enable the setting 'delete-unspawnable-shopkeepers' inside the config, or use the command '/shopkeepers deleteUnspawnableShopkeepers' to automatically delete these shopkeepers and get rid of these warnings.");
         }
      }

      return unspawnableShopkeepers;
   }

   private class CheckUnspawnableShopkeepersTask implements Runnable {
      public void run() {
         ShopkeeperSpawner.this.checkUnspawnableShopkeepers(Settings.deleteUnspawnableShopkeepers, false);
      }
   }
}
