package me.SuperRonanCraft.BetterRTP.references.customEvents;

import me.SuperRonanCraft.BetterRTP.player.rtp.RTPPlayer;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.Nullable;

public class RTP_FindLocationEvent extends RTPEvent implements Cancellable {
   Player p;
   RTPWorld world;
   Location loc;
   int attempts;
   boolean cancelled;

   public RTP_FindLocationEvent(RTPPlayer rtpPlayer) {
      this.p = rtpPlayer.getPlayer();
      this.world = rtpPlayer.getWorldPlayer();
      this.attempts = rtpPlayer.getAttempts();
   }

   public void setLocation(Location loc) {
      this.loc = loc;
   }

   @Nullable
   public Location getLocation() {
      return this.loc;
   }

   public RTPWorld getWorld() {
      return this.world;
   }

   public Player getPlayer() {
      return this.p;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean b) {
      this.cancelled = b;
   }
}
