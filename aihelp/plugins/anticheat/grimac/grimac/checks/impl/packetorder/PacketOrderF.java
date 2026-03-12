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
   name = "PacketOrderF",
   experimental = true
)
public class PacketOrderF extends Check implements PostPredictionCheck {
   private final ArrayDeque<String> flags = new ArrayDeque();

   public PacketOrderF(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if ((event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY || event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT || event.getPacketType() == PacketType.Play.Client.USE_ITEM || event.getPacketType() == PacketType.Play.Client.PICK_ITEM || event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING || event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && (new WrapperPlayClientClientStatus(event)).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT) && (this.player.packetOrderProcessor.isSprinting() || this.player.packetOrderProcessor.isSneaking())) {
         String var10000 = event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY ? "interact" : (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT ? "place" : (event.getPacketType() == PacketType.Play.Client.USE_ITEM ? "use" : (event.getPacketType() == PacketType.Play.Client.PICK_ITEM ? "pick" : (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING ? "dig" : "openInventory"))));
         String verbose = "action=" + var10000 + ", sprinting=" + this.player.packetOrderProcessor.isSprinting() + ", sneaking=" + this.player.packetOrderProcessor.isSneaking();
         if (!this.player.canSkipTicks()) {
            if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
               if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING && (new WrapperPlayClientPlayerDigging(event)).getAction() == DiggingAction.RELEASE_USE_ITEM) {
                  return;
               }

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
