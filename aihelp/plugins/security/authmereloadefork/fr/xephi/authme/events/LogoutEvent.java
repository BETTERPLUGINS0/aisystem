package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LogoutEvent extends CustomEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;

   public LogoutEvent(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
