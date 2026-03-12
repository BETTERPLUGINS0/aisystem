package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.BitSet;

@ApiStatus.Internal
public class ChunkBitMask {
   private ChunkBitMask() {
   }

   public static long[] readBitSetLongs(PacketWrapper<?> packet) {
      if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
         return packet.readLongArray();
      } else {
         return packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? new long[]{(long)packet.readVarInt()} : new long[]{(long)packet.readUnsignedShort()};
      }
   }

   public static BitSet readChunkMask(PacketWrapper<?> packet) {
      return BitSet.valueOf(readBitSetLongs(packet));
   }

   public static void writeChunkMask(PacketWrapper<?> packet, BitSet chunkMask) {
      long[] longArray = chunkMask.toLongArray();
      if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
         packet.writeLongArray(longArray);
      } else if (packet.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         if (longArray.length > 0) {
            packet.writeVarInt((int)longArray[0]);
         } else {
            packet.writeVarInt(0);
         }
      } else if (longArray.length > 0) {
         packet.writeShort((int)longArray[0]);
      } else {
         packet.writeShort(0);
      }

   }
}
