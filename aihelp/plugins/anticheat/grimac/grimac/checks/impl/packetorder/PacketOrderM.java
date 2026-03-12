package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "PacketOrderM",
   experimental = true
)
public class PacketOrderM extends Check implements PostPredictionCheck {
   private int invalid;
   private boolean usingWithoutInteract;
   private boolean interacting;

   public PacketOrderM(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY && (new WrapperPlayClientInteractEntity(event)).getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
         this.interacting = true;
         if (this.usingWithoutInteract) {
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert() && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               ++this.invalid;
            }
         }
      }

      if (event.getPacketType() == PacketType.Play.Client.USE_ITEM || event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT && (new WrapperPlayClientPlayerBlockPlacement(event)).getFace() == BlockFace.OTHER) {
         if (!this.interacting) {
            this.usingWithoutInteract = true;
         }

         this.interacting = false;
      }

      if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
         this.usingWithoutInteract = this.interacting = false;
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.canSkipTicks()) {
         if (this.player.isTickingReliablyFor(3)) {
            while(this.invalid >= 1) {
               this.flagAndAlert();
               --this.invalid;
            }
         }

         this.invalid = 0;
      }
   }
}
