package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "NoSlow",
   description = "Was not slowed while using an item",
   setback = 5.0D
)
public class NoSlow extends Check implements PostPredictionCheck {
   public boolean didSlotChangeLastTick = false;
   public boolean flaggedLastTick = false;
   private double offsetToFlag;
   private double bestOffset = 1.0D;

   public NoSlow(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (predictionComplete.isChecked()) {
         if (this.player.packetStateData.isSlowedByUsingItem()) {
            if (this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) && this.didSlotChangeLastTick) {
               this.didSlotChangeLastTick = false;
               this.flaggedLastTick = false;
            }

            if (this.bestOffset > this.offsetToFlag) {
               if (this.flaggedLastTick) {
                  this.flagAndAlertWithSetback();
               }

               this.flaggedLastTick = true;
            } else {
               this.reward();
               this.flaggedLastTick = false;
            }
         }

         this.bestOffset = 1.0D;
      }
   }

   public void handlePredictionAnalysis(double offset) {
      this.bestOffset = Math.min(this.bestOffset, offset);
   }

   public void onReload(ConfigManager config) {
      this.offsetToFlag = config.getDoubleElse(this.getConfigName() + ".threshold", 0.001D);
   }
}
