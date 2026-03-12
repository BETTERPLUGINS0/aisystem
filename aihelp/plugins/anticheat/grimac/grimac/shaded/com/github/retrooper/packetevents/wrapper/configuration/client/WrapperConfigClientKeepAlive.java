package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigClientKeepAlive extends PacketWrapper<WrapperConfigClientKeepAlive> {
   private long id;

   public WrapperConfigClientKeepAlive(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientKeepAlive(long id) {
      super((PacketTypeCommon)PacketType.Configuration.Client.KEEP_ALIVE);
      this.id = id;
   }

   public void read() {
      this.id = this.readLong();
   }

   public void write() {
      this.writeLong(this.id);
   }

   public void copy(WrapperConfigClientKeepAlive wrapper) {
      this.id = wrapper.id;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }
}
