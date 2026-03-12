package fr.xephi.authme.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class UnregisterByAdminEvent extends AbstractUnregisterEvent {
   private static final HandlerList handlers = new HandlerList();
   private final String playerName;
   private final CommandSender initiator;

   public UnregisterByAdminEvent(Player player, String playerName, boolean isAsync, CommandSender initiator) {
      super(player, isAsync);
      this.playerName = playerName;
      this.initiator = initiator;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public CommandSender getInitiator() {
      return this.initiator;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
