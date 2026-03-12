package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.enums.Pose;
import java.util.Collections;

@CheckData(
   name = "SprintB",
   description = "Sprinting while sneaking or crawling",
   setback = 5.0D,
   experimental = true
)
public class SprintB extends Check implements PostPredictionCheck {
   public SprintB(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.isSlowMovement && this.player.sneakingSpeedMultiplier < 0.8F && predictionComplete.isChecked()) {
         ClientVersion version = this.player.getClientVersion();
         if (version.isNewerThanOrEquals(ClientVersion.V_1_14_2) && version != ClientVersion.V_1_21_4) {
            return;
         }

         if (version.isNewerThanOrEquals(ClientVersion.V_1_14) && this.player.wasFlying && this.player.lastPose == Pose.FALL_FLYING && !this.player.isGliding) {
            return;
         }

         if (version == ClientVersion.V_1_21_4 && ((Double)Collections.max(this.player.uncertaintyHandler.pistonX) != 0.0D || (Double)Collections.max(this.player.uncertaintyHandler.pistonY) != 0.0D || (Double)Collections.max(this.player.uncertaintyHandler.pistonZ) != 0.0D)) {
            return;
         }

         if (!this.player.isSprinting || this.player.wasTouchingWater && !version.isOlderThan(ClientVersion.V_1_13)) {
            this.reward();
         } else {
            this.flagAndAlertWithSetback();
         }
      }

   }
}
