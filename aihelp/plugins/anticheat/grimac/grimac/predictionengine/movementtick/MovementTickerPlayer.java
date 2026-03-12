package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineLava;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWater;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWaterLegacy;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.nmsutil.BlockProperties;

public class MovementTickerPlayer extends MovementTicker {
   public MovementTickerPlayer(GrimPlayer player) {
      super(player);
   }

   public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
      if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
         (new PredictionEngineWater()).guessBestMovement(swimSpeed, this.player, isFalling, this.player.gravity, swimFriction);
      } else {
         (new PredictionEngineWaterLegacy()).guessBestMovement(swimSpeed, this.player, swimFriction);
      }

   }

   public void doLavaMove() {
      (new PredictionEngineLava()).guessBestMovement(0.02F, this.player);
   }

   public void doNormalMove(float blockFriction) {
      (new PredictionEngineNormal()).guessBestMovement(BlockProperties.getFrictionInfluencedSpeed(blockFriction, this.player), this.player);
   }
}
