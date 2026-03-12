package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "SprintC",
   description = "Sprinting while using an item",
   setback = 5.0D,
   experimental = true
)
public class SprintC extends Check implements PostPredictionCheck {
   private boolean flaggedLastTick = false;

   public SprintC(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.packetStateData.isSlowedByUsingItem()) {
         ClientVersion version = this.player.getClientVersion();
         if (version.isNewerThanOrEquals(ClientVersion.V_1_14_2) && version != ClientVersion.V_1_21_4) {
            return;
         }

         if (!this.player.isSprinting || this.player.wasTouchingWater && !version.isOlderThan(ClientVersion.V_1_13)) {
            this.reward();
            this.flaggedLastTick = false;
         } else {
            if (this.flaggedLastTick) {
               this.flagAndAlertWithSetback();
            }

            this.flaggedLastTick = true;
         }
      }

   }
}
