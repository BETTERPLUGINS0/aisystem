package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerServerLinks;
import java.util.List;

public class WrapperPlayServerServerLinks extends WrapperCommonServerServerLinks<WrapperPlayServerServerLinks> {
   public WrapperPlayServerServerLinks(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerServerLinks(List<WrapperCommonServerServerLinks.ServerLink> links) {
      super(PacketType.Play.Server.SERVER_LINKS, links);
   }
}
