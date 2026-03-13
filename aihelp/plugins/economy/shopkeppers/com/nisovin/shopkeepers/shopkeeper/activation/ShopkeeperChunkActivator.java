package com.nisovin.shopkeepers.shopkeeper.activation;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.shopkeeper.spawning.ShopkeeperSpawner;
import com.nisovin.shopkeepers.shopkeeper.ticking.ShopkeeperTicker;
import com.nisovin.shopkeepers.util.bukkit.MutableChunkCoords;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import com.nisovin.shopkeepers.util.timer.Timer;
import com.nisovin.shopkeepers.util.timer.Timings;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopkeeperChunkActivator {
   private static final long CHUNK_ACTIVATION_DELAY_TICKS = 20L;
   private static final int IMMEDIATE_CHUNK_ACTIVATION_RADIUS = 2;
   private static final Predicate<AbstractShopkeeper> SHOPKEEPER_IS_ACTIVE = AbstractShopkeeper::isActive;
   private static final Predicate<AbstractShopkeeper> SHOPKEEPER_IS_INACTIVE;
   private static final Location sharedLocation;
   private static final MutableChunkCoords sharedChunkCoords;
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final ShopkeeperTicker shopkeeperTicker;
   private final ShopkeeperSpawner shopkeeperSpawner;
   private final ChunkActivationListener listener = new ChunkActivationListener((ShopkeeperChunkActivator)Unsafe.initialized(this));
   private final Map<ChunkCoords, ChunkData> chunks = new HashMap();
   private boolean chunkActivationInProgress = false;
   private final Queue<ChunkData> deferredChunkActivations = new ArrayDeque();
   private final Timer chunkActivationTimings = new Timer();
   private int immediateChunkActivationRadius;

   public ShopkeeperChunkActivator(SKShopkeepersPlugin plugin, SKShopkeeperRegistry shopkeeperRegistry, ShopkeeperTicker shopkeeperTicker, ShopkeeperSpawner shopkeeperSpawner) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(shopkeeperRegistry, (String)"shopkeeperRegistry is null");
      Validate.notNull(shopkeeperTicker, (String)"shopkeeperTicker is null");
      Validate.notNull(shopkeeperSpawner, (String)"shopkeeperSpawner is null");
      this.plugin = plugin;
      this.shopkeeperRegistry = shopkeeperRegistry;
      this.shopkeeperTicker = shopkeeperTicker;
      this.shopkeeperSpawner = shopkeeperSpawner;
   }

   public void onEnable() {
      this.immediateChunkActivationRadius = Math.min(2, Bukkit.getViewDistance());
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this.listener);
      this.chunkActivationTimings.reset();
      this.ensureEmpty();
   }

   private void ensureEmpty() {
      if (!this.chunks.isEmpty()) {
         Log.warning("Some chunk entries were not properly removed from the chunk activator!");
         this.chunks.clear();
      }

      if (!this.deferredChunkActivations.isEmpty()) {
         Log.warning("Some deferred chunk activations were not properly removed from the chunk activator!");
         this.deferredChunkActivations.clear();
      }

   }

   @Nullable
   private ChunkData getChunkData(Chunk chunk) {
      assert chunk != null;

      sharedChunkCoords.set(chunk);
      return this.getChunkData((ChunkCoords)sharedChunkCoords);
   }

   @Nullable
   private ChunkData getChunkData(String worldName, int chunkX, int chunkZ) {
      sharedChunkCoords.set(worldName, chunkX, chunkZ);
      return this.getChunkData((ChunkCoords)sharedChunkCoords);
   }

   @Nullable
   private ChunkData getChunkData(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      return (ChunkData)this.chunks.get(chunkCoords);
   }

   private ChunkData getOrCreateChunkData(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      ChunkData chunkData = (ChunkData)this.chunks.computeIfAbsent(chunkCoords, ChunkData::new);

      assert chunkData != null;

      return chunkData;
   }

   @Nullable
   private ChunkData removeChunkData(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      ChunkData chunkData = (ChunkData)this.chunks.remove(chunkCoords);
      if (chunkData != null) {
         this.cancelDeferredActivation(chunkData);
         chunkData.cleanUp();
      }

      return chunkData;
   }

   public void onShopkeeperChunkAdded(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      this.getOrCreateChunkData(chunkCoords);
   }

   public void onShopkeeperChunkRemoved(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      this.removeChunkData(chunkCoords);
   }

   public void checkShopkeeperActivation(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      assert !shopkeeper.isVirtual();

      ChunkCoords chunkCoords = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getLastChunkCoords());
      ChunkData chunkData = (ChunkData)Unsafe.assertNonNull(this.getChunkData(chunkCoords));
      if (chunkData.isActive()) {
         this.activateShopkeeper(shopkeeper);
      } else {
         this.deactivateShopkeeper(shopkeeper);
      }

   }

   private void activateShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      assert !shopkeeper.isVirtual();

      assert shopkeeper.getLastChunkCoords() != null;

      assert this.getChunkData((ChunkCoords)Unsafe.assertNonNull(shopkeeper.getLastChunkCoords())) != null;

      if (!shopkeeper.isActive()) {
         shopkeeper.setActive(true);
         this.shopkeeperTicker.startTicking(shopkeeper);
         if (shopkeeper.isActive()) {
            this.shopkeeperSpawner.spawnShopkeeperImmediately(shopkeeper);
         }
      }
   }

   public void deactivateShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      assert !shopkeeper.isVirtual();

      assert shopkeeper.getLastChunkCoords() != null;

      assert this.getChunkData((ChunkCoords)Unsafe.assertNonNull(shopkeeper.getLastChunkCoords())) != null;

      if (shopkeeper.isActive()) {
         shopkeeper.setActive(false);
         this.shopkeeperTicker.stopTicking(shopkeeper);
         if (!shopkeeper.isActive()) {
            this.shopkeeperSpawner.despawnShopkeeper(shopkeeper);
         }
      }
   }

   public void onShopkeeperMoved(AbstractShopkeeper shopkeeper, ChunkCoords oldChunkCoords) {
      assert shopkeeper != null && oldChunkCoords != null;

      assert !shopkeeper.isVirtual();

      boolean oldActivationState = shopkeeper.isActive();
      this.checkShopkeeperActivation(shopkeeper);
      boolean activationStateChanged = shopkeeper.isActive() != oldActivationState;
      this.shopkeeperSpawner.onShopkeeperMoved(shopkeeper, oldChunkCoords, activationStateChanged);
   }

   public Timings getChunkActivationTimings() {
      return this.chunkActivationTimings;
   }

   public boolean isChunkActive(ChunkCoords chunkCoords) {
      ChunkData chunkData = this.getChunkData(chunkCoords);
      return chunkData == null ? false : chunkData.isActive();
   }

   void onChunkLoad(Chunk chunk) {
      assert chunk != null;

      ChunkData chunkData = this.getChunkData(chunk);
      if (chunkData != null) {
         if (chunkData.isActive()) {
            Log.debug(DebugOptions.shopkeeperActivation, () -> {
               return "Detected chunk load for already active chunk: " + TextUtils.getChunkString(chunk);
            });
         } else if (chunkData.isActivationDelayed()) {
            Log.debug(DebugOptions.shopkeeperActivation, () -> {
               return "Detected chunk load for chunk with already delayed activation: " + TextUtils.getChunkString(chunk);
            });
         } else {
            (new ShopkeeperChunkActivator.DelayedChunkActivationTask(chunkData)).start();
         }
      }
   }

   void activatePendingNearbyChunksDelayed(Player player) {
      assert player != null;

      Bukkit.getScheduler().runTask(this.plugin, new ShopkeeperChunkActivator.ActivatePendingNearbyChunksTask(player));
   }

   private void activatePendingNearbyChunks(Player player) {
      World world = player.getWorld();
      Location location = (Location)Unsafe.assertNonNull(player.getLocation(sharedLocation));
      int chunkX = ChunkCoords.fromBlock(location.getBlockX());
      int chunkZ = ChunkCoords.fromBlock(location.getBlockZ());
      sharedLocation.setWorld((World)null);
      this.activatePendingNearbyChunks(world, chunkX, chunkZ, this.immediateChunkActivationRadius);
   }

   private void activatePendingNearbyChunks(World world, int centerChunkX, int centerChunkZ, int chunkRadius) {
      assert world != null && chunkRadius >= 0;

      String worldName = world.getName();
      int minChunkX = centerChunkX - chunkRadius;
      int maxChunkX = centerChunkX + chunkRadius;
      int minChunkZ = centerChunkZ - chunkRadius;
      int maxChunkZ = centerChunkZ + chunkRadius;

      for(int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
         for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
            ChunkData chunkData = this.getChunkData(worldName, chunkX, chunkZ);
            if (chunkData != null && chunkData.isActivationDelayed()) {
               this.activateChunk(chunkData);
            }
         }
      }

   }

   private boolean isActivationDeferred(ChunkData chunkData) {
      return this.deferredChunkActivations.contains(chunkData);
   }

   private void cancelDeferredActivation(ChunkData chunkData) {
      assert chunkData != null;

      if (chunkData.isShouldBeActive()) {
         chunkData.setShouldBeActive(false);
         this.deferredChunkActivations.remove(chunkData);
      }

   }

   private void activateChunk(ChunkData chunkData) {
      assert chunkData != null;

      assert chunkData.getChunkCoords().isChunkLoaded() : "Chunk not loaded";

      boolean oldShouldBeActive = chunkData.isShouldBeActive();
      chunkData.setShouldBeActive(true);
      if (chunkData.isActive()) {
         assert !chunkData.isActivationDelayed();

         assert !this.isActivationDeferred(chunkData);

      } else {
         chunkData.cancelDelayedActivation();
         ChunkCoords chunkCoords = chunkData.getChunkCoords();
         if (this.chunkActivationInProgress) {
            if (oldShouldBeActive) {
               Log.debug(DebugOptions.shopkeeperActivation, () -> {
                  return "Ignoring activation request of chunk " + String.valueOf(chunkCoords) + ": The chunk is already pending activation.";
               });
            } else {
               assert !this.isActivationDeferred(chunkData);

               Log.debug(DebugOptions.shopkeeperActivation, () -> {
                  return "Another chunk activation is already in progress. Deferring activation of chunk " + String.valueOf(chunkCoords);
               });
               this.deferredChunkActivations.add(chunkData);
            }
         } else {
            assert !this.isActivationDeferred(chunkData);

            this.chunkActivationInProgress = true;
            this.chunkActivationTimings.start();
            Collection<? extends AbstractShopkeeper> shopkeepers = this.shopkeeperRegistry.getShopkeepersInChunkSnapshot(chunkCoords);
            Log.debug(DebugOptions.shopkeeperActivation, () -> {
               int var10000 = shopkeepers.size();
               return "Activating " + var10000 + " shopkeepers in chunk " + TextUtils.getChunkString(chunkCoords);
            });
            chunkData.setActive(true);
            shopkeepers.forEach((shopkeeperx) -> {
               shopkeeperx.setActive(true);
            });

            try {
               Iterator var5 = shopkeepers.iterator();

               while(var5.hasNext()) {
                  AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var5.next();
                  if (!chunkData.isActive()) {
                     return;
                  }

                  if (shopkeeper.isActive()) {
                     this.shopkeeperTicker.startTicking(shopkeeper);
                  }
               }

               if (chunkData.isActive()) {
                  this.shopkeeperSpawner.spawnChunkShopkeepers(chunkCoords, "activation", shopkeepers, SHOPKEEPER_IS_ACTIVE, false);
                  return;
               }
            } finally {
               this.chunkActivationTimings.stop();
               this.chunkActivationInProgress = false;
               this.processDeferredChunkActivations();
            }

         }
      }
   }

   private void processDeferredChunkActivations() {
      ChunkData chunkData;
      while((chunkData = (ChunkData)this.deferredChunkActivations.poll()) != null) {
         assert chunkData.isShouldBeActive();

         this.activateChunk(chunkData);
      }

   }

   void onChunkUnload(Chunk chunk) {
      assert chunk != null;

      ChunkData chunkData = this.getChunkData(chunk);
      if (chunkData != null) {
         this.deactivateChunk(chunkData);
      }
   }

   private void deactivateChunk(ChunkData chunkData) {
      assert chunkData != null;

      ChunkCoords chunkCoords = chunkData.getChunkCoords();
      if (!chunkData.isActive()) {
         this.cancelDeferredActivation(chunkData);
         chunkData.cancelDelayedActivation();
      } else {
         assert !chunkData.isActivationDelayed();

         assert !this.isActivationDeferred(chunkData);

         chunkData.setActive(false);
         Collection<? extends AbstractShopkeeper> shopkeepers = this.shopkeeperRegistry.getShopkeepersInChunkSnapshot(chunkCoords);
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            int var10000 = shopkeepers.size();
            return "Deactivating " + var10000 + " shopkeepers in chunk " + TextUtils.getChunkString(chunkCoords);
         });
         shopkeepers.forEach((shopkeeperx) -> {
            shopkeeperx.setActive(false);
         });
         Iterator var4 = shopkeepers.iterator();

         while(var4.hasNext()) {
            AbstractShopkeeper shopkeeper = (AbstractShopkeeper)var4.next();
            if (chunkData.isActive()) {
               return;
            }

            if (!shopkeeper.isActive()) {
               this.shopkeeperTicker.stopTicking(shopkeeper);
            }
         }

         if (!chunkData.isActive()) {
            this.shopkeeperSpawner.despawnChunkShopkeepers(chunkCoords, "deactivation", shopkeepers, SHOPKEEPER_IS_INACTIVE, (Consumer)null);
         }
      }
   }

   public void activateShopkeepersInAllWorlds() {
      List<? extends World> worlds = (List)Unsafe.castNonNull(Bukkit.getWorlds());
      worlds.forEach(this::activateChunks);
   }

   void onWorldLoad(World world) {
      assert world != null;

      this.activateChunks(world);
   }

   private void activateChunks(World world) {
      assert world != null;

      String worldName = world.getName();
      int shopkeeperCount = this.shopkeeperRegistry.getShopkeepersInWorld(worldName).size();
      if (shopkeeperCount != 0) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            return "Activating " + shopkeeperCount + " shopkeepers in world '" + worldName + "'";
         });
         List<? extends ChunkCoords> chunks = new ArrayList(this.shopkeeperRegistry.getShopkeepersByChunks(worldName).keySet());
         chunks.forEach((chunkCoords) -> {
            ChunkData chunkData = (ChunkData)Unsafe.assertNonNull(this.getChunkData(chunkCoords));
            if (chunkData.needsActivation()) {
               chunkData.setShouldBeActive(true);
            }

         });
         chunks.forEach(this::activateChunkIfShouldBeActive);
      }
   }

   private void activateChunkIfShouldBeActive(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      ChunkData chunkData = this.getChunkData(chunkCoords);
      if (chunkData != null) {
         if (chunkData.isShouldBeActive()) {
            this.activateChunk(chunkData);
         }
      }
   }

   public void deactivateShopkeepersInAllWorlds() {
      List<? extends World> worlds = (List)Unsafe.castNonNull(Bukkit.getWorlds());
      worlds.forEach(this::deactivateChunks);
   }

   void onWorldUnload(World world) {
      assert world != null;

      this.deactivateChunks(world);
   }

   private void deactivateChunks(World world) {
      assert world != null;

      String worldName = world.getName();
      int shopkeeperCount = this.shopkeeperRegistry.getShopkeepersInWorld(worldName).size();
      if (shopkeeperCount != 0) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            return "Deactivating " + shopkeeperCount + " shopkeepers in world '" + worldName + "'";
         });
         List<? extends ChunkCoords> chunks = new ArrayList(this.shopkeeperRegistry.getShopkeepersByChunks(worldName).keySet());
         chunks.forEach((chunkCoords) -> {
            ChunkData chunkData = (ChunkData)Unsafe.assertNonNull(this.getChunkData(chunkCoords));
            chunkData.setShouldBeActive(false);
         });
         chunks.forEach(this::deactivateChunkIfShouldBeInactive);
      }
   }

   private void deactivateChunkIfShouldBeInactive(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      ChunkData chunkData = this.getChunkData(chunkCoords);
      if (chunkData != null) {
         if (!chunkData.isShouldBeActive()) {
            this.deactivateChunk(chunkData);
         }
      }
   }

   static {
      SHOPKEEPER_IS_INACTIVE = (Predicate)Unsafe.assertNonNull(SHOPKEEPER_IS_ACTIVE.negate());
      sharedLocation = new Location((World)null, 0.0D, 0.0D, 0.0D);
      sharedChunkCoords = new MutableChunkCoords();
   }

   private class DelayedChunkActivationTask implements Runnable {
      private final ChunkData chunkData;

      DelayedChunkActivationTask(ChunkData param2) {
         assert chunkData != null;

         this.chunkData = chunkData;
      }

      void start() {
         assert !this.chunkData.isActive() && !this.chunkData.isActivationDelayed();

         BukkitTask task = Bukkit.getScheduler().runTaskLater(ShopkeeperChunkActivator.this.plugin, this, 20L);
         this.chunkData.setDelayedActivationTask(task);
      }

      public void run() {
         assert this.chunkData.getChunkCoords().isChunkLoaded();

         this.chunkData.setDelayedActivationTask((BukkitTask)null);
         ShopkeeperChunkActivator.this.activateChunk(this.chunkData);
      }
   }

   private class ActivatePendingNearbyChunksTask implements Runnable {
      private final Player player;

      ActivatePendingNearbyChunksTask(Player param2) {
         assert player != null;

         this.player = player;
      }

      public void run() {
         if (this.player.isOnline()) {
            ShopkeeperChunkActivator.this.activatePendingNearbyChunks(this.player);
         }
      }
   }
}
