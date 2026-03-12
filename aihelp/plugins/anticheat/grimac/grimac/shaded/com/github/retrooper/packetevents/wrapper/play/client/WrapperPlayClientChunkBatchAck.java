package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChunkBatchAck extends PacketWrapper<WrapperPlayClientChunkBatchAck> {
   private float desiredChunksPerTick;

   public WrapperPlayClientChunkBatchAck(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientChunkBatchAck(float desiredChunksPerTick) {
      super((PacketTypeCommon)PacketType.Play.Client.CHUNK_BATCH_ACK);
      this.desiredChunksPerTick = desiredChunksPerTick;
   }

   public void read() {
      this.desiredChunksPerTick = this.readFloat();
   }

   public void write() {
      this.writeFloat(this.desiredChunksPerTick);
   }

   public void copy(WrapperPlayClientChunkBatchAck wrapper) {
      this.desiredChunksPerTick = wrapper.desiredChunksPerTick;
   }

   public float getDesiredChunksPerTick() {
      return this.desiredChunksPerTick;
   }

   public void setDesiredChunksPerTick(float desiredChunksPerTick) {
      this.desiredChunksPerTick = desiredChunksPerTick;
   }
}
