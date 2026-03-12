package ac.grim.grimac.checks.impl.multiactions;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CheckData(
   name = "MultiActionsF",
   description = "Interacting with a block and an entity in the same tick",
   experimental = true
)
public class MultiActionsF extends BlockPlaceCheck {
   private final List<String> flags = new ArrayList();
   private boolean entity;
   private boolean block;

   public MultiActionsF(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
      this.block = true;
      if (this.entity) {
         if (!this.player.canSkipTicks()) {
            if (this.flagAndAlert("place") && this.shouldModifyPackets() && this.shouldCancel()) {
               place.resync();
            }
         } else {
            this.flags.add("place");
         }
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
         this.entity = true;
         if (this.block) {
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert("entity") && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               this.flags.add("entity");
            }
         }
      }

      if (this.isTickPacket(event.getPacketType())) {
         this.block = this.entity = false;
      }

   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (blockBreak.action == DiggingAction.START_DIGGING || blockBreak.action == DiggingAction.FINISHED_DIGGING) {
         this.block = true;
         if (this.entity) {
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert("dig") && this.shouldModifyPackets()) {
                  blockBreak.cancel();
               }
            } else {
               this.flags.add("dig");
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
