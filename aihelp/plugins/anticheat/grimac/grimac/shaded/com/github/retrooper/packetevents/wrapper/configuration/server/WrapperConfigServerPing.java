package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerPing;

public class WrapperConfigServerPing extends WrapperCommonServerPing<WrapperConfigServerPing> {
   public WrapperConfigServerPing(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerPing(int id) {
      super(PacketType.Configuration.Server.PING, id);
   }
}
