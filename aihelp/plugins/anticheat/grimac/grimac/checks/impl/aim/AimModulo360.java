package ac.grim.grimac.checks.impl.aim;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.RotationCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;

@CheckData(
   name = "AimModulo360",
   decay = 0.005D
)
public class AimModulo360 extends Check implements RotationCheck {
   private float lastDeltaYaw;

   public AimModulo360(GrimPlayer playerData) {
      super(playerData);
   }

   public void process(RotationUpdate rotationUpdate) {
      if (!this.player.packetStateData.lastPacketWasTeleport && !this.player.vehicleData.wasVehicleSwitch && !this.player.packetStateData.horseInteractCausedForcedRotation) {
         if (this.player.yaw < 360.0F && this.player.yaw > -360.0F && Math.abs(rotationUpdate.getDeltaXRot()) > 320.0F && Math.abs(this.lastDeltaYaw) < 30.0F) {
            this.flagAndAlert();
         } else {
            this.reward();
         }

         this.lastDeltaYaw = rotationUpdate.getDeltaXRot();
      } else {
         this.lastDeltaYaw = rotationUpdate.getDeltaXRot();
      }
   }
}
