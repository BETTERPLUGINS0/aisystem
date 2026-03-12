package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

public class PacketPlayerTick extends PacketListenerAbstract {
   public PacketPlayerTick() {
      super(PacketListenerPriority.LOW);
   }

   public boolean isPreVia() {
      return true;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      GrimPlayer player;
      PacketWorldBorder border;
      if (event.getPacketType() == PacketType.Play.Client.CLIENT_TICK_END) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null || player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2)) {
            return;
         }

         border = (PacketWorldBorder)player.checkManager.getPacketCheck(PacketWorldBorder.class);
         border.tickBorder();
      } else if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
            return;
         }

         border = (PacketWorldBorder)player.checkManager.getPacketCheck(PacketWorldBorder.class);
         border.tickBorder();
      }

   }
}
