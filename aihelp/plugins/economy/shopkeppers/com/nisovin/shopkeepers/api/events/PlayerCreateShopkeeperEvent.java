package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCreateShopkeeperEvent extends Event implements Cancellable {
   private final ShopCreationData creationData;
   private boolean cancelled = false;
   private static final HandlerList handlers = new HandlerList();

   public PlayerCreateShopkeeperEvent(ShopCreationData creationData) {
      Preconditions.checkNotNull(creationData, "creationData is null");
      this.creationData = creationData;
   }

   public ShopCreationData getShopCreationData() {
      return this.creationData;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
