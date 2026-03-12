package ac.grim.grimac.checks.impl.elytra;

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
   name = "ElytraH",
   description = "Started gliding in vehicle",
   experimental = true
)
public class ElytraH extends Check implements PostPredictionCheck {
   private boolean setback;

   public ElytraH(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
         if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION && (new WrapperPlayClientEntityAction(event)).getAction() == WrapperPlayClientEntityAction.Action.START_FLYING_WITH_ELYTRA && this.player.inVehicle() && this.flagAndAlert()) {
            this.setback = true;
            if (this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
               this.player.resyncPose();
            }
         }

      }
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.setback) {
         this.setbackIfAboveSetbackVL();
         this.setback = false;
      }

   }
}
