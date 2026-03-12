package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.BaseStorage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.BitSet;

public class ChunkReader_v1_18 implements ChunkReader {
   @ApiStatus.Internal
   public static int getMojangZeroByteSuffixLength(BaseChunk[] chunks) {
      int mojangPleaseFixThisZeroByteSuffixLength = 0;
      BaseChunk[] var2 = chunks;
      int var3 = chunks.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BaseChunk chunk = var2[var4];
         BaseStorage chunkStorage = ((Chunk_v1_18)chunk).getChunkData().storage;
         int chunkStorageLen = ByteBufHelper.getByteSize(chunkStorage != null ? chunkStorage.getData().length : 0);
         BaseStorage biomeStorage = ((Chunk_v1_18)chunk).getBiomeData().storage;
         int biomeStorageLen = ByteBufHelper.getByteSize(biomeStorage != null ? biomeStorage.getData().length : 0);
         mojangPleaseFixThisZeroByteSuffixLength += chunkStorageLen + biomeStorageLen;
      }

      return mojangPleaseFixThisZeroByteSuffixLength;
   }

   public BaseChunk[] read(DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize, int arrayLength, PacketWrapper<?> wrapper) {
      int ri = ByteBufHelper.readerIndex(wrapper.buffer);
      BaseChunk[] chunks = new BaseChunk[chunkSize];

      for(int i = 0; i < chunkSize; ++i) {
         chunks[i] = Chunk_v1_18.read(wrapper);
      }

      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_6) && wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) && ByteBufHelper.readerIndex(wrapper.buffer) - ri < arrayLength) {
         ByteBufHelper.skipBytes(wrapper.buffer, getMojangZeroByteSuffixLength(chunks));
      }

      return chunks;
   }
}
