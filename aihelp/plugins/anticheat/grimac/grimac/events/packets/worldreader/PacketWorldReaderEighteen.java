package ac.grim.grimac.events.packets.worldreader;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.HeightmapType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.reader.impl.ChunkReader_v1_18;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.BitSet;

public class PacketWorldReaderEighteen extends BasePacketWorldReader {
   private static final ChunkReader_v1_18 CHUNK_READER_V_1_18 = new ChunkReader_v1_18();
   private static final boolean PRE_1_21_5;

   public void handleMapChunk(GrimPlayer player, PacketSendEvent event) {
      PacketWrapper<?> wrapper = new PacketWrapper(event);
      int x = wrapper.readInt();
      int z = wrapper.readInt();
      if (PRE_1_21_5) {
         wrapper.readNBT();
      } else {
         wrapper.readMap(HeightmapType::read, PacketWrapper::readLongArray);
      }

      BaseChunk[] chunks = CHUNK_READER_V_1_18.read(DimensionTypes.OVERWORLD, (BitSet)null, (BitSet)null, true, false, false, event.getUser().getTotalWorldHeight() >> 4, wrapper.readVarInt(), wrapper);

      for(int i = 0; i < chunks.length; ++i) {
         Chunk_v1_18 chunk = (Chunk_v1_18)chunks[i];
         if (chunk != null) {
            chunks[i] = new Chunk_v1_18(chunk.getBlockCount(), chunk.getChunkData(), (DataPalette)null);
         }
      }

      this.addChunkToCache(event, player, chunks, true, x, z);
      event.setLastUsedWrapper((PacketWrapper)null);
   }

   static {
      PRE_1_21_5 = PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21_5);
   }
}
