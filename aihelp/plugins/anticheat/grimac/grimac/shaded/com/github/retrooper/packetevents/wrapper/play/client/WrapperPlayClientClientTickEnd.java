package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientClientTickEnd extends PacketWrapper<WrapperPlayClientClientTickEnd> {
   public WrapperPlayClientClientTickEnd(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientClientTickEnd() {
      super((PacketTypeCommon)PacketType.Play.Client.CLIENT_TICK_END);
   }
}
