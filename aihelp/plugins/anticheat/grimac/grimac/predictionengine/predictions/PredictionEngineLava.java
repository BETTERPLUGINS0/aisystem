package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PredictionEngineLava extends PredictionEngine {
   public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
      Iterator var3 = (new HashSet(existingVelocities)).iterator();

      while(var3.hasNext()) {
         VectorData vector = (VectorData)var3.next();
         if (player.couldSkipTick && vector.isZeroPointZeroThree()) {
            double extraVelFromVertTickSkipUpwards = GrimMath.clamp(player.actualMovement.getY(), vector.vector.clone().getY(), vector.vector.clone().getY() + 0.05000000074505806D);
            existingVelocities.add(new VectorData(vector.vector.clone().setY(extraVelFromVertTickSkipUpwards), vector, VectorData.VectorType.Jump));
         } else {
            existingVelocities.add(new VectorData(vector.vector.clone().add(0.0D, 0.03999999910593033D, 0.0D), vector, VectorData.VectorType.Jump));
         }

         if (player.slightlyTouchingLava && player.lastOnGround && !player.onGround) {
            Vector3dm withJump = vector.vector.clone();
            super.doJump(player, withJump);
            existingVelocities.add(new VectorData(withJump, vector, VectorData.VectorType.Jump));
         }
      }

   }
}
