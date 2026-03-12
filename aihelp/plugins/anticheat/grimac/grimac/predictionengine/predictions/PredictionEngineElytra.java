package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PredictionEngineElytra extends PredictionEngine {
   public static Vector3dm getElytraMovement(GrimPlayer player, Vector3dm vector, Vector3dm lookVector) {
      float pitchRadians = GrimMath.radians(player.pitch);
      double horizontalSqrt = Math.sqrt(lookVector.getX() * lookVector.getX() + lookVector.getZ() * lookVector.getZ());
      double horizontalLength = vector.clone().setY(0).length();
      double length = lookVector.length();
      double vertCosRotation = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_18_2) ? Math.cos((double)pitchRadians) : (double)player.trigHandler.cos(pitchRadians);
      vertCosRotation = (double)((float)(vertCosRotation * vertCosRotation * Math.min(1.0D, length / 0.4D)));
      double recalculatedGravity = player.compensatedEntities.self.getAttributeValue(Attributes.GRAVITY);
      if (player.clientVelocity.getY() <= 0.0D && player.compensatedEntities.getSlowFallingAmplifier().isPresent()) {
         recalculatedGravity = player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5) ? 0.01D : Math.min(recalculatedGravity, 0.01D);
      }

      vector.add(0.0D, recalculatedGravity * (-1.0D + vertCosRotation * 0.75D), 0.0D);
      double d5;
      if (vector.getY() < 0.0D && horizontalSqrt > 0.0D) {
         d5 = vector.getY() * -0.1D * vertCosRotation;
         vector.add(lookVector.getX() * d5 / horizontalSqrt, d5, lookVector.getZ() * d5 / horizontalSqrt);
      }

      if (pitchRadians < 0.0F && horizontalSqrt > 0.0D) {
         d5 = horizontalLength * (double)(-player.trigHandler.sin(pitchRadians)) * 0.04D;
         vector.add(-lookVector.getX() * d5 / horizontalSqrt, d5 * 3.2D, -lookVector.getZ() * d5 / horizontalSqrt);
      }

      if (horizontalSqrt > 0.0D) {
         vector.add((lookVector.getX() / horizontalSqrt * horizontalLength - vector.getX()) * 0.1D, 0.0D, (lookVector.getZ() / horizontalSqrt * horizontalLength - vector.getZ()) * 0.1D);
      }

      return vector;
   }

   public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
      List<VectorData> results = new ArrayList();
      int shitmath = 0;

      while(shitmath <= 1) {
         Vector3dm currentLook = ReachUtils.getLook(player, player.yaw, player.pitch);

         for(int applyStuckSpeed = 1; applyStuckSpeed >= 0 && (applyStuckSpeed != 0 || !player.isForceStuckSpeed()); --applyStuckSpeed) {
            Iterator var8 = possibleVectors.iterator();

            while(var8.hasNext()) {
               VectorData data = (VectorData)var8.next();
               Vector3dm elytraResult = getElytraMovement(player, data.vector.clone(), currentLook);
               if (applyStuckSpeed != 0) {
                  elytraResult.multiply(player.stuckSpeedMultiplier);
               }

               elytraResult.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D);
               VectorData modified = data.returnNewModified(elytraResult, VectorData.VectorType.InputResult);
               modified.input = new Vector3dm(0, 0, 0);
               results.add(modified);
            }
         }

         ++shitmath;
         player.trigHandler.toggleShitMath();
      }

      return results;
   }

   public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
      (new PredictionEngineNormal()).addJumpsToPossibilities(player, existingVelocities);
   }
}
