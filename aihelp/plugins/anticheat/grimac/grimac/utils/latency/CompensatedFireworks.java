package ac.grim.grimac.utils.latency;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.HashSet;
import java.util.Set;

public class CompensatedFireworks extends Check implements PostPredictionCheck {
   private final Set<Integer> activeFireworks = new HashSet();
   private final Set<Integer> fireworksToRemoveNextTick = new HashSet();

   public CompensatedFireworks(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      this.activeFireworks.removeAll(this.fireworksToRemoveNextTick);
      this.fireworksToRemoveNextTick.clear();
   }

   public boolean hasFirework(int entityId) {
      return this.activeFireworks.contains(entityId);
   }

   public void addNewFirework(int entityID) {
      this.activeFireworks.add(entityID);
   }

   public void removeFirework(int entityID) {
      if (this.activeFireworks.contains(entityID)) {
         this.fireworksToRemoveNextTick.add(entityID);
      }

   }

   public int getMaxFireworksAppliedPossible() {
      return this.activeFireworks.size();
   }
}
