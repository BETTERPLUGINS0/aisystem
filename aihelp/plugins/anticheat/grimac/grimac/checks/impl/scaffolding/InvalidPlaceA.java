package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;

@CheckData(
   name = "InvalidPlaceA",
   description = "Sent invalid cursor position"
)
public class InvalidPlaceA extends BlockPlaceCheck {
   public InvalidPlaceA(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      Vector3f cursor = place.cursor;
      if (cursor != null) {
         if ((!Float.isFinite(cursor.x) || !Float.isFinite(cursor.y) || !Float.isFinite(cursor.z)) && this.flagAndAlert() && this.shouldModifyPackets() && this.shouldCancel()) {
            place.resync();
         }

      }
   }
}
