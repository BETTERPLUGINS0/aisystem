package me.ag4.playershop.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopPlaceEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Block block;
   private final Player player;
   private boolean cancel;

   public ShopPlaceEvent(Player player, Block block) {
      this.player = player;
      this.block = block;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Block getBlock() {
      return this.block;
   }

   public void setCancelled(boolean cancel) {
      this.cancel = cancel;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
