package ac.grim.grimac.checks.impl.timer;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(
   name = "TickTimer",
   setback = 1.0D
)
public class TickTimer extends Check implements PacketCheck {
   private boolean receivedTickEnd = true;
   private int flyingPackets = 0;

   public TickTimer(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (this.player.supportsEndTick()) {
         if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && !this.player.packetStateData.lastPacketWasTeleport) {
            if (!this.receivedTickEnd && this.flagAndAlertWithSetback("type=flying, packets=" + this.flyingPackets)) {
               this.handleViolation();
            }

            this.receivedTickEnd = false;
            ++this.flyingPackets;
         } else if (event.getPacketType() == PacketType.Play.Client.CLIENT_TICK_END) {
            this.receivedTickEnd = true;
            if (this.flyingPackets > 1 && this.flagAndAlertWithSetback("type=end, packets=" + this.flyingPackets)) {
               this.handleViolation();
            }

            this.flyingPackets = 0;
         }

      }
   }

   private void handleViolation() {
      this.player.onPacketCancel();
   }
}
