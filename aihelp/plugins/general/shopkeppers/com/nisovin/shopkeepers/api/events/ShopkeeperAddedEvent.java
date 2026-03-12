package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.event.HandlerList;

public class ShopkeeperAddedEvent extends ShopkeeperEvent {
   private final ShopkeeperAddedEvent.Cause cause;
   private static final HandlerList handlers = new HandlerList();

   public ShopkeeperAddedEvent(Shopkeeper shopkeeper, ShopkeeperAddedEvent.Cause cause) {
      super(shopkeeper);
      Preconditions.checkNotNull(cause, "cause is null");
      this.cause = cause;
   }

   public ShopkeeperAddedEvent.Cause getCause() {
      return this.cause;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public static enum Cause {
      CREATED,
      LOADED;

      // $FF: synthetic method
      private static ShopkeeperAddedEvent.Cause[] $values() {
         return new ShopkeeperAddedEvent.Cause[]{CREATED, LOADED};
      }
   }
}
