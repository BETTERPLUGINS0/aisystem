package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_7.Chunk_v1_7;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_8.Chunk_v1_8;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.ListPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.LegacyFlexibleStorage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public interface BaseChunk {
   int getBlockId(int x, int y, int z);

   default WrappedBlockState get(ClientVersion version, int x, int y, int z) {
      return this.get(version, x, y, z, true);
   }

   default WrappedBlockState get(ClientVersion version, int x, int y, int z, boolean clone) {
      return WrappedBlockState.getByGlobalId(version, this.getBlockId(x, y, z), clone);
   }

   default WrappedBlockState get(int x, int y, int z) {
      return this.get(x, y, z, true);
   }

   default WrappedBlockState get(int x, int y, int z, boolean clone) {
      return this.get(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), x, y, z, clone);
   }

   default void set(int x, int y, int z, WrappedBlockState state) {
      this.set(x, y, z, state.getGlobalId());
   }

   void set(int x, int y, int z, int combinedID);

   default void set(ClientVersion version, int x, int y, int z, int combinedID) {
      this.set(x, y, z, combinedID);
   }

   boolean isEmpty();

   static BaseChunk create() {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      if (version.isNewerThanOrEquals(ServerVersion.V_1_18)) {
         return new Chunk_v1_18();
      } else if (version.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         return new Chunk_v1_9(0, PaletteType.CHUNK.create());
      } else if (version.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         return new Chunk_v1_9(0, new DataPalette(new ListPalette(4), new LegacyFlexibleStorage(4, 4096), PaletteType.CHUNK));
      } else {
         return (BaseChunk)(version.isNewerThanOrEquals(ServerVersion.V_1_8) ? new Chunk_v1_8(new ShortArray3d(4096), (NibbleArray3d)null, (NibbleArray3d)null) : new Chunk_v1_7(false, true));
      }
   }
}
