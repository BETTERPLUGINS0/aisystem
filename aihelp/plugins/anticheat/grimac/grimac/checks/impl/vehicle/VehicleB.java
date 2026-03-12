package ac.grim.grimac.checks.impl.vehicle;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(
   name = "VehicleB",
   description = "Claimed to be in a vehicle while not in a vehicle"
)
public class VehicleB extends Check implements PacketCheck {
   public VehicleB(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE && !this.player.inVehicle() && this.flagAndAlert() && this.shouldModifyPackets()) {
         event.setCancelled(true);
         this.player.onPacketCancel();
      }

   }
}
