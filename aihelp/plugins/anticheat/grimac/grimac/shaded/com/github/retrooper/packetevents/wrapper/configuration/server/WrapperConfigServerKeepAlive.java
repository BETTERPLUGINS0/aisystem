package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerKeepAlive extends PacketWrapper<WrapperConfigServerKeepAlive> {
   private long id;

   public WrapperConfigServerKeepAlive(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerKeepAlive(long id) {
      super((PacketTypeCommon)PacketType.Configuration.Server.KEEP_ALIVE);
      this.id = id;
   }

   public void read() {
      this.id = this.readLong();
   }

   public void write() {
      this.writeLong(this.id);
   }

   public void copy(WrapperConfigServerKeepAlive wrapper) {
      this.id = wrapper.id;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }
}
