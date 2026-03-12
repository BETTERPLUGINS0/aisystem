package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerConfigurationEnd extends PacketWrapper<WrapperConfigServerConfigurationEnd> {
   public WrapperConfigServerConfigurationEnd(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerConfigurationEnd() {
      super((PacketTypeCommon)PacketType.Configuration.Server.CONFIGURATION_END);
   }
}
