package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(
   name = "BadPacketsZ",
   experimental = true
)
public class BadPacketsZ extends Check implements PacketCheck {
   private boolean sent;

   public BadPacketsZ(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLIENT_TICK_END) {
         this.sent = false;
      }

      if (event.getPacketType() == PacketType.Play.Client.PLAYER_INPUT) {
         if (this.sent) {
            this.flagAndAlert();
         }

         this.sent = true;
      }

   }
}
