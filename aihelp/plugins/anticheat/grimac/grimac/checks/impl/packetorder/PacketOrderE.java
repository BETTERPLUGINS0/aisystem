package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayDeque;
import java.util.Iterator;

@CheckData(
   name = "PacketOrderE",
   experimental = true
)
public class PacketOrderE extends Check implements PostPredictionCheck {
   private final ArrayDeque<String> flags = new ArrayDeque();
   private boolean setback;

   public PacketOrderE(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE && (this.player.packetOrderProcessor.isAttacking() || this.player.packetOrderProcessor.isRightClicking() || this.player.packetOrderProcessor.isOpeningInventory() || this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isSneaking() || this.player.packetOrderProcessor.isSprinting() || this.player.packetOrderProcessor.isLeavingBed() || this.player.packetOrderProcessor.isStartingToGlide() || this.player.packetOrderProcessor.isJumpingWithMount())) {
         boolean var10000 = this.player.packetOrderProcessor.isAttacking();
         String verbose = "attacking=" + var10000 + ", rightClicking=" + this.player.packetOrderProcessor.isRightClicking() + ", openingInventory=" + this.player.packetOrderProcessor.isOpeningInventory() + ", releasing=" + this.player.packetOrderProcessor.isReleasing() + ", sneaking=" + this.player.packetOrderProcessor.isSneaking() + ", sprinting=" + this.player.packetOrderProcessor.isSprinting() + ", bed=" + this.player.packetOrderProcessor.isLeavingBed() + ", sprinting=" + this.player.packetOrderProcessor.isSprinting() + ", gliding=" + this.player.packetOrderProcessor.isStartingToGlide() + ", mountJumping=" + this.player.packetOrderProcessor.isJumpingWithMount();
         if ((this.player.canSkipTicks() && this.flags.add(verbose) || this.flagAndAlert(verbose)) && this.player.packetOrderProcessor.isUsing()) {
            this.setback = true;
         }
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (!this.player.canSkipTicks()) {
         if (this.setback) {
            this.setback = false;
            this.setbackIfAboveSetbackVL();
         }

      } else {
         if (this.player.isTickingReliablyFor(3)) {
            Iterator var2 = this.flags.iterator();

            while(var2.hasNext()) {
               String verbose = (String)var2.next();
               if (this.flagAndAlert(verbose) && this.setback) {
                  this.setback = false;
                  this.setbackIfAboveSetbackVL();
               }
            }
         }

         this.setback = false;
         this.flags.clear();
      }
   }
}
