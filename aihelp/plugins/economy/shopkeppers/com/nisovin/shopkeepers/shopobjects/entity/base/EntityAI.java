package com.nisovin.shopkeepers.shopobjects.entity.base;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.MutableChunkCoords;
import com.nisovin.shopkeepers.util.bukkit.WorldUtils;
import com.nisovin.shopkeepers.util.java.CyclicCounter;
import com.nisovin.shopkeepers.util.java.RateLimiter;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.timer.Timer;
import com.nisovin.shopkeepers.util.timer.Timings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EntityAI implements Listener {
   public static final float LOOK_RANGE = 6.0F;
   public static final int AI_ACTIVATION_TICK_RATE = 30;
   private static final int AI_ACTIVATION_CHUNK_RANGE = 1;
   private static final double DISTANCE_TO_GROUND_THRESHOLD = 0.01D;
   private static final double MAX_FALLING_DISTANCE_PER_TICK = 0.5D;
   private static final int FALLING_CHECK_PERIOD_TICKS = 10;
   private static final CyclicCounter nextFallingCheckOffset = new CyclicCounter(1, 11);
   private static final Location sharedLocation = new Location((World)null, 0.0D, 0.0D, 0.0D);
   private static final MutableChunkCoords sharedChunkCoords = new MutableChunkCoords();
   private final SKShopkeepersPlugin plugin;
   private double maxFallingDistancePerUpdate;
   private double gravityCollisionCheckRange;
   private boolean customGravityEnabled;
   private final Map<ChunkCoords, EntityAI.ChunkData> chunks = new LinkedHashMap();
   private final Map<BaseEntityShopObject<?>, EntityAI.EntityData> shopObjects = new HashMap();
   @Nullable
   private BukkitTask aiTask = null;
   private boolean currentlyRunning = false;
   private int activeAIChunksCount = 0;
   private int activeAIEntityCount = 0;
   private int activeGravityChunksCount = 0;
   private int activeGravityEntityCount = 0;
   private final Timer totalTimings = new Timer();
   private final Timer activationTimings = new Timer();
   private final Timer gravityTimings = new Timer();
   private final Timer aiTimings = new Timer();

   public EntityAI(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
   }

   public void onEnable() {
      this.maxFallingDistancePerUpdate = (double)Settings.entityBehaviorTickPeriod * 0.5D;
      this.gravityCollisionCheckRange = this.maxFallingDistancePerUpdate + 0.1D;
      this.customGravityEnabled = this._isCustomGravityEnabled();
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      this.startTask();
   }

   public void onDisable() {
      assert !this.currentlyRunning;

      HandlerList.unregisterAll(this);
      this.stopTask();
      this.chunks.clear();
      this.shopObjects.clear();
      this.resetStatistics();
   }

   public void addShopObject(BaseEntityShopObject<?> shopObject) {
      Validate.notNull(shopObject, (String)"shopObject is null");
      Validate.State.isTrue(!this.currentlyRunning, "Cannot add shop objects while the AI task is running!");
      Validate.isTrue(!this.shopObjects.containsKey(shopObject), "shopObject is already added");
      Entity entity = shopObject.getEntity();
      Validate.notNull(entity, (String)"shopObject is not spawned currently!");

      assert entity != null;

      Validate.isTrue(entity.isValid(), "entity is invalid");
      Location entityLocation = (Location)Unsafe.assertNonNull(entity.getLocation(sharedLocation));
      sharedChunkCoords.set(entityLocation);
      sharedLocation.setWorld((World)null);
      EntityAI.ChunkData chunkData = (EntityAI.ChunkData)this.chunks.get(sharedChunkCoords);
      if (chunkData == null) {
         ChunkCoords chunkCoords = new ChunkCoords(sharedChunkCoords);
         chunkData = new EntityAI.ChunkData(chunkCoords, this.customGravityEnabled);
         this.chunks.put(chunkCoords, chunkData);
         if (chunkData.activeAI) {
            ++this.activeAIChunksCount;
         }

         if (chunkData.activeGravity) {
            ++this.activeGravityChunksCount;
         }
      }

      EntityAI.EntityData entityData = new EntityAI.EntityData(shopObject, chunkData);
      this.shopObjects.put(shopObject, entityData);
      chunkData.entities.add(entityData);
      if (chunkData.activeAI) {
         ++this.activeAIEntityCount;
      }

      if (chunkData.activeGravity) {
         ++this.activeGravityEntityCount;
      }

      this.startTask();
   }

   public void removeShopObject(BaseEntityShopObject<?> shopObject) {
      Validate.State.isTrue(!this.currentlyRunning, "Cannot remove entities while the AI task is running!");
      EntityAI.EntityData entityData = (EntityAI.EntityData)this.shopObjects.remove(shopObject);
      if (entityData != null) {
         EntityAI.ChunkData chunkData = entityData.chunkData;
         chunkData.entities.remove(entityData);
         if (chunkData.entities.isEmpty()) {
            this.chunks.remove(chunkData.chunkCoords);
            if (chunkData.activeAI) {
               --this.activeAIChunksCount;
            }

            if (chunkData.activeGravity) {
               --this.activeGravityChunksCount;
            }
         }

         if (chunkData.activeAI) {
            --this.activeAIEntityCount;
         }

         if (chunkData.activeGravity) {
            --this.activeGravityEntityCount;
         }

      }
   }

   public void updateLocation(BaseEntityShopObject<?> shopObject) {
      this.removeShopObject(shopObject);
      this.addShopObject(shopObject);
   }

   private void resetStatistics() {
      this.activeAIChunksCount = 0;
      this.activeAIEntityCount = 0;
      this.activeGravityChunksCount = 0;
      this.activeGravityEntityCount = 0;
      this.totalTimings.reset();
      this.activationTimings.reset();
      this.gravityTimings.reset();
      this.aiTimings.reset();
   }

   public int getEntityCount() {
      return this.shopObjects.size();
   }

   public int getActiveAIChunksCount() {
      return this.activeAIChunksCount;
   }

   public int getActiveAIEntityCount() {
      return this.activeAIEntityCount;
   }

   public int getActiveGravityChunksCount() {
      return this.activeGravityChunksCount;
   }

   public int getActiveGravityEntityCount() {
      return this.activeGravityEntityCount;
   }

   public Timings getTotalTimings() {
      return this.totalTimings;
   }

   public Timings getActivationTimings() {
      return this.activationTimings;
   }

   public Timings getGravityTimings() {
      return this.gravityTimings;
   }

   public Timings getAITimings() {
      return this.aiTimings;
   }

   private void startTask() {
      if (this.aiTask == null) {
         int tickPeriod = Settings.entityBehaviorTickPeriod;
         this.aiTask = Bukkit.getScheduler().runTaskTimer(this.plugin, new EntityAI.TickTask(), (long)tickPeriod, (long)tickPeriod);
      }
   }

   private void stopTask() {
      if (this.aiTask != null) {
         this.aiTask.cancel();
         this.aiTask = null;
      }

   }

   private void updateChunkActivations() {
      this.activationTimings.start();
      this.chunks.values().forEach((chunkData) -> {
         chunkData.activeAI = false;
         chunkData.activeGravity = false;
      });
      this.activeAIChunksCount = 0;
      this.activeGravityChunksCount = 0;
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();

         assert player != null;

         this.activateNearbyChunks(player);
      }

      this.activationTimings.stop();
   }

   private void activateNearbyChunks(Player player) {
      World world = player.getWorld();
      Location location = (Location)Unsafe.assertNonNull(player.getLocation(sharedLocation));
      int chunkX = ChunkCoords.fromBlock(location.getBlockX());
      int chunkZ = ChunkCoords.fromBlock(location.getBlockZ());
      this.activateNearbyChunks(world, chunkX, chunkZ, 1, EntityAI.ActivationType.AI);
      if (this.customGravityEnabled) {
         assert Settings.gravityChunkRange >= 0;

         this.activateNearbyChunks(world, chunkX, chunkZ, Settings.gravityChunkRange, EntityAI.ActivationType.GRAVITY);
      }

      sharedLocation.setWorld((World)null);
   }

   private void activateNearbyChunksDelayed(Player player) {
      if (player.isOnline()) {
         Bukkit.getScheduler().runTask(this.plugin, new EntityAI.ActivateNearbyChunksDelayedTask(player));
      }
   }

   private void activateNearbyChunks(World world, int centerChunkX, int centerChunkZ, int chunkRadius, EntityAI.ActivationType activationType) {
      assert world != null && chunkRadius >= 0 && activationType != null;

      String worldName = world.getName();
      int minChunkX = centerChunkX - chunkRadius;
      int maxChunkX = centerChunkX + chunkRadius;
      int minChunkZ = centerChunkZ - chunkRadius;
      int maxChunkZ = centerChunkZ + chunkRadius;

      for(int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
         for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
            sharedChunkCoords.set(worldName, chunkX, chunkZ);
            EntityAI.ChunkData chunkData = (EntityAI.ChunkData)this.chunks.get(sharedChunkCoords);
            if (chunkData != null) {
               switch(activationType.ordinal()) {
               case 0:
                  if (!chunkData.activeGravity) {
                     chunkData.activeGravity = true;
                     ++this.activeGravityChunksCount;
                  }
                  break;
               case 1:
                  if (!chunkData.activeAI) {
                     chunkData.activeAI = true;
                     ++this.activeAIChunksCount;
                  }
                  break;
               default:
                  throw new IllegalStateException("Unexpected activation type: " + String.valueOf(activationType));
               }
            }
         }
      }

   }

   private void processEntities() {
      this.activeAIEntityCount = 0;
      this.activeGravityEntityCount = 0;
      if (this.activeAIChunksCount != 0 || this.activeGravityChunksCount != 0) {
         this.chunks.values().forEach(this::processEntities);
      }
   }

   private void processEntities(EntityAI.ChunkData chunkData) {
      assert chunkData != null;

      if (chunkData.activeGravity || chunkData.activeAI) {
         chunkData.entities.forEach(this::processEntity);
      }
   }

   private void processEntity(EntityAI.EntityData entityData) {
      assert entityData != null;

      Entity entity = entityData.shopObject.getEntity();
      if (entity != null) {
         if (!entity.isDead()) {
            EntityAI.ChunkData chunkData = entityData.chunkData;
            this.gravityTimings.resume();
            if (chunkData.activeGravity && entityData.isAffectedByGravity()) {
               ++this.activeGravityEntityCount;
               this.processGravity(entityData);
            }

            this.gravityTimings.pause();
            this.aiTimings.resume();
            if (chunkData.activeAI) {
               ++this.activeAIEntityCount;
               this.processAI(entityData);
            }

            this.aiTimings.pause();
         }
      }
   }

   private boolean _isCustomGravityEnabled() {
      return !Settings.disableGravity && Compat.getProvider().isNoAIDisablingGravity();
   }

   private void processGravity(EntityAI.EntityData entityData) {
      if (entityData.falling || entityData.fallingCheckLimiter.request(Settings.entityBehaviorTickPeriod)) {
         Entity entity = (Entity)Unsafe.assertNonNull(entityData.shopObject.getEntity());
         Location entityLocation = (Location)Unsafe.assertNonNull(entity.getLocation(sharedLocation));
         Set<? extends Material> collidableFluids = EntityUtils.getCollidableFluids(entity.getType());
         if (!collidableFluids.isEmpty()) {
            Block blockAbove = entity.getWorld().getBlockAt(entityLocation.getBlockX(), entityLocation.getBlockY() + 1, entityLocation.getBlockZ());
            if (blockAbove.isLiquid()) {
               collidableFluids = Collections.emptySet();
            }
         }

         entityData.distanceToGround = WorldUtils.getCollisionDistanceToGround(entityLocation, this.gravityCollisionCheckRange, collidableFluids);
         sharedLocation.setWorld((World)null);
         boolean isInAir = entityData.distanceToGround >= 0.01D;
         boolean falling = isInAir && !EntityUtils.canFly(entity.getType());
         entityData.falling = falling;
         if (isInAir && !falling) {
            Compat.getProvider().setOnGround(entity, false);
         } else {
            if (falling) {
               Compat.getProvider().setOnGround(entity, false);
               this.tickFalling(entityData);
            }

            if (!entityData.falling) {
               Compat.getProvider().setOnGround(entity, true);
            }
         }
      }

   }

   private void tickFalling(EntityAI.EntityData entityData) {
      assert entityData.falling && entityData.distanceToGround >= 0.01D;

      Entity entity = (Entity)Unsafe.assertNonNull(entityData.shopObject.getEntity());
      double remainingDistance = entityData.distanceToGround - this.maxFallingDistancePerUpdate;
      double fallingStepSize;
      if (remainingDistance <= 0.01D) {
         fallingStepSize = entityData.distanceToGround;
         entityData.falling = false;
      } else {
         fallingStepSize = this.maxFallingDistancePerUpdate;
      }

      Location newLocation = (Location)Unsafe.assertNonNull(entity.getLocation(sharedLocation));
      newLocation.add(0.0D, -fallingStepSize, 0.0D);
      this.plugin.getForcingEntityTeleporter().teleport(entity, newLocation);
      sharedLocation.setWorld((World)null);
   }

   private void processAI(EntityAI.EntityData entityData) {
      entityData.shopObject.tickAI();
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      this.activateNearbyChunksDelayed(player);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerTeleport(PlayerTeleportEvent event) {
      Location targetLocation = event.getTo();
      if (targetLocation != null) {
         Player player = event.getPlayer();
         this.activateNearbyChunksDelayed(player);
      }
   }

   private static class ChunkData {
      private final ChunkCoords chunkCoords;
      private final List<EntityAI.EntityData> entities = new ArrayList();
      public boolean activeGravity;
      public boolean activeAI = true;

      public ChunkData(ChunkCoords chunkCoords, boolean activeGravity) {
         this.chunkCoords = chunkCoords;
         this.activeGravity = activeGravity;
      }
   }

   private static class EntityData {
      private final BaseEntityShopObject<?> shopObject;
      private final EntityAI.ChunkData chunkData;
      public final RateLimiter fallingCheckLimiter;
      public boolean falling;
      public double distanceToGround;

      public EntityData(BaseEntityShopObject<?> shopObject, EntityAI.ChunkData chunkData) {
         this.fallingCheckLimiter = new RateLimiter(10, EntityAI.nextFallingCheckOffset.getAndIncrement());
         this.falling = false;
         this.distanceToGround = 0.0D;
         this.shopObject = shopObject;
         this.chunkData = chunkData;
      }

      public boolean isAffectedByGravity() {
         switch(this.shopObject.getEntityType()) {
         case SHULKER:
            return false;
         default:
            return true;
         }
      }
   }

   private class TickTask implements Runnable {
      private final RateLimiter aiActivationLimiter = new RateLimiter(30);

      TickTask() {
      }

      public void run() {
         if (!EntityAI.this.shopObjects.isEmpty()) {
            EntityAI.this.currentlyRunning = true;
            EntityAI.this.totalTimings.start();
            EntityAI.this.gravityTimings.startPaused();
            EntityAI.this.aiTimings.startPaused();
            if (this.aiActivationLimiter.request(Settings.entityBehaviorTickPeriod)) {
               EntityAI.this.updateChunkActivations();
            }

            EntityAI.this.processEntities();
            EntityAI.this.totalTimings.stop();
            EntityAI.this.gravityTimings.stop();
            EntityAI.this.aiTimings.stop();
            EntityAI.this.currentlyRunning = false;
         }
      }
   }

   private static enum ActivationType {
      GRAVITY,
      AI;

      // $FF: synthetic method
      private static EntityAI.ActivationType[] $values() {
         return new EntityAI.ActivationType[]{GRAVITY, AI};
      }
   }

   private class ActivateNearbyChunksDelayedTask implements Runnable {
      private final Player player;

      ActivateNearbyChunksDelayedTask(Player param2) {
         assert player != null;

         this.player = player;
      }

      public void run() {
         if (this.player.isOnline()) {
            EntityAI.this.activateNearbyChunks(this.player);
         }
      }
   }
}
