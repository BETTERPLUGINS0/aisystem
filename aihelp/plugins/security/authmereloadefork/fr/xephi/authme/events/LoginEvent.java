package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LoginEvent extends CustomEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;

   public LoginEvent(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   /** @deprecated */
   @Deprecated
   public boolean isLogin() {
      return true;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
