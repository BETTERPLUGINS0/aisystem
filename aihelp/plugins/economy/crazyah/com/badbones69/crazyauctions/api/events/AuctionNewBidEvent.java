package com.badbones69.crazyauctions.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AuctionNewBidEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final long bid;
   private final ItemStack item;

   public AuctionNewBidEvent(Player player, ItemStack item, long bid) {
      this.player = player;
      this.item = item;
      this.bid = bid;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public Player getPlayer() {
      return this.player;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public long getBid() {
      return this.bid;
   }
}
