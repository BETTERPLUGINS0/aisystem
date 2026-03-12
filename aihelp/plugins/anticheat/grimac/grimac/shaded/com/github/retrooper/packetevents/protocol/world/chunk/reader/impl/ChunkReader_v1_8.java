package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.NetworkChunkData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.ShortArray3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.BitSet;

public class ChunkReader_v1_8 implements ChunkReader {
   public BaseChunk[] read(DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize, int arrayLength, PacketWrapper<?> wrapper) {
      byte[] data = wrapper.readByteArrayOfSize(arrayLength);
      Chunk_v1_8[] chunks = new Chunk_v1_8[16];
      int pos = 0;
      int expected = fullChunk ? 256 : 0;
      boolean sky = false;
      ShortBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();

      int pass;
      for(pass = 0; pass < 4; ++pass) {
         for(int ind = 0; ind < 16; ++ind) {
            if (chunkMask.get(ind)) {
               if (pass == 0) {
                  expected += 10240;
               }

               if (pass == 1) {
                  chunks[ind] = new Chunk_v1_8(sky || hasBlockLight);
                  ShortArray3d blocks = chunks[ind].getBlocks();
                  buf.position(pos / 2);
                  buf.get(blocks.getData(), 0, blocks.getData().length);
                  pos += blocks.getData().length * 2;
               }

               NibbleArray3d skylight;
               if (pass == 2) {
                  skylight = chunks[ind].getBlockLight();
                  System.arraycopy(data, pos, skylight.getData(), 0, skylight.getData().length);
                  pos += skylight.getData().length;
               }

               if (pass == 3 && (sky || hasBlockLight)) {
                  skylight = chunks[ind].getSkyLight();
                  System.arraycopy(data, pos, skylight.getData(), 0, skylight.getData().length);
                  pos += skylight.getData().length;
               }
            }
         }

         if (pass == 0 && data.length > expected) {
            sky = hasSkyLight;
         }
      }

      pass = ByteBufHelper.readerIndex(wrapper.buffer);
      ByteBufHelper.readerIndex(wrapper.buffer, pass - (arrayLength - pos));
      return chunks;
   }

   public static NetworkChunkData chunksToData(Chunk_v1_8[] chunks, byte[] biomes) {
      int chunkMask = 0;
      boolean fullChunk = biomes != null;
      boolean sky = false;
      int length = fullChunk ? biomes.length : 0;
      byte[] data = null;
      int pos = 0;
      ShortBuffer buf = null;

      for(int pass = 0; pass < 4; ++pass) {
         for(int ind = 0; ind < chunks.length; ++ind) {
            Chunk_v1_8 chunk = chunks[ind];
            if (chunk != null && (!fullChunk || !chunk.isEmpty())) {
               if (pass == 0) {
                  chunkMask |= 1 << ind;
                  length += chunk.getBlocks().getData().length * 2;
                  length += chunk.getBlockLight().getData().length;
                  if (chunk.getSkyLight() != null) {
                     length += chunk.getSkyLight().getData().length;
                  }
               }

               if (pass == 1) {
                  short[] blocks = chunk.getBlocks().getData();
                  buf.position(pos / 2);
                  buf.put(blocks, 0, blocks.length);
                  pos += blocks.length * 2;
               }

               byte[] skylight;
               if (pass == 2) {
                  skylight = chunk.getBlockLight().getData();
                  System.arraycopy(skylight, 0, data, pos, skylight.length);
                  pos += skylight.length;
               }

               if (pass == 3 && chunk.getSkyLight() != null) {
                  skylight = chunk.getSkyLight().getData();
                  System.arraycopy(skylight, 0, data, pos, skylight.length);
                  pos += skylight.length;
                  sky = true;
               }
            }
         }

         if (pass == 0) {
            data = new byte[length];
            buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
         }
      }

      if (fullChunk) {
         System.arraycopy(biomes, 0, data, pos, biomes.length);
         int var10000 = pos + biomes.length;
      }

      return new NetworkChunkData(chunkMask, fullChunk, sky, data);
   }
}
