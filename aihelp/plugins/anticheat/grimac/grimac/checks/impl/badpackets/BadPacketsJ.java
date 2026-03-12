package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.utils.data.HeadRotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CheckData(
   name = "BadPacketsJ",
   description = "Rotation in use item packet did not match tick rotation"
)
public class BadPacketsJ extends Check implements PacketCheck {
   private final List<HeadRotation> rotations = new ArrayList();

   public BadPacketsJ(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (!this.player.cameraEntity.isSelf()) {
         this.rotations.clear();
      } else {
         if (event.getPacketType() == PacketType.Play.Client.USE_ITEM && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            WrapperPlayClientUseItem packet = new WrapperPlayClientUseItem(event);
            this.rotations.add(new HeadRotation(packet.getYaw(), packet.getPitch()));
         }

         if (this.isTickPacket(event.getPacketType())) {
            boolean allowLast = this.player.canSkipTicks() && (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION);
            Iterator var3 = this.rotations.iterator();

            while(true) {
               while(var3.hasNext()) {
                  HeadRotation rotation = (HeadRotation)var3.next();
                  if (rotation.yaw() == this.player.yaw && rotation.pitch() == this.player.pitch) {
                     allowLast = false;
                  } else if (rotation.yaw() != this.player.lastYaw || rotation.pitch() != this.player.lastPitch || !allowLast) {
                     this.flagAndAlert();
                  }
               }

               this.rotations.clear();
               break;
            }
         }

      }
   }
}
