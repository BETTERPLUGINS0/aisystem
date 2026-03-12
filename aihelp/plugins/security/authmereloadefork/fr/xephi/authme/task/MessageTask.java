package fr.xephi.authme.task;

import fr.xephi.authme.libs.com.github.Anon8281.universalScheduler.UniversalRunnable;
import org.bukkit.entity.Player;

public class MessageTask extends UniversalRunnable {
   private final Player player;
   private final String[] message;
   private boolean isMuted;

   public MessageTask(Player player, String[] lines) {
      this.player = player;
      this.message = lines;
      this.isMuted = false;
   }

   public void setMuted(boolean isMuted) {
      this.isMuted = isMuted;
   }

   public void run() {
      if (!this.isMuted) {
         this.player.sendMessage(this.message);
      }

   }
}
