package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerLoaded extends PacketWrapper<WrapperPlayClientPlayerLoaded> {
   public WrapperPlayClientPlayerLoaded(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerLoaded() {
      super((PacketTypeCommon)PacketType.Play.Client.PLAYER_LOADED);
   }
}
