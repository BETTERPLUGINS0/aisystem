package fr.xephi.authme.events;

import org.bukkit.entity.Player;

public abstract class AbstractUnregisterEvent extends CustomEvent {
   private final Player player;

   public AbstractUnregisterEvent(Player player, boolean isAsync) {
      super(isAsync);
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }
}
