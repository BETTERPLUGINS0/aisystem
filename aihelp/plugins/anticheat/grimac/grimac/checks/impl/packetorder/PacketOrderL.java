package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayDeque;
import java.util.Iterator;

@CheckData(
   name = "PacketOrderL",
   experimental = true
)
public class PacketOrderL extends Check implements PostPredictionCheck {
   private final ArrayDeque<String> flags = new ArrayDeque();

   public PacketOrderL(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && (new WrapperPlayClientClientStatus(event)).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT && this.player.packetOrderProcessor.isDropping()) {
         if (!this.player.canSkipTicks()) {
            if (this.flagAndAlert("inventory") && this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         } else {
            this.flags.add("inventory");
         }
      }

      if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING && (new WrapperPlayClientPlayerDigging(event)).getAction() == DiggingAction.SWAP_ITEM_WITH_OFFHAND && this.player.packetOrderProcessor.isDropping()) {
         if (!this.player.canSkipTicks()) {
            if (this.flagAndAlert("swap") && this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         } else {
            this.flags.add("swap");
         }
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
