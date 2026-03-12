package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.status.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusServerPong extends PacketWrapper<WrapperStatusServerPong> {
   private long time;

   public WrapperStatusServerPong(PacketSendEvent event) {
      super(event);
   }

   public WrapperStatusServerPong(long time) {
      super((PacketTypeCommon)PacketType.Status.Server.PONG);
      this.time = time;
   }

   public void read() {
      this.time = this.readLong();
   }

   public void write() {
      this.writeLong(this.time);
   }

   public void copy(WrapperStatusServerPong wrapper) {
      this.time = wrapper.time;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }
}
