package ac.grim.grimac.predictionengine;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;

public class GhostBlockDetector extends Check implements PostPredictionCheck {
   public GhostBlockDetector(GrimPlayer player) {
      super(player);
   }

   public static boolean isGhostBlock(GrimPlayer player) {
      if (player.uncertaintyHandler.isOrWasNearGlitchyBlock) {
         return true;
      } else {
         if (player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
            SimpleCollisionBox largeExpandedBB = player.boundingBox.copy().expand(12.0D, 0.5D, 12.0D);
            ObjectIterator var2 = player.compensatedEntities.entityMap.values().iterator();

            while(var2.hasNext()) {
               PacketEntity entity = (PacketEntity)var2.next();
               if (entity.isBoat && entity.getPossibleCollisionBoxes().isIntersected(largeExpandedBB)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (!(predictionComplete.getOffset() < 0.001D) || this.player.clientClaimsLastOnGround != this.player.onGround && !this.player.inVehicle()) {
         boolean shouldResync = isGhostBlock(this.player);
         if (shouldResync) {
            if (this.player.clientClaimsLastOnGround != this.player.onGround) {
               this.player.onGround = this.player.clientClaimsLastOnGround;
            }

            predictionComplete.setOffset(0.0D);
            this.player.getSetbackTeleportUtil().executeForceResync();
         }

      }
   }
}
