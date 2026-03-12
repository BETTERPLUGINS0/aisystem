package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public abstract class WrapperCommonServerClearDialog<T extends WrapperCommonServerClearDialog<T>> extends PacketWrapper<T> {
   public WrapperCommonServerClearDialog(PacketSendEvent event) {
      super(event);
   }

   public WrapperCommonServerClearDialog(PacketTypeCommon packetType) {
      super(packetType);
   }
}
