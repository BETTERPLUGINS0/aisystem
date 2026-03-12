package fr.xephi.authme.task;

import fr.xephi.authme.data.auth.PlayerCache;
import org.bukkit.entity.Player;

public class TimeoutTask implements Runnable {
   private final Player player;
   private final String message;
   private final PlayerCache playerCache;

   public TimeoutTask(Player player, String message, PlayerCache playerCache) {
      this.message = message;
      this.player = player;
      this.playerCache = playerCache;
   }

   public void run() {
      if (!this.playerCache.isAuthenticated(this.player.getName())) {
         this.player.kickPlayer(this.message);
      }

   }
}
