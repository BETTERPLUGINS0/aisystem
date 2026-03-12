package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "PacketOrderN",
   experimental = true
)
public class PacketOrderN extends BlockPlaceCheck {
   private int invalid;
   private boolean usingWithoutPlacing;
   private boolean placing;

   public PacketOrderN(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      this.placing = true;
      if (this.usingWithoutPlacing) {
         if (!this.player.canSkipTicks()) {
            if (this.flagAndAlert() && this.shouldModifyPackets() && this.shouldCancel()) {
               place.resync();
            }
         } else {
            ++this.invalid;
         }
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.USE_ITEM || event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT && (new WrapperPlayClientPlayerBlockPlacement(event)).getFace() == BlockFace.OTHER) {
         if (!this.placing) {
            this.usingWithoutPlacing = true;
         }

         this.placing = false;
      }

      if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
         this.usingWithoutPlacing = this.placing = false;
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
