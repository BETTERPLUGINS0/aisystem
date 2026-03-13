package me.ag4.playershop.events;

import me.ag4.playershop.objects.Shop;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopBreakEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final Shop shop;
   private final Block block;
   private final Player player;
   public boolean cancel;

   public ShopBreakEvent(Player player, Block block, Shop shop) {
      this.player = player;
      this.block = block;
      this.shop = shop;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Block getBlock() {
      return this.block;
   }

   public Shop getShop() {
      return this.shop;
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
