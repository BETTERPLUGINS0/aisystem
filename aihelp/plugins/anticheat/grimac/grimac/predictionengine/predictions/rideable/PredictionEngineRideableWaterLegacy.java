package ac.grim.grimac.predictionengine.predictions.rideable;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWaterLegacy;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.List;
import java.util.Set;
import lombok.Generated;

public class PredictionEngineRideableWaterLegacy extends PredictionEngineWaterLegacy {
   private final Vector3dm movementVector;

   public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
      PredictionEngineRideableUtils.handleJumps(player, existingVelocities);
   }

   public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
      return PredictionEngineRideableUtils.applyInputsToVelocityPossibilities(this.movementVector, player, possibleVectors, speed);
   }

   @Generated
   public PredictionEngineRideableWaterLegacy(Vector3dm movementVector) {
      this.movementVector = movementVector;
   }
}
