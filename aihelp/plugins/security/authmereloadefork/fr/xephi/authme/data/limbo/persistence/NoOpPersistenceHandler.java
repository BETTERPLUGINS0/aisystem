package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.data.limbo.LimboPlayer;
import org.bukkit.entity.Player;

class NoOpPersistenceHandler implements LimboPersistenceHandler {
   public LimboPlayer getLimboPlayer(Player player) {
      return null;
   }

   public void saveLimboPlayer(Player player, LimboPlayer limbo) {
   }

   public void removeLimboPlayer(Player player) {
   }

   public LimboPersistenceType getType() {
      return LimboPersistenceType.DISABLED;
   }
}
