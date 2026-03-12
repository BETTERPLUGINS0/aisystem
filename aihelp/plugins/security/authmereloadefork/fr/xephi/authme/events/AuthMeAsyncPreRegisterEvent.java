package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class AuthMeAsyncPreRegisterEvent extends CustomEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private boolean canRegister = true;

   public AuthMeAsyncPreRegisterEvent(Player player, boolean isAsync) {
      super(isAsync);
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public boolean canRegister() {
      return this.canRegister;
   }

   public void setCanRegister(boolean canRegister) {
      this.canRegister = canRegister;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
