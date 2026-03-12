package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigClientConfigurationEndAck extends PacketWrapper<WrapperConfigClientConfigurationEndAck> {
   public WrapperConfigClientConfigurationEndAck(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientConfigurationEndAck() {
      super((PacketTypeCommon)PacketType.Configuration.Client.CONFIGURATION_END_ACK);
   }
}
