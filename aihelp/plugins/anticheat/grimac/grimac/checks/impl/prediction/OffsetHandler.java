package ac.grim.grimac.checks.impl.prediction;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.event.events.CompletePredictionEvent;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.concurrent.atomic.AtomicInteger;

@CheckData(
   name = "Simulation",
   decay = 0.02D
)
public class OffsetHandler extends Check implements PostPredictionCheck {
   private static final AtomicInteger flags = new AtomicInteger(0);
   private double setbackDecayMultiplier;
   private double threshold;
   private double immediateSetbackThreshold;
   private double maxAdvantage;
   private double maxCeiling;
   private double setbackViolationThreshold;
   private double advantageGained = 0.0D;

   public OffsetHandler(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (predictionComplete.isChecked()) {
         double offset = predictionComplete.getOffset();
         CompletePredictionEvent completePredictionEvent = new CompletePredictionEvent(this.player, this, offset);
         GrimAPI.INSTANCE.getEventBus().post(completePredictionEvent);
         if (!completePredictionEvent.isCancelled()) {
            if (!(offset >= this.threshold) && !(offset >= this.immediateSetbackThreshold)) {
               this.advantageGained *= this.setbackDecayMultiplier;
            } else {
               this.advantageGained += offset;
               this.giveOffsetLenienceNextTick(offset);
               synchronized(flags) {
                  int flagId = (flags.get() & 255) + 1;
                  String humanFormattedOffset;
                  if (offset < 0.001D) {
                     humanFormattedOffset = String.format("%.4E", offset);
                     humanFormattedOffset = humanFormattedOffset.replace("E-0", "E-");
                  } else {
                     humanFormattedOffset = String.format("%6f", offset);
                     humanFormattedOffset = humanFormattedOffset.replace("0.", ".");
                  }

                  String verbose = humanFormattedOffset + " /gl " + flagId;
                  if (this.flag(verbose)) {
                     if (this.alert(verbose)) {
                        flags.incrementAndGet();
                        predictionComplete.setIdentifier(flagId);
                     }

                     if ((this.advantageGained >= this.maxAdvantage || offset >= this.immediateSetbackThreshold) && !this.isNoSetbackPermission() && this.violations >= this.setbackViolationThreshold) {
                        this.player.getSetbackTeleportUtil().executeViolationSetback();
                     }
                  }
               }

               this.advantageGained = Math.min(this.advantageGained, this.maxCeiling);
            }

            this.removeOffsetLenience();
         }
      }
   }

   private void giveOffsetLenienceNextTick(double offset) {
      double minimizedOffset = Math.min(offset, 1.0D);
      this.player.uncertaintyHandler.lastHorizontalOffset = minimizedOffset;
      this.player.uncertaintyHandler.lastVerticalOffset = minimizedOffset;
   }

   private void removeOffsetLenience() {
      this.player.uncertaintyHandler.lastHorizontalOffset = 0.0D;
      this.player.uncertaintyHandler.lastVerticalOffset = 0.0D;
   }

   public void onReload(ConfigManager config) {
      this.setbackDecayMultiplier = config.getDoubleElse("Simulation.setback-decay-multiplier", 0.999D);
      this.threshold = config.getDoubleElse("Simulation.threshold", 0.001D);
      this.immediateSetbackThreshold = config.getDoubleElse("Simulation.immediate-setback-threshold", 0.1D);
      this.maxAdvantage = config.getDoubleElse("Simulation.max-advantage", 1.0D);
      this.maxCeiling = config.getDoubleElse("Simulation.max-ceiling", 4.0D);
      this.setbackViolationThreshold = config.getDoubleElse("Simulation.setback-violation-threshold", 1.0D);
      if (this.maxAdvantage == -1.0D) {
         this.maxAdvantage = Double.MAX_VALUE;
      }

      if (this.immediateSetbackThreshold == -1.0D) {
         this.immediateSetbackThreshold = Double.MAX_VALUE;
      }

   }

   public boolean doesOffsetFlag(double offset) {
      return offset >= this.threshold;
   }
}
