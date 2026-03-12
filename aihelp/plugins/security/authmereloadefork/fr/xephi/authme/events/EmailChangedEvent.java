package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EmailChangedEvent extends CustomEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final String oldEmail;
   private final String newEmail;
   private boolean isCancelled;

   public EmailChangedEvent(Player player, String oldEmail, String newEmail, boolean isAsync) {
      super(isAsync);
      this.player = player;
      this.oldEmail = oldEmail;
      this.newEmail = newEmail;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getOldEmail() {
      return this.oldEmail;
   }

   public String getNewEmail() {
      return this.newEmail;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }
}
