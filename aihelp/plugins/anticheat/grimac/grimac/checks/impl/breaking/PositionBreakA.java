package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

@CheckData(
   name = "PositionBreakA"
)
public class PositionBreakA extends Check implements BlockBreakCheck {
   public PositionBreakA(GrimPlayer player) {
      super(player);
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (!this.player.inVehicle() && blockBreak.action != DiggingAction.CANCELLED_DIGGING && blockBreak.block.getType() != StateTypes.REDSTONE_WIRE) {
         SimpleCollisionBox combined = blockBreak.getCombinedBox();
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

         SimpleCollisionBox eyePositions = new SimpleCollisionBox(this.player.x, this.player.y + minEyeHeight, this.player.z, this.player.x, this.player.y + maxEyeHeight, this.player.z);
         if (!this.player.packetStateData.didLastMovementIncludePosition || this.player.canSkipTicks()) {
            eyePositions.expand(this.player.getMovementThreshold());
         }

         if (!eyePositions.isIntersected(combined)) {
            boolean var10000;
            switch(blockBreak.face) {
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
            if (flag) {
               String var10001 = String.valueOf(blockBreak.action);
               if (this.flagAndAlert("action=" + var10001 + ", face=" + String.valueOf(blockBreak.face)) && this.shouldModifyPackets()) {
                  blockBreak.cancel();
               }
            }

         }
      }
   }
}
