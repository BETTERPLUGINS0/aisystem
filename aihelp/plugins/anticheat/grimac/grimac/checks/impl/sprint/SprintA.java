package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(
   name = "SprintA",
   description = "Sprinting with too low hunger",
   setback = 0.0D
)
public class SprintA extends Check implements PacketCheck {
   public SprintA(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
         if (this.player.canFly) {
            return;
         }

         if ((float)this.player.food < 6.0F && this.player.isSprinting) {
            if (this.flagAndAlert("hunger=" + this.player.food)) {
               if (this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }

               if (this.shouldSetback()) {
                  this.player.getSetbackTeleportUtil().executeNonSimulatingSetback();
               }
            }
         } else {
            this.reward();
         }
      }

   }
}
