package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class ShopkeeperEditedEvent extends ShopkeeperEvent {
   private final Player player;
   private static final HandlerList handlers = new HandlerList();

   public ShopkeeperEditedEvent(Shopkeeper shopkeeper, Player player) {
      super(shopkeeper);
      Preconditions.checkNotNull(player, "player is null");
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
