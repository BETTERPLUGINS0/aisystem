package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

@CheckData(
   name = "PositionPlace",
   description = "Placed a block against a hidden face"
)
public class PositionPlace extends BlockPlaceCheck {
   public PositionPlace(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      if (place.material != StateTypes.SCAFFOLDING && !this.player.inVehicle()) {
         SimpleCollisionBox combined = this.getCombinedBox(place);
         double[] possibleEyeHeights = this.player.getPossibleEyeHeights();
         double minEyeHeight = Double.MAX_VALUE;
         double maxEyeHeight = Double.MIN_VALUE;
         double[] var8 = possibleEyeHeights;
         int var9 = possibleEyeHeights.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            double height = var8[var10];
            minEyeHeight = Math.min(minEyeHeight, height);
            maxEyeHeight = Math.max(maxEyeHeight, height);
         }

         double movementThreshold = this.player.packetStateData.didLastMovementIncludePosition && !this.player.canSkipTicks() ? 0.0D : this.player.getMovementThreshold();
         SimpleCollisionBox eyePositions = new SimpleCollisionBox(this.player.x, this.player.y + minEyeHeight, this.player.z, this.player.x, this.player.y + maxEyeHeight, this.player.z);
         eyePositions.expand(movementThreshold);
         if (!eyePositions.isIntersected(combined)) {
            boolean var10000;
            switch(place.getFace()) {
            case NORTH:
               var10000 = eyePositions.minZ > combined.minZ;
               break;
            case SOUTH:
               var10000 = eyePositions.maxZ < combined.maxZ;
               break;
            case EAST:
               var10000 = eyePositions.maxX < combined.maxX;
               break;
            case WEST:
               var10000 = eyePositions.minX > combined.minX;
               break;
            case UP:
               var10000 = eyePositions.maxY < combined.maxY;
               break;
            case DOWN:
               var10000 = eyePositions.minY > combined.minY;
               break;
            default:
               var10000 = false;
            }

            boolean flag = var10000;
            if (flag && this.flagAndAlert() && this.shouldModifyPackets() && this.shouldCancel()) {
               place.resync();
            }

         }
      }
   }
}
