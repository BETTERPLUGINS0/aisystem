package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CheckData(
   name = "MultiBreak",
   experimental = true
)
public class MultiBreak extends Check implements BlockBreakCheck {
   private final List<String> flags = new ArrayList();
   private boolean hasBroken;
   private BlockFace lastFace;
   private Vector3i lastPos;

   public MultiBreak(GrimPlayer player) {
      super(player);
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (blockBreak.action != DiggingAction.CANCELLED_DIGGING) {
         if (this.hasBroken && (blockBreak.face != this.lastFace || !blockBreak.position.equals(this.lastPos))) {
            String var10000 = String.valueOf(blockBreak.face);
            String verbose = "face=" + var10000 + ", lastFace=" + String.valueOf(this.lastFace) + ", pos=" + MessageUtil.toUnlabledString(blockBreak.position) + ", lastPos=" + MessageUtil.toUnlabledString(this.lastPos);
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                  blockBreak.cancel();
               }
            } else {
               this.flags.add(verbose);
            }
         }

         this.lastFace = blockBreak.face;
         this.lastPos = blockBreak.position;
         this.hasBroken = true;
      }
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
         this.hasBroken = false;
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.canSkipTicks()) {
         if (this.player.isTickingReliablyFor(3)) {
            Iterator var2 = this.flags.iterator();

            while(var2.hasNext()) {
               String verbose = (String)var2.next();
               this.flagAndAlert(verbose);
            }
         }

         this.flags.clear();
      }
   }
}
