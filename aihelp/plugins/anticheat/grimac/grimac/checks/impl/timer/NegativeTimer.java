package ac.grim.grimac.checks.impl.timer;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "NegativeTimer",
   setback = -1.0D,
   experimental = true
)
public class NegativeTimer extends Timer implements PostPredictionCheck {
   public NegativeTimer(GrimPlayer player) {
      super(player);
      this.timerBalanceRealTime = System.nanoTime() + this.clockDrift;
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.uncertaintyHandler.lastPointThree.hasOccurredSince(2) || !predictionComplete.isChecked()) {
         this.timerBalanceRealTime = System.nanoTime() + this.clockDrift;
      }

      if (this.timerBalanceRealTime < this.lastMovementPlayerClock - this.clockDrift) {
         int lostMS = (int)((double)(System.nanoTime() - this.timerBalanceRealTime) / 1000000.0D);
         this.flagAndAlertWithSetback("-" + lostMS);
         this.timerBalanceRealTime = (long)((double)this.timerBalanceRealTime + 5.0E7D);
      }

   }

   public void doCheck(PacketReceiveEvent event) {
   }

   public void onReload(ConfigManager config) {
      this.clockDrift = (long)(config.getDoubleElse(this.getConfigName() + ".drift", 1200.0D) * 1000000.0D);
   }
}
