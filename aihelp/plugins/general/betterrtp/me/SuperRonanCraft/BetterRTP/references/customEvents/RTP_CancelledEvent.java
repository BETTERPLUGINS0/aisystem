package me.SuperRonanCraft.BetterRTP.references.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class RTP_CancelledEvent extends RTPEvent {
   Player p;
   private static final HandlerList handler = new HandlerList();

   public RTP_CancelledEvent(Player p) {
      this.p = p;
   }

   public Player getPlayer() {
      return this.p;
   }
}
