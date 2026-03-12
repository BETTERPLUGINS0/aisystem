package me.SuperRonanCraft.BetterRTP.references.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class RTP_TeleportPreEvent extends RTPEvent implements Cancellable {
   Player p;
   boolean cancelled;

   public RTP_TeleportPreEvent(Player p) {
      this.p = p;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean b) {
      this.cancelled = b;
   }

   public Player getP() {
      return this.p;
   }
}
