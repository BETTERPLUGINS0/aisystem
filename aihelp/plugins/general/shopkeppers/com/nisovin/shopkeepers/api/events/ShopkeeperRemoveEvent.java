package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.event.HandlerList;

public class ShopkeeperRemoveEvent extends ShopkeeperEvent {
   private final ShopkeeperRemoveEvent.Cause cause;
   private static final HandlerList handlers = new HandlerList();

   public ShopkeeperRemoveEvent(Shopkeeper shopkeeper, ShopkeeperRemoveEvent.Cause cause) {
      super(shopkeeper);
      Preconditions.checkNotNull(cause, "cause is null");
      this.cause = cause;
   }

   public ShopkeeperRemoveEvent.Cause getCause() {
      return this.cause;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public static enum Cause {
      DELETE,
      UNLOAD;

      // $FF: synthetic method
      private static ShopkeeperRemoveEvent.Cause[] $values() {
         return new ShopkeeperRemoveEvent.Cause[]{DELETE, UNLOAD};
      }
   }
}
