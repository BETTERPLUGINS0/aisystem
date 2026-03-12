package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;

@CheckData(
   name = "InvalidBreak",
   description = "Sent impossible block face id"
)
public class InvalidBreak extends Check implements BlockBreakCheck {
   public InvalidBreak(GrimPlayer player) {
      super(player);
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (blockBreak.faceId != 255 || blockBreak.action != DiggingAction.CANCELLED_DIGGING || !this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10)) {
         if (blockBreak.faceId < 0 || blockBreak.faceId > 5) {
            int var10001 = blockBreak.faceId;
            if (this.flagAndAlert("face=" + var10001 + ", action=" + String.valueOf(blockBreak.action)) && this.shouldModifyPackets()) {
               blockBreak.cancel();
            }
         }

      }
   }
}
