package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.event.Event;

public abstract class ShopkeeperEvent extends Event {
   private final Shopkeeper shopkeeper;

   protected ShopkeeperEvent(Shopkeeper shopkeeper) {
      Preconditions.checkNotNull(shopkeeper, "shopkeeper is null");
      this.shopkeeper = shopkeeper;
   }

   public Shopkeeper getShopkeeper() {
      return this.shopkeeper;
   }
}
