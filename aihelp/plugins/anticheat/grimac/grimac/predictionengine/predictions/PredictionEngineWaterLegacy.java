package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PredictionEngineWaterLegacy extends PredictionEngine {
   private float swimmingSpeed;
   private float swimmingFriction;

   public void guessBestMovement(float swimmingSpeed, GrimPlayer player, float swimmingFriction) {
      this.swimmingSpeed = swimmingSpeed;
      this.swimmingFriction = swimmingFriction;
      super.guessBestMovement(swimmingSpeed, player);
   }

   public Vector3dm getMovementResultFromInput(GrimPlayer player, Vector3dm inputVector, float f, float f2) {
      float lengthSquared = (float)inputVector.lengthSquared();
      if (lengthSquared >= 1.0E-4F) {
         lengthSquared = (float)Math.sqrt((double)lengthSquared);
         if (lengthSquared < 1.0F) {
            lengthSquared = 1.0F;
         }

         lengthSquared = this.swimmingSpeed / lengthSquared;
         inputVector.multiply(lengthSquared);
         float yawRadians = GrimMath.radians(player.yaw);
         float sinResult = player.trigHandler.sin(yawRadians);
         float cosResult = player.trigHandler.cos(yawRadians);
         return new Vector3dm(inputVector.getX() * (double)cosResult - inputVector.getZ() * (double)sinResult, inputVector.getY(), inputVector.getZ() * (double)cosResult + inputVector.getX() * (double)sinResult);
      } else {
         return new Vector3dm();
      }
   }

   public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
      Iterator var3 = (new HashSet(existingVelocities)).iterator();

      while(var3.hasNext()) {
         VectorData vector = (VectorData)var3.next();
         existingVelocities.add(new VectorData(vector.vector.clone().add(0.0D, 0.03999999910593033D, 0.0D), vector, VectorData.VectorType.Jump));
         if (player.skippedTickInActualMovement) {
            existingVelocities.add(new VectorData(vector.vector.clone().add(0.0D, 0.019999999552965164D, 0.0D), vector, VectorData.VectorType.Jump));
         }
      }

   }

   public void endOfTick(GrimPlayer player, double playerGravity) {
      super.endOfTick(player, playerGravity);
      Iterator var4 = player.getPossibleVelocitiesMinusKnockback().iterator();

      while(var4.hasNext()) {
         VectorData vector = (VectorData)var4.next();
         vector.vector.multiply((double)this.swimmingFriction, 0.800000011920929D, (double)this.swimmingFriction);
         vector.vector.setY(vector.vector.getY() - 0.02D);
      }

   }
}
