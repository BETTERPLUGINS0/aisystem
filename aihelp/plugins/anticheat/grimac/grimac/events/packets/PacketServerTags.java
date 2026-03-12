package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTags;

public class PacketServerTags extends PacketListenerAbstract {
   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.TAGS || event.getPacketType() == PacketType.Configuration.Server.UPDATE_TAGS) {
         GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         WrapperPlayServerTags tags = new WrapperPlayServerTags(event);
         boolean isPlay = event.getPacketType() == PacketType.Play.Server.TAGS;
         if (isPlay) {
            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
               player.tagManager.handleTagSync(tags);
            });
         } else {
            player.tagManager.handleTagSync(tags);
         }
      }

   }
}
