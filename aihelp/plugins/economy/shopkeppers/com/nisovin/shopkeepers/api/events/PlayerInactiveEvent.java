package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.user.User;
import java.util.Collection;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInactiveEvent extends Event implements Cancellable {
   private final User user;
   private final Collection<? extends PlayerShopkeeper> shopkeepers;
   private boolean cancelled = false;
   private static final HandlerList handlers = new HandlerList();

   public PlayerInactiveEvent(User user, Collection<? extends PlayerShopkeeper> shopkeepers) {
      Preconditions.checkNotNull(user, "user is null");
      Preconditions.checkNotNull(shopkeepers, "shopkeepers is null");
      this.user = user;
      this.shopkeepers = shopkeepers;
   }

   public User getUser() {
      return this.user;
   }

   public Collection<? extends PlayerShopkeeper> getShopkeepers() {
      return this.shopkeepers;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancel) {
      this.cancelled = cancel;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
