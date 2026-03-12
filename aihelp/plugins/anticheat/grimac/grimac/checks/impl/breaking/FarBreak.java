package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;

@CheckData(
   name = "FarBreak",
   description = "Breaking blocks too far away",
   experimental = true
)
public class FarBreak extends Check implements BlockBreakCheck {
   public FarBreak(GrimPlayer player) {
      super(player);
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (this.player.cameraEntity.isSelf() && !this.player.inVehicle() && blockBreak.action != DiggingAction.CANCELLED_DIGGING) {
         double min = Double.MAX_VALUE;
         double[] var4 = this.player.getPossibleEyeHeights();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            double d = var4[var6];
            SimpleCollisionBox box = new SimpleCollisionBox(blockBreak.position);
            Vector3dm best = VectorUtils.cutBoxToVector(this.player.x, this.player.y + d, this.player.z, box);
            min = Math.min(min, best.distanceSquared(this.player.x, this.player.y + d, this.player.z));
         }

         double maxReach = this.player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
         if (this.player.packetStateData.didLastMovementIncludePosition || this.player.canSkipTicks()) {
            double threshold = this.player.getMovementThreshold();
            maxReach += Math.hypot(threshold, threshold);
         }

         if (min > maxReach * maxReach && this.flagAndAlert(String.format("distance=%.2f", Math.sqrt(min))) && this.shouldModifyPackets()) {
            blockBreak.cancel();
         }

      }
   }
}
