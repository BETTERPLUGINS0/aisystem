package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "SprintF",
   description = "Sprinting while gliding",
   experimental = true
)
public class SprintF extends Check implements PostPredictionCheck {
   public SprintF(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.wasGliding && this.player.isGliding && this.player.getClientVersion() == ClientVersion.V_1_21_4) {
         if (this.player.isSprinting) {
            this.flagAndAlertWithSetback();
         } else {
            this.reward();
         }
      }

   }
}
