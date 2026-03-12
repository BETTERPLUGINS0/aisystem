package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;

public class PlayerCreatePlayerShopkeeperEvent extends PlayerCreateShopkeeperEvent {
   private int maxShopsLimit;

   public PlayerCreatePlayerShopkeeperEvent(ShopCreationData creationData, int maxShopsLimit) {
      super(creationData);
      Preconditions.checkArgument(maxShopsLimit >= 0, "maxShopsLimit cannot be negative");
      this.maxShopsLimit = maxShopsLimit;
   }

   public int getMaxShopsLimit() {
      return this.maxShopsLimit;
   }

   public void setMaxShopsLimit(int maxShopsLimit) {
      Preconditions.checkArgument(maxShopsLimit >= 0, "maxShopsLimit cannot be negative");
      this.maxShopsLimit = maxShopsLimit;
   }
}
