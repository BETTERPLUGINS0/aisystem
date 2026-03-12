package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.ByteArray3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7.Chunk_v1_7;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.BitSet;

public class ChunkReader_v1_7 implements ChunkReader {
   public BaseChunk[] read(DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize, int arrayLength, PacketWrapper<?> wrapper) {
      byte[] data = wrapper.readByteArrayOfSize(arrayLength);
      Chunk_v1_7[] chunks = new Chunk_v1_7[16];
      int pos = 0;
      int expected = 0;
      boolean sky = false;

      int pass;
      for(pass = 0; pass < 5; ++pass) {
         for(int ind = 0; ind < 16; ++ind) {
            if (chunkMask.get(ind)) {
               if (pass == 0) {
                  expected += 10240;
                  if (secondaryChunkMask.get(ind)) {
                     expected += 2048;
                  }
               }

               if (pass == 1) {
                  chunks[ind] = new Chunk_v1_7(sky, secondaryChunkMask.get(ind));
                  ByteArray3d blocks = chunks[ind].getBlocks();
                  System.arraycopy(data, pos, blocks.getData(), 0, blocks.getData().length);
                  pos += blocks.getData().length;
               }

               NibbleArray3d skylight;
               if (pass == 2) {
                  skylight = chunks[ind].getMetadata();
                  System.arraycopy(data, pos, skylight.getData(), 0, skylight.getData().length);
                  pos += skylight.getData().length;
               }

               if (pass == 3) {
                  skylight = chunks[ind].getBlockLight();
                  System.arraycopy(data, pos, skylight.getData(), 0, skylight.getData().length);
                  pos += skylight.getData().length;
               }

               if (pass == 4 && sky) {
                  skylight = chunks[ind].getSkyLight();
                  System.arraycopy(data, pos, skylight.getData(), 0, skylight.getData().length);
                  pos += skylight.getData().length;
               }
            }
         }

         if (pass == 0 && data.length >= expected) {
            sky = true;
         }
      }

      pass = ByteBufHelper.readerIndex(wrapper.buffer);
      ByteBufHelper.readerIndex(wrapper.buffer, pass - (arrayLength - pos));
      return chunks;
   }

   public static NetworkChunkData chunksToData(Chunk_v1_7[] chunks, byte[] biomes) {
      int chunkMask = 0;
      int extendedChunkMask = 0;
      boolean fullChunk = biomes != null;
      boolean sky = false;
      int length = fullChunk ? biomes.length : 0;
      byte[] data = null;
      int pos = 0;

      for(int pass = 0; pass < 6; ++pass) {
         for(int ind = 0; ind < chunks.length; ++ind) {
            Chunk_v1_7 chunk = chunks[ind];
            if (chunk != null && (!fullChunk || !chunk.isEmpty())) {
               if (pass == 0) {
                  chunkMask |= 1 << ind;
                  if (chunk.getExtendedBlocks() != null) {
                     extendedChunkMask |= 1 << ind;
                  }

                  length += chunk.getBlocks().getData().length;
                  length += chunk.getMetadata().getData().length;
                  length += chunk.getBlockLight().getData().length;
                  if (chunk.getSkyLight() != null) {
                     length += chunk.getSkyLight().getData().length;
                  }

                  if (chunk.getExtendedBlocks() != null) {
                     length += chunk.getExtendedBlocks().getData().length;
                  }
               }

               if (pass == 1) {
                  ByteArray3d blocks = chunk.getBlocks();
                  System.arraycopy(blocks.getData(), 0, data, pos, blocks.getData().length);
                  pos += blocks.getData().length;
               }

               byte[] extended;
               if (pass == 2) {
                  extended = chunk.getMetadata().getData();
                  System.arraycopy(extended, 0, data, pos, extended.length);
                  pos += extended.length;
               }

               if (pass == 3) {
                  extended = chunk.getBlockLight().getData();
                  System.arraycopy(extended, 0, data, pos, extended.length);
                  pos += extended.length;
               }

               if (pass == 4 && chunk.getSkyLight() != null) {
                  extended = chunk.getSkyLight().getData();
                  System.arraycopy(extended, 0, data, pos, extended.length);
                  pos += extended.length;
                  sky = true;
               }

               if (pass == 5 && chunk.getExtendedBlocks() != null) {
                  extended = chunk.getExtendedBlocks().getData();
                  System.arraycopy(extended, 0, data, pos, extended.length);
                  pos += extended.length;
               }
            }
         }

         if (pass == 0) {
            data = new byte[length];
         }
      }

      if (fullChunk) {
         System.arraycopy(biomes, 0, data, pos, biomes.length);
         int var10000 = pos + biomes.length;
      }

      return new NetworkChunkData(chunkMask, extendedChunkMask, fullChunk, sky, data);
   }
}
