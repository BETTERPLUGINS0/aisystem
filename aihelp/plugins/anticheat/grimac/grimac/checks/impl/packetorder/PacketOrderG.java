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
   name = "PacketOrderG",
   experimental = true
)
public class PacketOrderG extends Check implements PostPredictionCheck {
   private final ArrayDeque<String> flags = new ArrayDeque();

   public PacketOrderG(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING || event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && (new WrapperPlayClientClientStatus(event)).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT) {
         DiggingAction action = null;
         if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            action = (new WrapperPlayClientPlayerDigging(event)).getAction();
            if (action == DiggingAction.RELEASE_USE_ITEM || action == DiggingAction.START_DIGGING || action == DiggingAction.CANCELLED_DIGGING || action == DiggingAction.FINISHED_DIGGING) {
               return;
            }
         }

         if (this.player.packetOrderProcessor.isAttacking() || this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isRightClicking() || this.player.packetOrderProcessor.isPicking() || this.player.packetOrderProcessor.isDigging()) {
            String var10000 = action == null ? "openInventory" : (action == DiggingAction.SWAP_ITEM_WITH_OFFHAND ? "swap" : "drop");
            String verbose = "action=" + var10000 + ", attacking=" + this.player.packetOrderProcessor.isAttacking() + ", releasing=" + this.player.packetOrderProcessor.isReleasing() + ", rightClicking=" + this.player.packetOrderProcessor.isRightClicking() + ", picking=" + this.player.packetOrderProcessor.isPicking() + ", digging=" + this.player.packetOrderProcessor.isDigging();
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               this.flags.add(verbose);
            }
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
