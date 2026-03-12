package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerChunkBatchEnd extends PacketWrapper<WrapperPlayServerChunkBatchEnd> {
   private int batchSize;

   public WrapperPlayServerChunkBatchEnd(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerChunkBatchEnd(int batchSize) {
      super((PacketTypeCommon)PacketType.Play.Server.CHUNK_BATCH_END);
      this.batchSize = batchSize;
   }

   public void read() {
      this.batchSize = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.batchSize);
   }

   public void copy(WrapperPlayServerChunkBatchEnd wrapper) {
      this.batchSize = wrapper.batchSize;
   }

   public int getBatchSize() {
      return this.batchSize;
   }

   public void setBatchSize(int batchSize) {
      this.batchSize = batchSize;
   }
}
