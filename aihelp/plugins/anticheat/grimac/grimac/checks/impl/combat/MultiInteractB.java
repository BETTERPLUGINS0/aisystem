package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;
import java.util.Iterator;

@CheckData(
   name = "MultiInteractB",
   experimental = true
)
public class MultiInteractB extends Check implements PostPredictionCheck {
   private final ArrayList<String> flags = new ArrayList();
   private Vector3f lastPos;
   private boolean hasInteracted = false;

   public MultiInteractB(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
         Vector3f pos = (Vector3f)(new WrapperPlayClientInteractEntity(event)).getTarget().orElse((Object)null);
         if (pos == null) {
            return;
         }

         if (this.hasInteracted && !pos.equals(this.lastPos)) {
            String var10000 = MessageUtil.toUnlabledString(pos);
            String verbose = "pos=" + var10000 + ", lastPos=" + MessageUtil.toUnlabledString(this.lastPos);
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               this.flags.add(verbose);
            }
         }

         this.lastPos = pos;
         this.hasInteracted = true;
      }

      if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
         this.hasInteracted = false;
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
