package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerDeleteShopkeeperEvent extends ShopkeeperEvent implements Cancellable {
   private final Player player;
   private boolean cancelled = false;
   private static final HandlerList handlers = new HandlerList();

   public PlayerDeleteShopkeeperEvent(Shopkeeper shopkeeper, Player player) {
      super(shopkeeper);
      Preconditions.checkNotNull(player, "player is null");
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
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
