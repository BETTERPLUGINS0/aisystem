package me.frep.vulcan.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VulcanPunishmentLogCreateEvent extends Event implements Cancellable {
   private boolean cancelled;
   private final Player player;
   private static final HandlerList handlers = new HandlerList();

   public VulcanPunishmentLogCreateEvent(Player var1) {
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
}
