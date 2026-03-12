package me.frep.vulcan.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanGhostBlockEvent extends Event implements Cancellable {
   private final Player player;
   private boolean cancelled;
   private final long timestamp = System.currentTimeMillis();
   private static final HandlerList handlers = new HandlerList();

   public VulcanGhostBlockEvent(Player var1) {
      super(true);
      this.player = var1;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean var1) {
      this.cancelled = var1;
   }

   public Player getPlayer() {
      return this.player;
   }

   public long getTimestamp() {
      return this.timestamp;
   }
}
