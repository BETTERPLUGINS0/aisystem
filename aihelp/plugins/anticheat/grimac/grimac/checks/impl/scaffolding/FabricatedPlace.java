package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.nmsutil.Materials;

@CheckData(
   name = "FabricatedPlace",
   description = "Sent out of bounds cursor position"
)
public class FabricatedPlace extends BlockPlaceCheck {
   private static final double MAX_DOUBLE_ERROR = Math.ulp(3.0E7D) * 2.0D;
   private static final double FLOAT_STEP_AT_ONE = (double)Math.ulp(1.0F);

   public FabricatedPlace(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      Vector3f cursor = place.cursor;
      if (cursor != null) {
         boolean isExtended = Materials.isShapeExceedsCube(place.getPlacedAgainstMaterial()) || place.getPlacedAgainstMaterial() == StateTypes.LECTERN;
         double maxBound = isExtended ? 1.5D : 1.0D;
         double minBound = 1.0D - maxBound;
         if (!((double)cursor.getX() < minBound - MAX_DOUBLE_ERROR) && !((double)cursor.getY() < minBound - MAX_DOUBLE_ERROR) && !((double)cursor.getZ() < minBound - MAX_DOUBLE_ERROR)) {
            double upperTolerance = FLOAT_STEP_AT_ONE;
            if ((double)cursor.getX() > maxBound + upperTolerance || (double)cursor.getY() > maxBound + upperTolerance || (double)cursor.getZ() > maxBound + upperTolerance) {
               String debug = String.format("cursor=%s limit=%.16f", cursor, maxBound + upperTolerance);
               if (this.flagAndAlert(debug) && this.shouldModifyPackets() && this.shouldCancel()) {
                  place.resync();
               }
            }

         } else {
            String debug = String.format("cursor=%s limit=%.16f", cursor, minBound - MAX_DOUBLE_ERROR);
            if (this.flagAndAlert(debug) && this.shouldModifyPackets() && this.shouldCancel()) {
               place.resync();
            }

         }
      }
   }
}
