package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.status.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusClientPing extends PacketWrapper<WrapperStatusClientPing> {
   private long time;

   public WrapperStatusClientPing(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperStatusClientPing(long time) {
      super(PacketType.Status.Client.PING.getId(), ClientVersion.UNKNOWN);
      this.time = time;
   }

   public void read() {
      this.time = this.readLong();
   }

   public void write() {
      this.writeLong(this.time);
   }

   public void copy(WrapperStatusClientPing wrapper) {
      this.time = wrapper.time;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }
}
