package fr.xephi.authme.process.register.executors;

import org.bukkit.entity.Player;

public abstract class RegistrationParameters {
   private final Player player;

   public RegistrationParameters(Player player) {
      this.player = player;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getPlayerName() {
      return this.player.getName();
   }
}
