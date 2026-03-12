package ac.grim.grimac.predictionengine.predictions.rideable;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.Generated;

public class PredictionEngineNautilusWater extends PredictionEngine {
   private final Vector3dm movementVector;
   private final double multiplier;

   public void endOfTick(GrimPlayer player, double delta) {
      super.endOfTick(player, delta);
      Iterator var4 = player.getPossibleVelocitiesMinusKnockback().iterator();

      while(var4.hasNext()) {
         VectorData vector = (VectorData)var4.next();
         vector.vector.setX(vector.vector.getX() * this.multiplier);
         vector.vector.setY(vector.vector.getY() * this.multiplier);
         vector.vector.setZ(vector.vector.getZ() * this.multiplier);
      }

   }

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
   public PredictionEngineNautilusWater(Vector3dm movementVector, double multiplier) {
      this.movementVector = movementVector;
      this.multiplier = multiplier;
   }
}
