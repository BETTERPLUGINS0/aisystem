package ac.grim.grimac.checks.impl.movement;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.VehicleCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;
import ac.grim.grimac.utils.anticheat.update.VehiclePositionUpdate;
import ac.grim.grimac.utils.data.SetBackData;
import ac.grim.grimac.utils.data.TeleportData;

public class VehiclePredictionRunner extends Check implements VehicleCheck {
   public VehiclePredictionRunner(GrimPlayer playerData) {
      super(playerData);
   }

   public void process(VehiclePositionUpdate vehicleUpdate) {
      this.player.movementCheckRunner.processAndCheckMovementPacket(new PositionUpdate(vehicleUpdate.from(), vehicleUpdate.to(), false, (SetBackData)null, (TeleportData)null, vehicleUpdate.isTeleport()));
   }
}
