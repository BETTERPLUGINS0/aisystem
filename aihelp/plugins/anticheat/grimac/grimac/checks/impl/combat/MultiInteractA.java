package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;
import java.util.Iterator;

@CheckData(
   name = "MultiInteractA",
   description = "Interacted with multiple entities in the same tick",
   experimental = true
)
public class MultiInteractA extends Check implements PostPredictionCheck {
   private final ArrayList<String> flags = new ArrayList();
   private int lastEntity;
   private boolean lastSneaking;
   private boolean hasInteracted = false;

   public MultiInteractA(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
         WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
         int entity = packet.getEntityId();
         boolean sneaking = (Boolean)packet.isSneaking().orElse(false);
         if (this.hasInteracted && entity != this.lastEntity) {
            String verbose = "lastEntity=" + this.lastEntity + ", entity=" + entity + ", lastSneaking=" + this.lastSneaking + ", sneaking=" + sneaking;
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               this.flags.add(verbose);
            }
         }

         this.lastEntity = entity;
         this.lastSneaking = sneaking;
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
