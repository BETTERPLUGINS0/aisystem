package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientLoginSuccessAck extends PacketWrapper<WrapperLoginClientLoginSuccessAck> {
   public WrapperLoginClientLoginSuccessAck(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperLoginClientLoginSuccessAck() {
      super((PacketTypeCommon)PacketType.Login.Client.LOGIN_SUCCESS_ACK);
   }
}
