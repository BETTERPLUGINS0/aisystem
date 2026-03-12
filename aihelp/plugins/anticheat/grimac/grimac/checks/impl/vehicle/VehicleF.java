package ac.grim.grimac.checks.impl.vehicle;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerBoat;
import ac.grim.grimac.utils.data.KnownInput;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;

@CheckData(
   name = "VehicleF",
   experimental = true,
   description = "Sent incorrect boat paddle states"
)
public class VehicleF extends Check implements PacketCheck {
   private PacketEntity lastTickVehicle;

   public VehicleF(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.STEER_BOAT) {
         if (this.lastTickVehicle != this.player.getVehicle()) {
            return;
         }

         WrapperPlayClientSteerBoat packet = new WrapperPlayClientSteerBoat(event);
         boolean expectedLeft;
         boolean expectedRight;
         if (this.player.supportsEndTick()) {
            KnownInput input = this.player.packetStateData.knownInput;
            expectedLeft = input.forward() || !input.left() && input.right();
            expectedRight = input.forward() || input.left() && !input.right();
         } else {
            expectedLeft = this.player.vehicleData.nextVehicleForward > 0.0F || this.player.vehicleData.nextVehicleHorizontal < 0.0F;
            expectedRight = this.player.vehicleData.nextVehicleForward > 0.0F || this.player.vehicleData.nextVehicleHorizontal > 0.0F;
            if (this.player.vehicleData.nextVehicleForward == 0.0F && packet.isLeftPaddleTurning() && packet.isRightPaddleTurning()) {
               return;
            }
         }

         if (packet.isLeftPaddleTurning() != expectedLeft || packet.isRightPaddleTurning() != expectedRight) {
            boolean var10001 = packet.isLeftPaddleTurning();
            if (this.flagAndAlert("sent=(" + var10001 + ", " + packet.isRightPaddleTurning() + "), expected=(" + expectedLeft + ", " + expectedRight + ")") && this.shouldModifyPackets()) {
               packet.setLeftPaddleTurning(expectedLeft);
               packet.setRightPaddleTurning(expectedRight);
               event.markForReEncode(true);
            }
         }
      }

      if (this.isTickPacket(event.getPacketType())) {
         this.lastTickVehicle = this.player.getVehicle();
      }

   }
}
