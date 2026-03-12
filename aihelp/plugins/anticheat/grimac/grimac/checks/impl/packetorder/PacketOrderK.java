package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayDeque;
import java.util.Iterator;

@CheckData(
   name = "PacketOrderK",
   experimental = true
)
public class PacketOrderK extends Check implements PostPredictionCheck {
   private final ArrayDeque<String> flags = new ArrayDeque();

   public PacketOrderK(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      String verbose;
      if (event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && (new WrapperPlayClientClientStatus(event)).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT && (this.player.packetOrderProcessor.isClickingInInventory() || this.player.packetOrderProcessor.isClosingInventory())) {
         boolean var10000 = this.player.packetOrderProcessor.isClickingInInventory();
         verbose = "open, clicking=" + var10000 + ", closing=" + this.player.packetOrderProcessor.isClosingInventory();
         if (!this.player.canSkipTicks()) {
            this.flagAndAlert(verbose);
         } else {
            this.flags.add(verbose);
         }
      }

      if ((event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW || event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) && this.player.packetOrderProcessor.isOpeningInventory()) {
         verbose = event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW ? "click" : "close";
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
