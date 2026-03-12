package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerPing;

public class WrapperPlayServerPing extends WrapperCommonServerPing<WrapperPlayServerPing> {
   public WrapperPlayServerPing(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerPing(int id) {
      super(PacketType.Play.Server.PING, id);
   }
}
