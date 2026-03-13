package com.nisovin.shopkeepers.shopkeeper.spawning;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

class WorldSaveDespawner {
   private static final Predicate<AbstractShopkeeper> IS_DESPAWNED_DURING_WORLD_SAVE = (shopkeeper) -> {
      AbstractShopObjectType<?> objectType = shopkeeper.getShopObject().getType();
      return objectType.mustDespawnDuringWorldSave();
   };
   private final ShopkeeperSpawner spawner;
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;

   WorldSaveDespawner(ShopkeeperSpawner spawner, SKShopkeepersPlugin plugin, SKShopkeeperRegistry shopkeeperRegistry) {
      Validate.notNull(spawner, (String)"spawner is null");
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(shopkeeperRegistry, (String)"shopkeeperRegistry is null");
      this.spawner = spawner;
      this.plugin = plugin;
      this.shopkeeperRegistry = shopkeeperRegistry;
   }

   void onWorldUnload(World world) {
      assert world != null;

      String worldName = world.getName();
      WorldData worldData = this.spawner.getWorldData(worldName);
      if (worldData != null) {
         worldData.cancelWorldSaveRespawnTask();
      }
   }

   void onWorldSave(World world) {
      assert world != null;

      String worldName = world.getName();
      WorldData worldData = this.spawner.getOrCreateWorldData(worldName);
      if (worldData.isWorldSaveRespawnPending()) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            return "Ignoring saving of world '" + worldName + "', because another save is already processed for this world.";
         });
      } else {
         (new WorldSaveDespawner.RespawnShopkeepersAfterWorldSaveTask(worldData)).start();
         this.spawner.despawnShopkeepersInWorld(worldName, "world saving", IS_DESPAWNED_DURING_WORLD_SAVE, this::setPendingWorldSaveRespawn);
      }
   }

   private void setPendingWorldSaveRespawn(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);
      spawnState.setState(ShopkeeperSpawnState.State.PENDING_WORLD_SAVE_RESPAWN);
   }

   class RespawnShopkeepersAfterWorldSaveTask implements Runnable {
      private final WorldData worldData;
      @Nullable
      private BukkitTask task;

      RespawnShopkeepersAfterWorldSaveTask(WorldData param2) {
         assert worldData != null;

         this.worldData = worldData;
      }

      void start() {
         assert !this.worldData.isWorldSaveRespawnPending();

         this.task = Bukkit.getScheduler().runTask(WorldSaveDespawner.this.plugin, this);
         this.worldData.setWorldSaveRespawnTask(this);
      }

      public void run() {
         this.worldData.setWorldSaveRespawnTask((WorldSaveDespawner.RespawnShopkeepersAfterWorldSaveTask)null);
         WorldSaveDespawner.this.spawner.spawnShopkeepersInWorld(this.worldData.getWorldName(), "world saving finished", WorldSaveDespawner.IS_DESPAWNED_DURING_WORLD_SAVE, true);
      }

      public void cancel() {
         if (this.task != null) {
            this.task.cancel();
            this.task = null;
            this.worldData.setWorldSaveRespawnTask((WorldSaveDespawner.RespawnShopkeepersAfterWorldSaveTask)null);
            this.onCancelled();
         }

      }

      private void onCancelled() {
         WorldSaveDespawner.this.shopkeeperRegistry.getShopkeepersInWorld(this.worldData.getWorldName()).forEach((shopkeeper) -> {
            ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);
            if (spawnState.getState() == ShopkeeperSpawnState.State.PENDING_WORLD_SAVE_RESPAWN) {
               assert shopkeeper.getShopObject().getType().mustBeSpawned();

               assert shopkeeper.getShopObject().getType().mustDespawnDuringWorldSave();

               spawnState.setState(ShopkeeperSpawnState.State.DESPAWNED);
            }

         });
      }
   }
}
