package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PositionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;

public class PredictionRunner extends Check implements PositionCheck {
   public PredictionRunner(GrimPlayer playerData) {
      super(playerData);
   }

   public void onPositionUpdate(PositionUpdate positionUpdate) {
      if (!this.player.inVehicle()) {
         this.player.movementCheckRunner.processAndCheckMovementPacket(positionUpdate);
      }

   }
}
