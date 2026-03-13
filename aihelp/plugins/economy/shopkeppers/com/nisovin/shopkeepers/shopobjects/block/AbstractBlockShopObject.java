package com.nisovin.shopkeepers.shopobjects.block;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.block.BlockShopObject;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractBlockShopObject extends AbstractShopObject implements BlockShopObject {
   protected AbstractBlockShopObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(shopkeeper, creationData);
   }

   public abstract AbstractBlockShopObjectType<?> getType();

   public boolean isSpawned() {
      return this.getBlock() != null;
   }

   public boolean isActive() {
      return this.isSpawned();
   }

   @Nullable
   public Location getLocation() {
      Block block = this.getBlock();
      return block != null ? block.getLocation() : null;
   }

   @Nullable
   public Object getId() {
      Block block = this.getBlock();
      return block == null ? null : BlockShopObjectIds.getObjectId(block);
   }
}
