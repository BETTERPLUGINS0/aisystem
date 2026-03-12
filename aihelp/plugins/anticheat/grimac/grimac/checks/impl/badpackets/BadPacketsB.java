package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;

@CheckData(
   name = "BadPacketsB",
   description = "Ignored set rotation packet"
)
public class BadPacketsB extends Check implements PacketCheck {
   public BadPacketsB(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (isTransaction(event.getPacketType())) {
         this.player.pendingRotations.removeIf((data) -> {
            if (this.player.getLastTransactionReceived() > data.getTransaction()) {
               if (!data.isAccepted()) {
                  this.flagAndAlert();
               }

               return true;
            } else {
               return false;
            }
         });
      }

   }
}
