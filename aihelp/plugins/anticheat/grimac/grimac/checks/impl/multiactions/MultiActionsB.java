package ac.grim.grimac.checks.impl.multiactions;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;

@CheckData(
   name = "MultiActionsB",
   description = "Breaking blocks while using an item",
   experimental = true
)
public class MultiActionsB extends Check implements BlockBreakCheck {
   public MultiActionsB(GrimPlayer player) {
      super(player);
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (this.player.packetStateData.isSlowedByUsingItem() && (this.player.packetStateData.lastSlotSelected == this.player.packetStateData.getSlowedByUsingItemSlot() || this.player.packetStateData.itemInUseHand == InteractionHand.OFF_HAND)) {
         if (this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10)) {
            return;
         }

         if (this.flagAndAlert() && this.shouldModifyPackets()) {
            blockBreak.cancel();
         }
      }

   }
}
