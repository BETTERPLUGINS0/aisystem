package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(
   name = "CrashA"
)
public class CrashA extends Check implements PacketCheck {
   private static final double HARD_CODED_BORDER = 2.9999999E7D;

   public CrashA(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.packetStateData.lastPacketWasTeleport) {
         if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(event);
            if (!packet.hasPositionChanged()) {
               return;
            }

            if (Math.abs(packet.getLocation().getX()) > 2.9999999E7D || Math.abs(packet.getLocation().getZ()) > 2.9999999E7D || Math.abs(packet.getLocation().getY()) > 2.147483647E9D) {
               this.flagAndAlert();
               this.player.getSetbackTeleportUtil().executeViolationSetback();
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         }

      }
   }
}
