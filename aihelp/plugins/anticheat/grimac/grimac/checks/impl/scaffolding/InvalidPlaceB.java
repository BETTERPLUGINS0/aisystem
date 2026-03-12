package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;

@CheckData(
   name = "InvalidPlaceB",
   description = "Sent impossible block face id"
)
public class InvalidPlaceB extends BlockPlaceCheck {
   public InvalidPlaceB(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      if (place.getFaceId() != 255 || !PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8)) {
         if ((place.getFaceId() < 0 || place.getFaceId() > 5) && this.flagAndAlert("direction=" + place.getFaceId()) && this.shouldModifyPackets() && this.shouldCancel()) {
            place.resync();
         }

      }
   }
}
