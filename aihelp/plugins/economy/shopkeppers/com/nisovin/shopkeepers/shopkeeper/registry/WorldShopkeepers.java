package com.nisovin.shopkeepers.shopkeeper.registry;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

final class WorldShopkeepers {
   private final String worldName;
   private final Map<ChunkCoords, ChunkShopkeepers> shopkeepersByChunk = new HashMap();
   private final Map<ChunkCoords, List<? extends AbstractShopkeeper>> shopkeeperViewsByChunk = new LinkedHashMap();
   private final Map<ChunkCoords, List<? extends AbstractShopkeeper>> shopkeepersByChunkView;
   private int shopkeeperCount;
   private final Set<? extends AbstractShopkeeper> shopkeepersView;

   WorldShopkeepers(String worldName) {
      this.shopkeepersByChunkView = Collections.unmodifiableMap(this.shopkeeperViewsByChunk);
      this.shopkeeperCount = 0;
      this.shopkeepersView = new AbstractSet<AbstractShopkeeper>() {
         public Iterator<AbstractShopkeeper> iterator() {
            return this.isEmpty() ? Collections.emptyIterator() : WorldShopkeepers.this.shopkeepersByChunkView.values().stream().flatMap(Collection::stream).iterator();
         }

         public int size() {
            return WorldShopkeepers.this.shopkeeperCount;
         }
      };
      Validate.notEmpty(worldName, "worldName is null or empty");
      this.worldName = worldName;
   }

   public String getWorldName() {
      return this.worldName;
   }

   @Nullable
   ChunkShopkeepers getChunkShopkeepers(ChunkCoords chunkCoords) {
      assert chunkCoords != null;

      assert chunkCoords.getWorldName().equals(this.getWorldName());

      return (ChunkShopkeepers)this.shopkeepersByChunk.get(chunkCoords);
   }

   ChunkShopkeepers addShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      assert shopkeeper.getLastChunkCoords() == null;

      ChunkCoords chunkCoords = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getChunkCoords());

      assert chunkCoords.getWorldName().equals(this.getWorldName());

      ChunkShopkeepers chunkShopkeepers = (ChunkShopkeepers)this.shopkeepersByChunk.computeIfAbsent(chunkCoords, (chkCoords) -> {
         ChunkShopkeepers newChunkShopkeepers = new ChunkShopkeepers(chkCoords);
         this.shopkeeperViewsByChunk.put(chkCoords, newChunkShopkeepers.getShopkeepers());
         return newChunkShopkeepers;
      });

      assert chunkShopkeepers != null;

      assert !chunkShopkeepers.getShopkeepers().contains(shopkeeper);

      chunkShopkeepers.addShopkeeper(shopkeeper);
      ++this.shopkeeperCount;
      return chunkShopkeepers;
   }

   ChunkShopkeepers removeShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      ChunkCoords chunkCoords = (ChunkCoords)Unsafe.assertNonNull(shopkeeper.getLastChunkCoords());

      assert chunkCoords.getWorldName().equals(this.getWorldName());

      ChunkShopkeepers chunkShopkeepers = (ChunkShopkeepers)Unsafe.assertNonNull((ChunkShopkeepers)this.shopkeepersByChunk.get(chunkCoords));

      assert chunkShopkeepers.getShopkeepers().contains(shopkeeper);

      chunkShopkeepers.removeShopkeeper(shopkeeper);
      --this.shopkeeperCount;
      if (chunkShopkeepers.getShopkeepers().isEmpty()) {
         this.shopkeepersByChunk.remove(chunkCoords);
         this.shopkeeperViewsByChunk.remove(chunkCoords);
      }

      return chunkShopkeepers;
   }

   public int getShopkeeperCount() {
      return this.shopkeeperCount;
   }

   public Set<? extends AbstractShopkeeper> getShopkeepers() {
      return this.shopkeepersView;
   }

   public Map<? extends ChunkCoords, ? extends List<? extends AbstractShopkeeper>> getShopkeepersByChunk() {
      return this.shopkeepersByChunkView;
   }
}
