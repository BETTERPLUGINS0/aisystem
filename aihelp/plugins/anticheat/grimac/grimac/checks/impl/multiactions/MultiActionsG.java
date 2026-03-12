package ac.grim.grimac.checks.impl.multiactions;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;

@CheckData(
   name = "MultiActionsG",
   description = "Attacking or using items while rowing a boat",
   experimental = true
)
public class MultiActionsG extends BlockPlaceCheck {
   public MultiActionsG(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY && this.isCheckActive() && this.flagAndAlert("interact") && this.shouldModifyPackets()) {
         event.setCancelled(true);
         this.player.onPacketCancel();
      }

      if (event.getPacketType() == PacketType.Play.Client.USE_ITEM && this.isCheckActive() && this.flagAndAlert("use") && this.shouldModifyPackets()) {
         event.setCancelled(true);
         this.player.onPacketCancel();
      }

   }

   public void onBlockPlace(BlockPlace place) {
      if (this.isCheckActive() && this.flagAndAlert(place.getFace() == BlockFace.OTHER ? "use" : "place") && this.shouldModifyPackets() && this.shouldCancel()) {
         place.resync();
      }

   }

   public boolean isCheckActive() {
      return this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && !this.player.vehicleData.wasVehicleSwitch && this.player.inVehicle() && this.player.compensatedEntities.self.getRiding().type.isInstanceOf(EntityTypes.BOAT) && (this.player.vehicleData.nextVehicleForward != 0.0F || this.player.vehicleData.nextVehicleHorizontal != 0.0F);
   }
}
