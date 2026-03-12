package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerResetChat extends PacketWrapper<WrapperConfigServerResetChat> {
   public WrapperConfigServerResetChat(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerResetChat() {
      super((PacketTypeCommon)PacketType.Configuration.Server.RESET_CHAT);
   }
}
