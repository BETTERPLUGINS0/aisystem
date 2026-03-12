package ac.grim.grimac.checks.impl.vehicle;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(
   name = "VehicleE",
   experimental = true,
   description = "Sent boat paddle states while not in a boat"
)
public class VehicleE extends Check implements PacketCheck {
   public VehicleE(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.STEER_BOAT) {
         EntityType vehicle = this.player.getVehicleType();
         if (!EntityTypes.isTypeInstanceOf(vehicle, EntityTypes.BOAT)) {
            String var10001 = vehicle == null ? "null" : vehicle.getName().getKey().toLowerCase();
            if (this.flagAndAlert("vehicle=" + var10001) && this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         }
      }

   }
}
