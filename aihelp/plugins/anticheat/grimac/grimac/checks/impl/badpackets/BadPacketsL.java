package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import java.util.Locale;

@CheckData(
   name = "BadPacketsL",
   description = "Sent impossible dig packet"
)
public class BadPacketsL extends Check implements PacketCheck {
   public BadPacketsL(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
         WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(event);
         if (packet.getAction() == DiggingAction.START_DIGGING || packet.getAction() == DiggingAction.FINISHED_DIGGING || packet.getAction() == DiggingAction.CANCELLED_DIGGING) {
            return;
         }

         int expectedFace = this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10) && packet.getAction() == DiggingAction.RELEASE_USE_ITEM ? 255 : 0;
         if (packet.getBlockFaceId() != expectedFace || packet.getBlockPosition().getX() != 0 || packet.getBlockPosition().getY() != 0 || packet.getBlockPosition().getZ() != 0 || packet.getSequence() != 0) {
            int var10001 = packet.getBlockPosition().getX();
            if (this.flagAndAlert("pos=" + var10001 + ", " + packet.getBlockPosition().getY() + ", " + packet.getBlockPosition().getZ() + ", face=" + String.valueOf(packet.getBlockFace()) + ", sequence=" + packet.getSequence() + ", action=" + packet.getAction().toString().toLowerCase(Locale.ROOT)) && this.shouldModifyPackets() && packet.getAction() != DiggingAction.RELEASE_USE_ITEM) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         }
      }

   }
}
