package com.nisovin.shopkeepers.api.events;

import com.google.common.base.Preconditions;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerShopkeeperHireEvent extends ShopkeeperEvent implements Cancellable {
   private final Player player;
   private final ItemStack[] newPlayerInventoryContents;
   private int maxShopsLimit;
   private boolean cancelled = false;
   private static final HandlerList handlers = new HandlerList();

   public PlayerShopkeeperHireEvent(PlayerShopkeeper shopkeeper, Player player, ItemStack[] newPlayerInventoryContents, int maxShopsLimit) {
      super(shopkeeper);
      Preconditions.checkNotNull(player, "player is null");
      Preconditions.checkNotNull(newPlayerInventoryContents, "newPlayerInventoryContents is null");
      Preconditions.checkArgument(maxShopsLimit >= 0, "maxShopsLimit cannot be negative");
      this.player = player;
      this.newPlayerInventoryContents = newPlayerInventoryContents;
      this.maxShopsLimit = maxShopsLimit;
   }

   public PlayerShopkeeper getShopkeeper() {
      return (PlayerShopkeeper)super.getShopkeeper();
   }

   public Player getPlayer() {
      return this.player;
   }

   public ItemStack[] getNewPlayerInventoryContents() {
      return this.newPlayerInventoryContents;
   }

   public int getMaxShopsLimit() {
      return this.maxShopsLimit;
   }

   public void setMaxShopsLimit(int maxShopsLimit) {
      Preconditions.checkArgument(maxShopsLimit >= 0, "maxShopsLimit cannot be negative");
      this.maxShopsLimit = maxShopsLimit;
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
