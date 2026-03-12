package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigClientPong extends PacketWrapper<WrapperConfigClientPong> {
   private int id;

   public WrapperConfigClientPong(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientPong(int id) {
      super((PacketTypeCommon)PacketType.Configuration.Client.PONG);
      this.id = id;
   }

   public void read() {
      this.id = this.readInt();
   }

   public void write() {
      this.writeInt(this.id);
   }

   public void copy(WrapperConfigClientPong wrapper) {
      this.id = wrapper.id;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }
}
