package com.nisovin.shopkeepers.shopkeeper.registry;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

class ShopkeeperChunkMap {
   private final Map<String, WorldShopkeepers> shopkeepersByWorld;
   private final Set<String> shopkeeperWorldsView;
   private final ShopkeeperChunkMap.ChangeListener changeListener;

   ShopkeeperChunkMap() {
      this(new ShopkeeperChunkMap.ChangeListener());
   }

   ShopkeeperChunkMap(ShopkeeperChunkMap.ChangeListener changeListener) {
      this.shopkeepersByWorld = new LinkedHashMap();
      this.shopkeeperWorldsView = Collections.unmodifiableSet(this.shopkeepersByWorld.keySet());
      Validate.notNull(changeListener, (String)"changeListener is null");
      this.changeListener = changeListener;
   }

   @Nullable
   WorldShopkeepers getWorldShopkeepers(String worldName) {
      return (WorldShopkeepers)this.shopkeepersByWorld.get(worldName);
   }

   @Nullable
   ChunkShopkeepers getChunkShopkeepers(@Nullable ChunkCoords chunkCoords) {
      if (chunkCoords == null) {
         return null;
      } else {
         String worldName = chunkCoords.getWorldName();
         WorldShopkeepers worldShopkeepers = this.getWorldShopkeepers(worldName);
         return worldShopkeepers == null ? null : worldShopkeepers.getChunkShopkeepers(chunkCoords);
      }
   }

   ChunkShopkeepers addShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null && !shopkeeper.isVirtual();

      assert shopkeeper.getLastChunkCoords() == null;

      String worldName = (String)Unsafe.assertNonNull(shopkeeper.getWorldName());
      ChunkCoords shopkeeperChunk = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getChunkCoords());

      assert worldName.equals(shopkeeperChunk.getWorldName());

      WorldShopkeepers worldShopkeepers = (WorldShopkeepers)this.shopkeepersByWorld.computeIfAbsent(worldName, WorldShopkeepers::new);

      assert worldShopkeepers != null;

      ChunkShopkeepers chunkShopkeepers = worldShopkeepers.addShopkeeper(shopkeeper);
      if (worldShopkeepers.getShopkeeperCount() == 1) {
         this.changeListener.onWorldAdded(worldShopkeepers);
      }

      if (chunkShopkeepers.getShopkeepers().size() == 1) {
         this.changeListener.onChunkAdded(chunkShopkeepers);
      }

      this.changeListener.onShopkeeperAdded(shopkeeper, chunkShopkeepers);
      return chunkShopkeepers;
   }

   @Nullable
   ChunkShopkeepers removeShopkeeper(AbstractShopkeeper shopkeeper) {
      return this.removeShopkeeper(shopkeeper, false);
   }

   @Nullable
   private ChunkShopkeepers removeShopkeeper(AbstractShopkeeper shopkeeper, boolean skipWorldCleanup) {
      assert shopkeeper != null && !shopkeeper.isVirtual();

      ChunkCoords lastChunkCoords = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getLastChunkCoords());
      String worldName = lastChunkCoords.getWorldName();
      WorldShopkeepers worldShopkeepers = (WorldShopkeepers)this.shopkeepersByWorld.get(worldName);
      if (worldShopkeepers == null) {
         return null;
      } else {
         ChunkShopkeepers chunkShopkeepers = worldShopkeepers.removeShopkeeper(shopkeeper);
         boolean worldRemoved = false;
         if (!skipWorldCleanup && worldShopkeepers.getShopkeeperCount() == 0) {
            worldRemoved = true;
            this.shopkeepersByWorld.remove(worldName);
         }

         this.changeListener.onShopkeeperRemoved(shopkeeper, chunkShopkeepers);
         if (chunkShopkeepers.getShopkeepers().isEmpty()) {
            this.changeListener.onChunkRemoved(chunkShopkeepers);
         }

         if (worldRemoved) {
            this.changeListener.onWorldRemoved(worldShopkeepers);
         }

         return chunkShopkeepers;
      }
   }

   boolean moveShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      ChunkCoords oldChunk = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getLastChunkCoords());
      ChunkCoords newChunk = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getChunkCoords());
      if (newChunk.equals(oldChunk)) {
         return false;
      } else {
         boolean skipWorldCleanup = oldChunk.getWorldName().equals(newChunk.getWorldName());
         this.removeShopkeeper(shopkeeper, skipWorldCleanup);
         this.addShopkeeper(shopkeeper);
         return true;
      }
   }

   void ensureEmpty() {
      if (!this.shopkeepersByWorld.isEmpty()) {
         Log.warning("Some shopkeepers were not properly removed from the chunk map!");
         this.shopkeepersByWorld.clear();
      }

   }

   public Collection<? extends String> getWorldsWithShopkeepers() {
      return this.shopkeeperWorldsView;
   }

   static class ChangeListener {
      public void onShopkeeperAdded(AbstractShopkeeper shopkeeper, ChunkShopkeepers chunkShopkeepers) {
      }

      public void onShopkeeperRemoved(AbstractShopkeeper shopkeeper, ChunkShopkeepers chunkShopkeepers) {
      }

      public void onWorldAdded(WorldShopkeepers worldShopkeepers) {
      }

      public void onWorldRemoved(WorldShopkeepers worldShopkeepers) {
      }

      public void onChunkAdded(ChunkShopkeepers chunkShopkeepers) {
      }

      public void onChunkRemoved(ChunkShopkeepers chunkShopkeepers) {
      }
   }
}
