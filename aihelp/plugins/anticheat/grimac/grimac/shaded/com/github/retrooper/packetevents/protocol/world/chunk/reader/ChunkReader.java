package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Dimension;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.io.ByteArrayInputStream;
import java.util.BitSet;

public interface ChunkReader {
   /** @deprecated */
   @Deprecated
   default BaseChunk[] read(Dimension dimension, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize, byte[] data, NetStreamInput dataIn) {
      DimensionType dimensionType = dimension.asDimensionType((User)null, (ClientVersion)null);
      PacketWrapper wrapper = PacketWrapper.createUniversalPacketWrapper(UnpooledByteBufAllocationHelper.wrappedBuffer(data));

      BaseChunk[] var12;
      try {
         var12 = this.read(dimensionType, chunkMask, secondaryChunkMask, fullChunk, hasBlockLight, hasSkyLight, chunkSize, data.length, wrapper);
      } finally {
         ByteBufHelper.release(wrapper.buffer);
      }

      return var12;
   }

   /** @deprecated */
   @Deprecated
   default BaseChunk[] read(DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize, byte[] data, NetStreamInput dataIn) {
      Dimension dimension = Dimension.fromDimensionType(dimensionType, (User)null, (ClientVersion)null);
      return this.read(dimension, chunkMask, secondaryChunkMask, fullChunk, hasBlockLight, hasSkyLight, chunkSize, data, dataIn);
   }

   default BaseChunk[] read(DimensionType dimensionType, BitSet chunkMask, BitSet secondaryChunkMask, boolean fullChunk, boolean hasBlockLight, boolean hasSkyLight, int chunkSize, int arrayLength, PacketWrapper<?> wrapper) {
      byte[] data = wrapper.readByteArrayOfSize(arrayLength);
      NetStreamInput dataIn = new NetStreamInput(new ByteArrayInputStream(data));
      return this.read(dimensionType, chunkMask, secondaryChunkMask, fullChunk, hasBlockLight, hasSkyLight, chunkSize, data, dataIn);
   }
}
