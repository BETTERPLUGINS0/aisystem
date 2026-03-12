package ac.grim.grimac.checks.impl.elytra;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "ElytraB",
   description = "Started gliding without jumping"
)
public class ElytraB extends Check implements PostPredictionCheck {
   private boolean glide;
   private boolean setback;

   public ElytraB(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION && (new WrapperPlayClientEntityAction(event)).getAction() == WrapperPlayClientEntityAction.Action.START_FLYING_WITH_ELYTRA && this.player.supportsEndTick()) {
         if (this.player.packetStateData.knownInput.jump()) {
            if (this.flagAndAlert("no release")) {
               this.setback = true;
               if (this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
                  this.player.resyncPose();
               }
            }
         } else {
            this.glide = true;
         }
      }

      if (this.isUpdate(event.getPacketType())) {
         if (this.glide && !this.player.packetStateData.knownInput.jump() && this.flagAndAlert("no jump")) {
            this.setback = true;
         }

         this.glide = false;
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.setback) {
         this.setback = false;
         this.setbackIfAboveSetbackVL();
      }

   }
}
