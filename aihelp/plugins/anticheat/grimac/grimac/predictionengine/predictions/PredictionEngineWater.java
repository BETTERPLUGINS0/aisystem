package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.enums.FluidTag;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.FluidFallingAdjustedMovement;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PredictionEngineWater extends PredictionEngine {
   private boolean isFalling;
   private double playerGravity;
   private float swimmingFriction;

   public static void staticVectorEndOfTick(GrimPlayer player, Vector3dm vector, float swimmingFriction, double playerGravity, boolean isFalling) {
      vector.multiply((double)swimmingFriction, 0.800000011920929D, (double)swimmingFriction);
      Vector3dm fluidVector = FluidFallingAdjustedMovement.getFluidFallingAdjustedMovement(player, playerGravity, isFalling, vector);
      vector.setX(fluidVector.getX());
      vector.setY(fluidVector.getY());
      vector.setZ(fluidVector.getZ());
   }

   public static Set<VectorData> transformSwimmingVectors(GrimPlayer player, Set<VectorData> base) {
      Set<VectorData> swimmingVelocities = new HashSet();
      if ((player.wasEyeInWater || player.fluidOnEyes == FluidTag.WATER || player.isSwimming || player.wasSwimming) && !player.inVehicle()) {
         Iterator var3 = base.iterator();

         while(var3.hasNext()) {
            VectorData vector = (VectorData)var3.next();
            double lookYAmount = ReachUtils.getLook(player, player.yaw, player.pitch).getY();
            double scalar = lookYAmount < -0.2D ? 0.085D : 0.06D;
            swimmingVelocities.add(vector.returnNewModified(new Vector3dm(vector.vector.getX(), vector.vector.getY() + (lookYAmount - vector.vector.getY()) * scalar, vector.vector.getZ()), VectorData.VectorType.SwimmingSpace));
            swimmingVelocities.add(vector.returnNewModified(vector.vector, VectorData.VectorType.SurfaceSwimming));
         }

         return swimmingVelocities;
      } else {
         return base;
      }
   }

   public void guessBestMovement(float swimmingSpeed, GrimPlayer player, boolean isFalling, double playerGravity, float swimmingFriction) {
      this.isFalling = isFalling;
      this.playerGravity = playerGravity;
      this.swimmingFriction = swimmingFriction;
      super.guessBestMovement(swimmingSpeed, player);
   }

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

         if (player.slightlyTouchingWater && player.lastOnGround && !player.onGround) {
            Vector3dm withJump = vector.vector.clone();
            super.doJump(player, withJump);
            existingVelocities.add(new VectorData(withJump, vector, VectorData.VectorType.Jump));
         }
      }

   }

   public void endOfTick(GrimPlayer player, double playerGravity) {
      super.endOfTick(player, playerGravity);
      Iterator var4 = player.getPossibleVelocitiesMinusKnockback().iterator();

      while(var4.hasNext()) {
         VectorData vector = (VectorData)var4.next();
         staticVectorEndOfTick(player, vector.vector, this.swimmingFriction, playerGravity, this.isFalling);
      }

   }

   public Set<VectorData> fetchPossibleStartTickVectors(GrimPlayer player) {
      if (player.lastWasClimbing == 0.0D && player.pointThreeEstimator.isNearClimbable() && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) || !Collisions.isEmpty(player, player.boundingBox.copy().expand(player.clientVelocity.getX(), 0.0D, player.clientVelocity.getZ()).expand(0.5D, -1.0E-7D, 0.5D)))) {
         player.lastWasClimbing = FluidFallingAdjustedMovement.getFluidFallingAdjustedMovement(player, this.playerGravity, this.isFalling, player.clientVelocity.clone().setY(0.1600000023841858D)).getY();
      }

      Set<VectorData> baseVelocities = super.fetchPossibleStartTickVectors(player);
      return transformSwimmingVectors(player, baseVelocities);
   }
}
