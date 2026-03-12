package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;

@CheckData(
   name = "PacketOrderC"
)
public class PacketOrderC extends Check implements PacketCheck {
   private final boolean exempt;
   private boolean sentInteractAt;
   private int requiredEntity;
   private InteractionHand requiredHand;
   private boolean requiredSneaking;

   public PacketOrderC(GrimPlayer player) {
      super(player);
      this.exempt = this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10);
      this.sentInteractAt = false;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.exempt) {
         if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            PacketEntity entity = (PacketEntity)this.player.compensatedEntities.entityMap.get(packet.getEntityId());
            if (entity != null && entity.type == EntityTypes.ARMOR_STAND) {
               return;
            }

            boolean sneaking = (Boolean)packet.isSneaking().orElse(false);
            switch(packet.getAction()) {
            case INTERACT:
               if (!this.sentInteractAt) {
                  if (this.flagAndAlert("Skipped Interact-At") && this.shouldModifyPackets()) {
                     event.setCancelled(true);
                     this.player.onPacketCancel();
                  }
               } else if (packet.getEntityId() != this.requiredEntity || packet.getHand() != this.requiredHand || sneaking != this.requiredSneaking) {
                  String verbose = "requiredEntity=" + this.requiredEntity + ", entity=" + packet.getEntityId() + ", requiredHand=" + String.valueOf(this.requiredHand) + ", hand=" + String.valueOf(packet.getHand()) + ", requiredSneaking=" + this.requiredSneaking + ", sneaking=" + sneaking;
                  if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                     event.setCancelled(true);
                     this.player.onPacketCancel();
                  }
               }

               this.sentInteractAt = false;
               break;
            case INTERACT_AT:
               if (this.sentInteractAt && this.flagAndAlert("Skipped Interact") && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }

               this.requiredHand = packet.getHand();
               this.requiredEntity = packet.getEntityId();
               this.requiredSneaking = sneaking;
               this.sentInteractAt = true;
            }
         }

         if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && this.sentInteractAt) {
            this.sentInteractAt = false;
            this.flagAndAlert("Skipped Interact (Tick)");
         }

      }
   }
}
