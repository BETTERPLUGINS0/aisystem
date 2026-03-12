package ac.grim.grimac.utils.chunks;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;

public record Column(int x, int z, BaseChunk[] chunks, int transaction) {
   public Column(int x, int z, BaseChunk[] chunks, int transaction) {
      this.x = x;
      this.z = z;
      this.chunks = chunks;
      this.transaction = transaction;
   }

   public void mergeChunks(BaseChunk[] toMerge) {
      for(int i = 0; i < 16; ++i) {
         if (toMerge[i] != null) {
            this.chunks[i] = toMerge[i];
         }
      }

   }

   public int x() {
      return this.x;
   }

   public int z() {
      return this.z;
   }

   public BaseChunk[] chunks() {
      return this.chunks;
   }

   public int transaction() {
      return this.transaction;
   }
}
