package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CheckData(
   name = "MultiPlace",
   description = "Placed multiple blocks in a tick",
   experimental = true
)
public class MultiPlace extends BlockPlaceCheck {
   private final List<String> flags = new ArrayList();
   private boolean hasPlaced;
   private BlockFace lastFace;
   private Vector3f lastCursor;
   private Vector3i lastPos;

   public MultiPlace(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      BlockFace face = place.getFace();
      Vector3f cursor = place.cursor;
      Vector3i pos = place.position;
      if (this.hasPlaced && (face != this.lastFace || !cursor.equals(this.lastCursor) || !pos.equals(this.lastPos))) {
         String var10000 = String.valueOf(face);
         String verbose = "face=" + var10000 + ", lastFace=" + String.valueOf(this.lastFace) + ", cursor=" + MessageUtil.toUnlabledString(cursor) + ", lastCursor=" + MessageUtil.toUnlabledString(this.lastCursor) + ", pos=" + MessageUtil.toUnlabledString(pos) + ", lastPos=" + MessageUtil.toUnlabledString(this.lastPos);
         if (!this.player.canSkipTicks()) {
            if (this.flagAndAlert(verbose) && this.shouldModifyPackets() && this.shouldCancel()) {
               place.resync();
            }
         } else {
            this.flags.add(verbose);
         }
      }

      this.lastFace = face;
      this.lastCursor = cursor;
      this.lastPos = pos;
      this.hasPlaced = true;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
         this.hasPlaced = false;
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
