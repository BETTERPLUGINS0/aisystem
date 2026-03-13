package com.nisovin.shopkeepers.shopkeeper.registry;

import com.nisovin.shopkeepers.api.util.ChunkCoords;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ChunkShopkeepers {
   private final ChunkCoords chunkCoords;
   private final List<AbstractShopkeeper> shopkeepers = new ArrayList();
   private final List<? extends AbstractShopkeeper> shopkeepersView;
   @Nullable
   private List<? extends AbstractShopkeeper> shopkeepersSnapshot;

   ChunkShopkeepers(ChunkCoords chunkCoords) {
      this.shopkeepersView = Collections.unmodifiableList(this.shopkeepers);
      this.shopkeepersSnapshot = null;
      Validate.notNull(chunkCoords, (String)"chunkCoords is null");
      this.chunkCoords = chunkCoords;
   }

   public ChunkCoords getChunkCoords() {
      return this.chunkCoords;
   }

   void addShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      assert shopkeeper.getLastChunkCoords() == null;

      assert this.getChunkCoords().equals(shopkeeper.getChunkCoords());

      assert !this.getShopkeepers().contains(shopkeeper);

      this.shopkeepers.add(shopkeeper);
      shopkeeper.setLastChunkCoords(this.chunkCoords);
      this.shopkeepersSnapshot = null;
   }

   void removeShopkeeper(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      assert this.getChunkCoords().equals(shopkeeper.getLastChunkCoords());

      assert this.getShopkeepers().contains(shopkeeper);

      this.shopkeepers.remove(shopkeeper);
      shopkeeper.setLastChunkCoords((ChunkCoords)null);
      this.shopkeepersSnapshot = null;
   }

   public List<? extends AbstractShopkeeper> getShopkeepers() {
      return this.shopkeepersView;
   }

   public List<? extends AbstractShopkeeper> getShopkeepersSnapshot() {
      if (this.shopkeepersSnapshot == null) {
         this.shopkeepersSnapshot = Collections.unmodifiableList(new ArrayList(this.shopkeepers));
      }

      assert this.shopkeepersSnapshot != null;

      return this.shopkeepersSnapshot;
   }
}
