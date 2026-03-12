package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDebugPong extends PacketWrapper<WrapperPlayServerDebugPong> {
   private long timestamp;

   public WrapperPlayServerDebugPong(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDebugPong(long timestamp) {
      super((PacketTypeCommon)PacketType.Play.Server.DEBUG_PONG);
      this.timestamp = timestamp;
   }

   public void read() {
      this.timestamp = this.readLong();
   }

   public void write() {
      this.writeLong(this.timestamp);
   }

   public void copy(WrapperPlayServerDebugPong wrapper) {
      this.timestamp = wrapper.timestamp;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }
}
