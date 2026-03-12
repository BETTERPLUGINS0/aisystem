package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(
   name = "BadPacketsV",
   description = "Did not move far enough",
   experimental = true
)
public class BadPacketsV extends Check implements PacketCheck {
   private int noReminderTicks;

   public BadPacketsV(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.canSkipTicks() && this.isTickPacket(event.getPacketType())) {
         if (event.getPacketType() != PacketType.Play.Client.PLAYER_POSITION && event.getPacketType() != PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            ++this.noReminderTicks;
         } else {
            int positionAtLeastEveryNTicks = this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) ? 20 : 19;
            if (this.noReminderTicks < positionAtLeastEveryNTicks && !this.player.uncertaintyHandler.lastTeleportTicks.hasOccurredSince(1)) {
               double deltaSq = (new WrapperPlayClientPlayerFlying(event)).getLocation().getPosition().distanceSquared(new Vector3d(this.player.lastX, this.player.lastY, this.player.lastZ));
               if (deltaSq <= this.player.getMovementThreshold() * this.player.getMovementThreshold()) {
                  this.flagAndAlert("delta=" + Math.sqrt(deltaSq));
               }
            }

            this.noReminderTicks = 0;
         }
      }

   }
}
