package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEnterCombatEvent extends PacketWrapper<WrapperPlayServerEnterCombatEvent> {
   public WrapperPlayServerEnterCombatEvent(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEnterCombatEvent() {
      super((PacketTypeCommon)PacketType.Play.Server.ENTER_COMBAT_EVENT);
   }

   public void read() {
   }

   public void write() {
   }

   public void copy(WrapperPlayServerEnterCombatEvent wrapper) {
   }
}
