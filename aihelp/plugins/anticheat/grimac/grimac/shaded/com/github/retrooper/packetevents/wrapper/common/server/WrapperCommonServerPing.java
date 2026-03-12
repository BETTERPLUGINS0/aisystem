package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperCommonServerPing<T extends WrapperCommonServerPing<T>> extends PacketWrapper<T> {
   private int id;

   public WrapperCommonServerPing(PacketSendEvent event) {
      super(event);
   }

   public WrapperCommonServerPing(PacketTypeCommon packetType, int id) {
      super(packetType);
      this.id = id;
   }

   public void read() {
      this.id = this.readInt();
   }

   public void write() {
      this.writeInt(this.id);
   }

   public void copy(T wrapper) {
      this.id = wrapper.getId();
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }
}
