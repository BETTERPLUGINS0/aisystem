package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "PacketOrderH",
   experimental = true
)
public class PacketOrderH extends Check implements PostPredictionCheck {
   private int invalid;

   public PacketOrderH(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
         switch((new WrapperPlayClientEntityAction(event)).getAction()) {
         case START_SPRINTING:
         case STOP_SPRINTING:
            if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2) && this.player.packetOrderProcessor.isSneaking()) {
               if (!this.player.canSkipTicks()) {
                  this.flagAndAlert();
               } else {
                  ++this.invalid;
               }
            }
            break;
         case START_SNEAKING:
         case STOP_SNEAKING:
            if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) && this.player.packetOrderProcessor.isSprinting()) {
               if (!this.player.canSkipTicks()) {
                  this.flagAndAlert();
               } else {
                  ++this.invalid;
               }
            }
         }
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
