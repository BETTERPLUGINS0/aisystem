package ac.grim.grimac.checks.impl.timer;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;

@CheckData(
   name = "VehicleTimer",
   setback = 10.0D
)
public class VehicleTimer extends Timer {
   private boolean isDummy = false;

   public VehicleTimer(GrimPlayer player) {
      super(player);
   }

   public boolean shouldCountPacketForTimer(PacketTypeCommon packetType) {
      if (this.player.packetStateData.lastPacketWasTeleport) {
         return false;
      } else if (packetType == PacketType.Play.Client.VEHICLE_MOVE) {
         this.isDummy = false;
         return true;
      } else {
         if (packetType == PacketType.Play.Client.STEER_VEHICLE) {
            if (this.isDummy) {
               return true;
            }

            this.isDummy = true;
         }

         return false;
      }
   }
}
