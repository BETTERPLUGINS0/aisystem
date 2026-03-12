package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerServerLinks;
import java.util.List;

public class WrapperConfigServerServerLinks extends WrapperCommonServerServerLinks<WrapperConfigServerServerLinks> {
   public WrapperConfigServerServerLinks(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerServerLinks(List<WrapperCommonServerServerLinks.ServerLink> links) {
      super(PacketType.Configuration.Server.SERVER_LINKS, links);
   }
}
