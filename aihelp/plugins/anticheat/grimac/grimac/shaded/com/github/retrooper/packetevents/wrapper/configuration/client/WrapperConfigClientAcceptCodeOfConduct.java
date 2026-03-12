package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperConfigClientAcceptCodeOfConduct extends PacketWrapper<WrapperConfigClientAcceptCodeOfConduct> {
   public WrapperConfigClientAcceptCodeOfConduct(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientAcceptCodeOfConduct() {
      super((PacketTypeCommon)PacketType.Configuration.Client.ACCEPT_CODE_OF_CONDUCT);
   }
}
