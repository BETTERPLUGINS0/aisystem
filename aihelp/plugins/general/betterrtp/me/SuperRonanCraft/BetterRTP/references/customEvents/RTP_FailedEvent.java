package me.SuperRonanCraft.BetterRTP.references.customEvents;

import me.SuperRonanCraft.BetterRTP.player.rtp.RTPPlayer;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import org.bukkit.entity.Player;

public class RTP_FailedEvent extends RTPEvent {
   Player p;
   RTPWorld world;
   int attempts;

   public RTP_FailedEvent(RTPPlayer rtpPlayer) {
      this.p = rtpPlayer.getPlayer();
      this.world = rtpPlayer.getWorldPlayer();
      this.attempts = rtpPlayer.getAttempts();
   }

   public Player getP() {
      return this.p;
   }

   public RTPWorld getWorld() {
      return this.world;
   }

   public int getAttempts() {
      return this.attempts;
   }
}
