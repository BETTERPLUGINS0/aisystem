package ac.grim.grimac.checks.impl.prediction;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "GroundSpoof",
   setback = 10.0D,
   decay = 0.01D
)
public class GroundSpoof extends Check implements PostPredictionCheck {
   public GroundSpoof(GrimPlayer player) {
      super(player);
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (!PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_8) || this.player.gamemode != GameMode.SPECTATOR) {
         if (!this.player.exemptOnGround() && predictionComplete.isChecked()) {
            if (!this.player.getSetbackTeleportUtil().blockOffsets) {
               if (!this.player.packetStateData.lastPacketWasTeleport) {
                  if (this.player.clientClaimsLastOnGround != this.player.onGround) {
                     this.flagAndAlertWithSetback("claimed " + this.player.clientClaimsLastOnGround);
                     this.player.checkManager.getNoFall().flipPlayerGroundStatus = true;
                  }

               }
            }
         }
      }
   }
}
