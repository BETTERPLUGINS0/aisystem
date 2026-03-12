package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;

@CheckData(
   name = "BadPacketsK",
   description = "Sent spectate packets while not in spectator mode"
)
public class BadPacketsK extends Check implements PacketCheck {
   public BadPacketsK(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.SPECTATE && this.player.gamemode != GameMode.SPECTATOR && this.flagAndAlert() && this.shouldModifyPackets()) {
         event.setCancelled(true);
         this.player.onPacketCancel();
      }

   }
}
