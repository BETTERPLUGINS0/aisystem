package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "SprintG",
   description = "Sprinting while in water",
   experimental = true
)
public class SprintG extends Check implements PostPredictionCheck {
   public SprintG(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.wasTouchingWater && (this.player.wasWasTouchingWater || this.player.getClientVersion() == ClientVersion.V_1_21_4) && !this.player.wasEyeInWater && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && this.player.wasLastPredictionCompleteChecked && predictionComplete.isChecked()) {
         if (this.player.isSprinting && !this.player.isSwimming) {
            this.flagAndAlertWithSetback();
         } else {
            this.reward();
         }
      }

   }
}
