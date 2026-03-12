package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientDebugPing extends PacketWrapper<WrapperPlayClientDebugPing> {
   private long timestamp;

   public WrapperPlayClientDebugPing(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientDebugPing(long timestamp) {
      super((PacketTypeCommon)PacketType.Play.Client.DEBUG_PING);
      this.timestamp = timestamp;
   }

   public void read() {
      this.timestamp = this.readLong();
   }

   public void write() {
      this.writeLong(this.timestamp);
   }

   public void copy(WrapperPlayClientDebugPing wrapper) {
      this.timestamp = wrapper.timestamp;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }
}
