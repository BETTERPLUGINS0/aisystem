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
   name = "ElytraC",
   description = "Started gliding too frequently"
)
public class ElytraC extends Check implements PostPredictionCheck {
   private boolean glideThisTick;
   private boolean glideLastTick;
   private boolean setback;
   private int flags;
   public boolean exempt;

   public ElytraC(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
         if (!this.player.cameraEntity.isSelf()) {
            this.glideThisTick = this.glideLastTick = false;
         }

         if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION && (new WrapperPlayClientEntityAction(event)).getAction() == WrapperPlayClientEntityAction.Action.START_FLYING_WITH_ELYTRA && !this.exempt) {
            if (this.glideThisTick || this.glideLastTick) {
               if (this.player.canSkipTicks()) {
                  ++this.flags;
               } else if (this.flagAndAlert()) {
                  this.setback = true;
                  if (this.shouldModifyPackets()) {
                     event.setCancelled(true);
                     this.player.onPacketCancel();
                     this.player.resyncPose();
                  }
               }
            }

            this.glideThisTick = true;
         }

         if (this.isTickPacket(event.getPacketType())) {
            this.glideLastTick = this.glideThisTick;
            this.glideThisTick = this.exempt = false;
         }

      }
   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.canSkipTicks()) {
         if (this.player.isTickingReliablyFor(3)) {
            while(this.flags > 0) {
               this.flagAndAlert();
               --this.flags;
            }
         }

         this.flags = 0;
         this.setback = false;
      }

      if (this.setback) {
         this.setback = false;
         this.setbackIfAboveSetbackVL();
      }

   }
}
