package ac.grim.grimac.checks.impl.vehicle;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;

@CheckData(
   name = "VehicleA",
   description = "Impossible input values"
)
public class VehicleA extends Check implements PacketCheck {
   public VehicleA(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
         WrapperPlayClientSteerVehicle packet = new WrapperPlayClientSteerVehicle(event);
         if (Math.abs(packet.getForward()) > 0.98F || Math.abs(packet.getSideways()) > 0.98F) {
            float var10001 = packet.getForward();
            if (this.flagAndAlert("forwards=" + var10001 + ", sideways=" + packet.getSideways()) && this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         }
      }

   }
}
