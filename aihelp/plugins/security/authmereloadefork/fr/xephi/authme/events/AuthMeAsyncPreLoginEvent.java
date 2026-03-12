package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class AuthMeAsyncPreLoginEvent extends CustomEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private boolean canLogin = true;

   public AuthMeAsyncPreLoginEvent(Player player, boolean isAsync) {
      super(isAsync);
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean canLogin() {
      return this.canLogin;
   }

   public void setCanLogin(boolean canLogin) {
      this.canLogin = canLogin;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
