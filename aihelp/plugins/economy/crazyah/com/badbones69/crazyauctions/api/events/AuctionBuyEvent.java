package com.badbones69.crazyauctions.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AuctionBuyEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final long price;
   private final ItemStack item;

   public AuctionBuyEvent(Player player, ItemStack item, long price) {
      this.player = player;
      this.item = item;
      this.price = price;
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

   public long getPrice() {
      return this.price;
   }
}
