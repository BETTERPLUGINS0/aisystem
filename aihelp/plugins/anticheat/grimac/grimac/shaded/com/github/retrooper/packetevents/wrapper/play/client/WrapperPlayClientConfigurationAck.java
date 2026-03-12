package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientConfigurationAck extends PacketWrapper<WrapperPlayClientConfigurationAck> {
   public WrapperPlayClientConfigurationAck(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientConfigurationAck() {
      super((PacketTypeCommon)PacketType.Play.Client.CONFIGURATION_ACK);
   }
}
