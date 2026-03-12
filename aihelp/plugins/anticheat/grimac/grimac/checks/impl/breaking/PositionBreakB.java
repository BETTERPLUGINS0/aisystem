package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;

@CheckData(
   name = "PositionBreakB"
)
public class PositionBreakB extends Check implements BlockBreakCheck {
   private final int releaseFace;
   private BlockFace lastFace;

   public PositionBreakB(GrimPlayer player) {
      super(player);
      this.releaseFace = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) ? 0 : 255;
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (blockBreak.action == DiggingAction.START_DIGGING && blockBreak.face == this.lastFace) {
         this.lastFace = null;
      }

      if (this.lastFace != null) {
         String var10001 = String.valueOf(this.lastFace);
         this.flagAndAlert("lastFace=" + var10001 + ", action=" + String.valueOf(blockBreak.action));
      }

      if (blockBreak.action == DiggingAction.CANCELLED_DIGGING) {
         this.lastFace = blockBreak.faceId == this.releaseFace ? null : blockBreak.face;
      }

   }
}
