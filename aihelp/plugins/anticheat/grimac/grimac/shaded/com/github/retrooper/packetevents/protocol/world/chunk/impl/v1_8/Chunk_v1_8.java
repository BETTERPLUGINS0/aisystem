package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.ShortArray3d;

public class Chunk_v1_8 implements BaseChunk {
   private ShortArray3d blocks;
   private NibbleArray3d blocklight;
   private NibbleArray3d skylight;

   public Chunk_v1_8(boolean skylight) {
      this(new ShortArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
   }

   public Chunk_v1_8(ShortArray3d blocks, NibbleArray3d blocklight, NibbleArray3d skylight) {
      this.blocks = blocks;
      this.blocklight = blocklight;
      this.skylight = skylight;
   }

   public ShortArray3d getBlocks() {
      return this.blocks;
   }

   public NibbleArray3d getBlockLight() {
      return this.blocklight;
   }

   public NibbleArray3d getSkyLight() {
      return this.skylight;
   }

   public int getBlockId(int x, int y, int z) {
      return this.blocks.get(x, y, z);
   }

   public void set(int x, int y, int z, int combinedID) {
      this.blocks.set(x, y, z, combinedID);
   }

   public boolean isEmpty() {
      short[] var1 = this.blocks.getData();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         short block = var1[var3];
         if (block != 0) {
            return false;
         }
      }

      return true;
   }
}
