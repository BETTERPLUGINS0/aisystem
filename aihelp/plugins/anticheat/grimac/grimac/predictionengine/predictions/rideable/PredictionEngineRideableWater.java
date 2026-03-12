package ac.grim.grimac.predictionengine.predictions.rideable;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWater;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.List;
import java.util.Set;
import lombok.Generated;

public class PredictionEngineRideableWater extends PredictionEngineWater {
   protected final Vector3dm movementVector;

   public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
      PredictionEngineRideableUtils.handleJumps(player, existingVelocities);
   }

   public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
      return PredictionEngineRideableUtils.applyInputsToVelocityPossibilities(this, this.movementVector, player, possibleVectors, speed);
   }

   public Vector3dm getMovementResultFromInput(GrimPlayer player, Vector3dm inputVector, float flyingSpeed, float yRot) {
      float yRotRadians = GrimMath.radians(yRot);
      float sin = player.trigHandler.sin(yRotRadians);
      float cos = player.trigHandler.cos(yRotRadians);
      double xResult = inputVector.getX() * (double)cos - inputVector.getZ() * (double)sin;
      double zResult = inputVector.getZ() * (double)cos + inputVector.getX() * (double)sin;
      return new Vector3dm(xResult * (double)flyingSpeed, inputVector.getY() * (double)flyingSpeed, zResult * (double)flyingSpeed);
   }

   @Generated
   public PredictionEngineRideableWater(Vector3dm movementVector) {
      this.movementVector = movementVector;
   }
}
