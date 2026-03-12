package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerConfigurationStart extends PacketWrapper<WrapperPlayServerConfigurationStart> {
   public WrapperPlayServerConfigurationStart(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerConfigurationStart() {
      super((PacketTypeCommon)PacketType.Play.Server.CONFIGURATION_START);
   }
}
