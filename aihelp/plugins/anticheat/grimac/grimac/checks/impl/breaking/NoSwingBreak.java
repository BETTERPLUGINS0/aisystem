package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;

@CheckData(
   name = "NoSwingBreak",
   description = "Did not swing while breaking block",
   experimental = true
)
public class NoSwingBreak extends Check implements BlockBreakCheck {
   private boolean sentAnimation;
   private boolean sentBreak;

   public NoSwingBreak(GrimPlayer playerData) {
      super(playerData);
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (blockBreak.action != DiggingAction.CANCELLED_DIGGING) {
         this.sentBreak = true;
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ANIMATION) {
         this.sentAnimation = true;
      }

      if (this.isTickPacket(event.getPacketType())) {
         if (this.sentBreak && !this.sentAnimation) {
            this.flagAndAlert();
         }

         this.sentAnimation = this.sentBreak = false;
      }

   }
}
