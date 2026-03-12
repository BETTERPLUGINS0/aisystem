package com.nisovin.shopkeepers.shopobjects.block.base;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopkeeperMetadata;
import com.nisovin.shopkeepers.shopobjects.block.AbstractBlockShopObject;
import com.nisovin.shopkeepers.util.java.CyclicCounter;
import com.nisovin.shopkeepers.util.java.RateLimiter;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.concurrent.TimeUnit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BaseBlockShopObject extends AbstractBlockShopObject {
   private static final int CHECK_PERIOD_SECONDS = 10;
   private static final CyclicCounter nextCheckingOffset = new CyclicCounter(1, 11);
   private static final long RESPAWN_TIMEOUT_MILLIS;
   private final BaseBlockShops blockShops;
   private final RateLimiter checkLimiter;
   @Nullable
   private Block block;
   private long lastFailedRespawnAttemptMillis;

   protected BaseBlockShopObject(BaseBlockShops blockShops, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(shopkeeper, creationData);
      this.checkLimiter = new RateLimiter(10, nextCheckingOffset.getAndIncrement());
      this.block = null;
      this.lastFailedRespawnAttemptMillis = 0L;
      this.blockShops = blockShops;
   }

   public BaseBlockShops getBlockShops() {
      return this.blockShops;
   }

   @Nullable
   public Block getBlock() {
      return this.block;
   }

   protected abstract boolean isValidBlockType(Material var1);

   @Nullable
   public abstract BlockFace getAttachedBlockFace();

   public boolean isActive() {
      Block block = this.getBlock();
      if (block == null) {
         return false;
      } else {
         assert ((ChunkCoords)Unsafe.assertNonNull(this.shopkeeper.getChunkCoords())).isChunkLoaded();

         return this.isValidBlockType(block.getType());
      }
   }

   public boolean spawn() {
      if (this.block != null) {
         return true;
      } else {
         Location spawnLocation = this.shopkeeper.getLocation();
         if (spawnLocation == null) {
            this.onSpawnFailed();
            return false;
         } else if (System.currentTimeMillis() - this.lastFailedRespawnAttemptMillis < RESPAWN_TIMEOUT_MILLIS) {
            Log.debug(() -> {
               return this.shopkeeper.getLocatedLogPrefix() + "Spawn cooldown.";
            });
            this.onSpawnFailed();
            return false;
         } else {
            Block spawnBlock = spawnLocation.getBlock();
            BlockData blockData = this.createBlockData();
            if (blockData == null) {
               Log.debug(() -> {
                  return this.shopkeeper.getLocatedLogPrefix() + "Failed to create block data.";
               });
               this.onSpawnFailed();
               return false;
            } else {
               this.blockShops.addBlockPhysicsCancellation(spawnBlock);
               spawnBlock.setBlockData(blockData, false);
               if (!this.isValidBlockType(spawnBlock.getType())) {
                  this.lastFailedRespawnAttemptMillis = System.currentTimeMillis();
                  this.cleanUpBlock(spawnBlock);
                  this.onSpawnFailed();
                  return false;
               } else {
                  this.block = spawnBlock;
                  ShopkeeperMetadata.apply(this.block);
                  this.updateBlock();
                  this.onIdChanged();
                  this.onSpawnSucceeded();
                  return true;
               }
            }
         }
      }
   }

   @Nullable
   protected abstract BlockData createBlockData();

   protected abstract void updateBlock();

   public void despawn() {
      Block block = this.block;
      if (block != null) {
         block.setType(Material.AIR, false);
         this.block = null;
         this.cleanUpBlock(block);
         this.onIdChanged();
      }
   }

   protected void cleanUpBlock(Block block) {
      assert block != null;

      ShopkeeperMetadata.remove(block);
      this.blockShops.removeBlockPhysicsCancellation(block);
   }

   public boolean move() {
      return !this.isSpawned() ? false : this.respawn();
   }

   public void onTick() {
      super.onTick();
      if (this.checkLimiter.request()) {
         if (this.isSpawningScheduled()) {
            Log.debug(DebugOptions.regularTickActivities, () -> {
               return this.shopkeeper.getLogPrefix() + "Spawning is scheduled. Skipping block check.";
            });
         } else {
            this.indicateTickActivity();

            assert ((ChunkCoords)Unsafe.assertNonNull(this.shopkeeper.getChunkCoords())).isChunkLoaded();

            if (!this.isActive()) {
               Log.debug(() -> {
                  return this.shopkeeper.getLocatedLogPrefix() + "Block is missing! Attempting respawn.";
               });
               this.despawn();
               boolean success = this.spawn();
               if (!success) {
                  Log.warning(this.shopkeeper.getLocatedLogPrefix() + "Block could not be spawned!");
               }

            }
         }
      }
   }

   public void setName(@Nullable String name) {
      this.updateBlock();
   }

   @Nullable
   public String getName() {
      return null;
   }

   public void onShopOwnerChanged() {
      this.updateBlock();
   }

   static {
      RESPAWN_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(3L);
   }
}
