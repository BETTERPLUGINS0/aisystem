package com.badbones69.crazyauctions.api.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AuctionExpireEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final boolean isOnline;
   private final ItemStack item;
   private OfflinePlayer offlinePlayer;
   private Player onlinePlayer;

   public AuctionExpireEvent(OfflinePlayer offlinePlayer, ItemStack item) {
      this.offlinePlayer = offlinePlayer;
      this.item = item;
      this.isOnline = false;
   }

   public AuctionExpireEvent(Player onlinePlayer, ItemStack item) {
      this.onlinePlayer = onlinePlayer;
      this.item = item;
      this.isOnline = true;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public OfflinePlayer getOfflinePlayer() {
      return this.offlinePlayer;
   }

   public Player getOnlinePlayer() {
      return this.onlinePlayer;
   }

   public boolean isOnline() {
      return this.isOnline;
   }

   public ItemStack getItem() {
      return this.item;
   }
}
