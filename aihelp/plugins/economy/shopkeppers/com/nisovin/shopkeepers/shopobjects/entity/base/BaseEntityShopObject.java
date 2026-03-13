package com.nisovin.shopkeepers.shopobjects.entity.base;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.entity.EntityShopObject;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.debug.events.DebugListener;
import com.nisovin.shopkeepers.debug.events.EventDebugListener;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.ShopkeeperMetadata;
import com.nisovin.shopkeepers.shopobjects.entity.AbstractEntityShopObject;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.bukkit.WorldUtils;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.CyclicCounter;
import com.nisovin.shopkeepers.util.java.RateLimiter;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BaseEntityShopObject<E extends Entity> extends AbstractEntityShopObject implements EntityShopObject {
   protected static final double SPAWN_LOCATION_OFFSET = 0.98D;
   protected static final double SPAWN_LOCATION_RANGE = 2.0D;
   protected static final double SPAWN_LOCATION_RANGE_FLYING = 1.0D;
   protected static final int CHECK_PERIOD_SECONDS = 10;
   protected static final int CHECK_PERIOD_TICKS = 200;
   private static final CyclicCounter nextCheckingOffset = new CyclicCounter(1, 11);
   protected static final int MAX_RESPAWN_ATTEMPTS = 5;
   protected static final int THROTTLED_CHECK_PERIOD_SECONDS = 60;
   private static final Location sharedLocation = new Location((World)null, 0.0D, 0.0D, 0.0D);
   protected final BaseEntityShopObjectCreationContext context;
   private final BaseEntityShopObjectType<?> shopObjectType;
   @Nullable
   private E entity;
   @Nullable
   private Location lastSpawnLocation = null;
   private int respawnAttempts = 0;
   private boolean debuggingSpawn = false;
   private static long lastSpawnDebugMillis = 0L;
   private static final long SPAWN_DEBUG_THROTTLE_MILLIS;
   private final int checkingOffset;
   private final RateLimiter checkLimiter;
   private boolean skipRespawnAttemptsIfPeaceful;

   protected BaseEntityShopObject(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<?> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(shopkeeper, creationData);
      this.checkingOffset = nextCheckingOffset.getAndIncrement();
      this.checkLimiter = new RateLimiter(10, this.checkingOffset);
      this.skipRespawnAttemptsIfPeaceful = false;
      this.context = context;
      this.shopObjectType = shopObjectType;
   }

   protected BaseEntityShopObjectCreationContext getContext() {
      return this.context;
   }

   public BaseEntityShopObjectType<?> getType() {
      return this.shopObjectType;
   }

   public final EntityType getEntityType() {
      return this.shopObjectType.getEntityType();
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
   }

   public int updateItems(String logPrefix, @ReadWrite ShopObjectData shopObjectData) {
      int updatedItems = super.updateItems(logPrefix, shopObjectData);
      return updatedItems;
   }

   @Nullable
   public E getEntity() {
      return this.entity;
   }

   @Nullable
   private Location getSpawnLocation() {
      Location spawnLocation = this.shopkeeper.getLocation();
      if (spawnLocation == null) {
         return null;
      } else {
         spawnLocation.add(0.5D, 0.0D, 0.5D);
         if (this.shallAdjustSpawnLocation()) {
            this.adjustSpawnLocation(spawnLocation);
         }

         return spawnLocation;
      }
   }

   protected boolean shallAdjustSpawnLocation() {
      return true;
   }

   private void adjustSpawnLocation(Location spawnLocation) {
      Set<? extends Material> collidableFluids = EntityUtils.getCollidableFluids(this.getEntityType());
      if (!collidableFluids.isEmpty()) {
         World world = (World)Unsafe.assertNonNull(spawnLocation.getWorld());
         Block blockAbove = world.getBlockAt(this.shopkeeper.getX(), this.shopkeeper.getY() + 1, this.shopkeeper.getZ());
         if (blockAbove.isLiquid()) {
            collidableFluids = Collections.emptySet();
         }
      }

      spawnLocation.add(0.0D, 0.98D, 0.0D);
      double spawnLocationRange = EntityUtils.canFly(this.getEntityType()) ? 1.0D : 2.0D;
      double distanceToGround = WorldUtils.getCollisionDistanceToGround(spawnLocation, spawnLocationRange, collidableFluids);
      if (distanceToGround == spawnLocationRange) {
         distanceToGround = 0.98D;
      }

      spawnLocation.add(0.0D, -distanceToGround, 0.0D);
   }

   protected void prepareEntity(@NonNull E entity) {
      ShopkeeperMetadata.apply(entity);
      entity.setPersistent(false);
      this.applyName(entity, this.shopkeeper.getName());
      Compat.getProvider().prepareEntity(entity);
   }

   protected void cleanUpEntity() {
      Entity entity = (Entity)Unsafe.assertNonNull(this.entity);
      this.cleanupAI();
      ShopkeeperMetadata.remove(entity);
      if (!entity.isDead()) {
         entity.remove();
      }

      this.entity = null;
   }

   public boolean spawn() {
      if (this.entity != null) {
         return true;
      } else {
         Location spawnLocation = this.getSpawnLocation();
         if (spawnLocation == null) {
            this.onSpawnFailed();
            return false;
         } else {
            World world = (World)Unsafe.assertNonNull(spawnLocation.getWorld());
            EntityType entityType = this.getEntityType();
            Class<? extends Entity> entityClass = (Class)Unsafe.assertNonNull(entityType.getEntityClass());
            this.entity = world.spawn(spawnLocation, entityClass, false, (entityx) -> {
               assert entityx != null;

               if (entityx.isDead()) {
                  Log.debug("Spawning shopkeeper entity is dead already!");
               }

               this.prepareEntity(entityx);
               this.context.baseEntityShops.forceEntitySpawn(spawnLocation, entityType);
            });
            E entity = this.entity;

            assert entity != null;

            boolean success = this.isActive();
            if (success) {
               this.lastSpawnLocation = spawnLocation;
               Iterator var7 = entity.getPassengers().iterator();

               while(var7.hasNext()) {
                  Entity passenger = (Entity)var7.next();
                  passenger.remove();
               }

               entity.eject();
               entity.setInvulnerable(true);
               Compat.getProvider().setupSpawnedEntity(entity);
               this.overwriteAI();
               this.context.baseEntityShops.getEntityAI().addShopObject(this);
               this.onSpawn();
               this.respawnAttempts = 0;
               this.resetTickRate();
               this.skipRespawnAttemptsIfPeaceful = false;
               this.onIdChanged();
               this.onSpawnSucceeded();
            } else {
               this.onSpawnFailed();
               boolean debug = Settings.debug && !this.debuggingSpawn && entity.isDead() && System.currentTimeMillis() - lastSpawnDebugMillis > SPAWN_DEBUG_THROTTLE_MILLIS && ChunkCoords.isChunkLoaded(entity.getLocation());
               boolean var10000 = entity.isDead();
               Log.debug("Failed to spawn shopkeeper entity: Entity dead: " + var10000 + ", entity valid: " + entity.isValid() + ", chunk loaded: " + ChunkCoords.isChunkLoaded(entity.getLocation()) + ", debug -> " + debug);
               this.cleanUpEntity();
               if (debug) {
                  EntityUtils.printEntityCounts(spawnLocation.getChunk());
                  this.debuggingSpawn = true;
                  lastSpawnDebugMillis = System.currentTimeMillis();
                  Log.info("Trying again and logging event activity ..");
                  DebugListener debugListener = DebugListener.register(true, true);
                  EventDebugListener<EntitySpawnEvent> spawnListener = new EventDebugListener(EntitySpawnEvent.class, (priority, event) -> {
                     Entity spawnedEntity = event.getEntity();
                     String var10000 = String.valueOf(priority);
                     Log.info("  EntitySpawnEvent (" + var10000 + "): cancelled: " + event.isCancelled() + ", dead: " + spawnedEntity.isDead() + ", valid: " + spawnedEntity.isValid() + ", chunk loaded: " + ChunkCoords.isChunkLoaded(spawnedEntity.getLocation()));
                  });
                  success = this.spawn();
                  debugListener.unregister();
                  spawnListener.unregister();
                  this.debuggingSpawn = false;
                  Log.info(".. Done. Successful: " + success);
               }
            }

            return success;
         }
      }
   }

   protected void onSpawn() {
      assert this.getEntity() != null;

   }

   protected void overwriteAI() {
      E entity = (Entity)Unsafe.assertNonNull(this.entity);
      if (Settings.silenceShopEntities) {
         entity.setSilent(true);
      }

      if (Settings.disableGravity) {
         this.setNoGravity(entity);
         Compat.getProvider().setNoclip(entity);
      }

   }

   protected final void setNoGravity(E entity) {
      entity.setGravity(false);
      Compat.getProvider().setOnGround(entity, true);
   }

   protected void cleanupAI() {
      this.context.baseEntityShops.getEntityAI().removeShopObject(this);
   }

   public void despawn() {
      if (this.entity != null) {
         this.cleanUpEntity();
         this.lastSpawnLocation = null;
         this.onIdChanged();
      }
   }

   public boolean move() {
      Entity entity = this.entity;
      if (entity == null) {
         return false;
      } else {
         Location spawnLocation = this.getSpawnLocation();
         if (spawnLocation == null) {
            return false;
         } else {
            this.lastSpawnLocation = spawnLocation;
            boolean teleportSuccess = SKShopkeepersPlugin.getInstance().getForcingEntityTeleporter().teleport(entity, spawnLocation);
            this.context.baseEntityShops.getEntityAI().updateLocation(this);
            return teleportSuccess;
         }
      }
   }

   public void onTick() {
      super.onTick();
      if (this.checkLimiter.request()) {
         if (this.isSpawningScheduled()) {
            Log.debug(DebugOptions.regularTickActivities, () -> {
               return this.shopkeeper.getLogPrefix() + "Spawning is scheduled. Skipping entity check.";
            });
            return;
         }

         this.check();
         this.indicateTickActivity();
      }

   }

   private boolean isTickRateThrottled() {
      return this.checkLimiter.getThreshold() == 60;
   }

   private void throttleTickRate() {
      if (!this.isTickRateThrottled()) {
         Log.debug("Throttling tick rate");
         this.checkLimiter.setThreshold(60);
         this.checkLimiter.setRemainingThreshold(60 + this.checkingOffset);
      }
   }

   private void resetTickRate() {
      this.checkLimiter.setThreshold(10);
      this.checkLimiter.setRemainingThreshold(this.checkingOffset);
   }

   private void check() {
      if (!this.isActive()) {
         this.checkInactive();
      } else {
         this.checkActive();
      }

   }

   protected void checkInactive() {
      this.respawnInactiveEntity();
   }

   protected void checkActive() {
      this.teleportBackIfMoved();
   }

   private boolean respawnInactiveEntity() {
      assert !this.isActive();

      if (this.skipRespawnAttemptsIfPeaceful) {
         Location shopkeeperLocation = this.shopkeeper.getLocation();
         if (shopkeeperLocation != null && LocationUtils.getWorld(shopkeeperLocation).getDifficulty() == Difficulty.PEACEFUL) {
            Log.debug(DebugOptions.regularTickActivities, () -> {
               String var10000 = this.shopkeeper.getLocatedLogPrefix();
               return var10000 + String.valueOf(this.getEntityType()) + " is missing. Skipping respawn attempt due to peaceful difficulty.";
            });
            return false;
         }

         this.skipRespawnAttemptsIfPeaceful = false;
      }

      assert !this.skipRespawnAttemptsIfPeaceful;

      E entity = this.entity;
      if (entity != null) {
         Location entityLocation = entity.getLocation();
         if (ChunkCoords.isSameChunk(this.shopkeeper.getLocation(), entityLocation)) {
            if (entity.isDead() && EntityUtils.isRemovedOnPeacefulDifficulty(this.getEntityType()) && LocationUtils.getWorld(entityLocation).getDifficulty() == Difficulty.PEACEFUL) {
               this.skipRespawnAttemptsIfPeaceful = true;
               String var10000 = this.shopkeeper.getLocatedLogPrefix();
               Log.warning(var10000 + String.valueOf(this.getEntityType()) + " was removed due to the world's difficulty being set to peaceful. Respawn attempts are skipped until the difficulty is changed.");
            } else {
               Log.debug(() -> {
                  String var10000 = this.shopkeeper.getLocatedLogPrefix();
                  return var10000 + String.valueOf(this.getEntityType()) + " was removed. Maybe by another plugin, or the chunk was silently unloaded. (dead: " + entity.isDead() + ", valid: " + entity.isValid() + ", chunk loaded: " + ChunkCoords.isChunkLoaded(entityLocation) + ")";
               });
            }
         }

         this.despawn();
         if (this.skipRespawnAttemptsIfPeaceful) {
            return false;
         }
      }

      Log.debug(() -> {
         String var10000 = this.shopkeeper.getLocatedLogPrefix();
         return var10000 + String.valueOf(this.getEntityType()) + " is missing. Attempting respawn.";
      });
      boolean spawned = this.spawn();
      if (!spawned) {
         Log.debug("  Respawn failed");
         ++this.respawnAttempts;
         if (this.respawnAttempts >= 5) {
            this.throttleTickRate();
         }
      }

      return spawned;
   }

   private void teleportBackIfMoved() {
      assert this.isActive();

      E entity = (Entity)Unsafe.assertNonNull(this.entity);
      Location entityLoc = (Location)Unsafe.assertNonNull(entity.getLocation(sharedLocation));
      Location lastSpawnLocation = (Location)Unsafe.assertNonNull(this.lastSpawnLocation);
      if (LocationUtils.getDistanceSquared(entityLoc, lastSpawnLocation) > 0.2D) {
         Log.debug(DebugOptions.regularTickActivities, () -> {
            String var10000 = this.shopkeeper.getLocatedLogPrefix();
            return var10000 + "Entity moved (" + TextUtils.getLocationString(entityLoc) + "). Teleporting back.";
         });
         Location spawnLocation = (Location)Unsafe.assertNonNull(this.getSpawnLocation());
         spawnLocation.setYaw(entityLoc.getYaw());
         spawnLocation.setPitch(entityLoc.getPitch());
         this.lastSpawnLocation = spawnLocation;
         SKShopkeepersPlugin.getInstance().getForcingEntityTeleporter().teleport(entity, spawnLocation);
         this.overwriteAI();
      }

      sharedLocation.setWorld((World)null);
   }

   public void teleportBack() {
      E entity = this.getEntity();
      if (entity != null) {
         Location lastSpawnLocation = (Location)Unsafe.assertNonNull(this.lastSpawnLocation);
         Location entityLoc = entity.getLocation();
         lastSpawnLocation.setYaw(entityLoc.getYaw());
         lastSpawnLocation.setPitch(entityLoc.getPitch());
         SKShopkeepersPlugin.getInstance().getForcingEntityTeleporter().teleport(entity, lastSpawnLocation);
      }
   }

   public void tickAI() {
      Entity entity = this.getEntity();
      if (entity != null) {
         ;
      }
   }

   public void setName(String name) {
      E entity = this.entity;
      if (entity != null) {
         this.applyName(entity, name);
      }
   }

   protected void applyName(@NonNull E entity, @Nullable String name) {
      if (Settings.showNameplates && name != null && !name.isEmpty()) {
         String preparedName = this.prepareName(Messages.nameplatePrefix + name);
         entity.setCustomName(preparedName);
         entity.setCustomNameVisible(Settings.alwaysShowNameplates);
      } else {
         entity.setCustomName((String)null);
         entity.setCustomNameVisible(false);
      }

   }

   public String getName() {
      E entity = this.entity;
      return entity == null ? null : entity.getCustomName();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      return editorButtons;
   }

   static {
      SPAWN_DEBUG_THROTTLE_MILLIS = TimeUnit.MINUTES.toMillis(5L);
   }
}
