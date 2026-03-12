package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineRideableLava;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineRideableNormal;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineRideableWater;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineRideableWaterLegacy;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;

public class MovementTickerLivingVehicle extends MovementTicker {
   protected Vector3dm movementInput = new Vector3dm();

   public MovementTickerLivingVehicle(GrimPlayer player) {
      super(player);
   }

   public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
      if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
         (new PredictionEngineRideableWater(this.movementInput)).guessBestMovement(swimSpeed, this.player, isFalling, this.player.gravity, swimFriction);
      } else {
         (new PredictionEngineRideableWaterLegacy(this.movementInput)).guessBestMovement(swimSpeed, this.player, swimFriction);
      }

   }

   public void doLavaMove() {
      (new PredictionEngineRideableLava(this.movementInput)).guessBestMovement(0.02F, this.player);
   }

   public void doNormalMove(float blockFriction) {
      (new PredictionEngineRideableNormal(this.movementInput)).guessBestMovement(BlockProperties.getFrictionInfluencedSpeed(blockFriction, this.player), this.player);
   }
}
