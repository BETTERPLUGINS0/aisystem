package com.nisovin.shopkeepers.shopkeeper.registry;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.activation.ShopkeeperChunkActivator;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ActiveChunkQueries {
   private final ShopkeeperChunkMap shopkeeperChunkMap;
   private final ShopkeeperChunkActivator shopkeeperActivator;
   private final Set<? extends AbstractShopkeeper> shopkeepersInActiveChunksView = new AbstractSet<AbstractShopkeeper>() {
      public Iterator<AbstractShopkeeper> iterator() {
         return ((ShopkeeperChunkMap)Unsafe.assertNonNull(ActiveChunkQueries.this.shopkeeperChunkMap)).getWorldsWithShopkeepers().stream().flatMap((worldName) -> {
            return ((ActiveChunkQueries)Unsafe.initialized(ActiveChunkQueries.this)).getShopkeepersInActiveChunks(worldName).stream();
         }).iterator();
      }

      public int size() {
         return ((ShopkeeperChunkMap)Unsafe.assertNonNull(ActiveChunkQueries.this.shopkeeperChunkMap)).getWorldsWithShopkeepers().stream().mapToInt((worldName) -> {
            return ((ActiveChunkQueries)Unsafe.initialized(ActiveChunkQueries.this)).getShopkeepersInActiveChunks(worldName).size();
         }).sum();
      }
   };

   ActiveChunkQueries(ShopkeeperChunkMap shopkeeperChunkMap, ShopkeeperChunkActivator shopkeeperActivator) {
      Validate.notNull(shopkeeperChunkMap, (String)"shopkeeperChunkMap is null");
      Validate.notNull(shopkeeperActivator, (String)"shopkeeperActivator is null");
      this.shopkeeperChunkMap = shopkeeperChunkMap;
      this.shopkeeperActivator = shopkeeperActivator;
   }

   private boolean isChunkActive(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      return this.shopkeeperActivator.isChunkActive(chunkCoords);
   }

   public Set<? extends AbstractShopkeeper> getShopkeepersInActiveChunks() {
      return this.shopkeepersInActiveChunksView;
   }

   public Set<? extends ChunkCoords> getActiveChunks(String worldName) {
      final WorldShopkeepers worldShopkeepers = this.shopkeeperChunkMap.getWorldShopkeepers(worldName);
      if (worldShopkeepers == null) {
         return Collections.emptySet();
      } else {
         Set<? extends ChunkCoords> activeChunksView = new AbstractSet<ChunkCoords>() {
            public Iterator<ChunkCoords> iterator() {
               return (Iterator)Unsafe.cast(worldShopkeepers.getShopkeepersByChunk().keySet().stream().filter(ActiveChunkQueries.this::isChunkActive).iterator());
            }

            public int size() {
               return worldShopkeepers.getShopkeepersByChunk().keySet().stream().filter(ActiveChunkQueries.this::isChunkActive).mapToInt((chunkCoords) -> {
                  return 1;
               }).sum();
            }
         };
         return activeChunksView;
      }
   }

   public Set<? extends AbstractShopkeeper> getShopkeepersInActiveChunks(String worldName) {
      final WorldShopkeepers worldShopkeepers = this.shopkeeperChunkMap.getWorldShopkeepers(worldName);
      if (worldShopkeepers == null) {
         return Collections.emptySet();
      } else {
         Set<? extends AbstractShopkeeper> shopkeepersInActiveChunksView = new AbstractSet<AbstractShopkeeper>() {
            public Iterator<AbstractShopkeeper> iterator() {
               return worldShopkeepers.getShopkeepersByChunk().entrySet().stream().filter((chunkEntry) -> {
                  return ActiveChunkQueries.this.isChunkActive((ChunkCoords)chunkEntry.getKey());
               }).flatMap((chunkEntry) -> {
                  return ((List)chunkEntry.getValue()).stream();
               }).iterator();
            }

            public int size() {
               return worldShopkeepers.getShopkeepersByChunk().entrySet().stream().filter((chunkEntry) -> {
                  return ActiveChunkQueries.this.isChunkActive((ChunkCoords)chunkEntry.getKey());
               }).mapToInt((chunkEntry) -> {
                  return ((List)chunkEntry.getValue()).size();
               }).sum();
            }
         };
         return shopkeepersInActiveChunksView;
      }
   }
}
