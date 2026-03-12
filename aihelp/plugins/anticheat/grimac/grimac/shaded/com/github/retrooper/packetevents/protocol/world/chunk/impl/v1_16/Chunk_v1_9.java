package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInputWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamOutputWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.NibbleArray3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class Chunk_v1_9 implements BaseChunk {
   private static final int AIR = 0;
   private static final int LIGHT_NIBBLES_SIZE = 2048;
   private int blockCount;
   private final DataPalette dataPalette;
   @Nullable
   private NibbleArray3d blockLight;
   @Nullable
   private NibbleArray3d skyLight;

   public Chunk_v1_9(int blockCount, DataPalette dataPalette) {
      this(blockCount, dataPalette, (NibbleArray3d)null, (NibbleArray3d)null);
   }

   public Chunk_v1_9(int blockCount, DataPalette dataPalette, @Nullable NibbleArray3d blockLight, @Nullable NibbleArray3d skyLight) {
      this.blockCount = blockCount;
      this.dataPalette = dataPalette;
      this.blockLight = blockLight;
      this.skyLight = skyLight;
   }

   /** @deprecated */
   @Deprecated
   public Chunk_v1_9(NetStreamInput in, boolean hasBlockLight, boolean hasSkyLight) {
      this(in, hasBlockLight, hasSkyLight, PacketEvents.getAPI().getServerManager().getVersion());
   }

   /** @deprecated */
   @Deprecated
   private Chunk_v1_9(NetStreamInput in, boolean hasBlockLight, boolean hasSkyLight, ServerVersion version) {
      this.blockCount = version.isNewerThanOrEquals(ServerVersion.V_1_14) ? in.readShort() : Integer.MAX_VALUE;
      this.dataPalette = version.isNewerThanOrEquals(ServerVersion.V_1_16) ? DataPalette.read(in, PaletteType.CHUNK, false) : DataPalette.readLegacy(in);
      this.blockLight = hasBlockLight ? new NibbleArray3d(in, 2048) : null;
      this.skyLight = hasSkyLight ? new NibbleArray3d(in, 2048) : null;
   }

   public static Chunk_v1_9 read(PacketWrapper<?> wrapper, boolean hasBlockLight, boolean hasSkyLight) {
      NetStreamInputWrapper legacyInput = new NetStreamInputWrapper(wrapper);
      return new Chunk_v1_9(legacyInput, hasBlockLight, hasSkyLight, wrapper.getServerVersion());
   }

   public static void write(PacketWrapper<?> wrapper, Chunk_v1_9 chunk) {
      NetStreamOutputWrapper legacyOutput = new NetStreamOutputWrapper(wrapper);
      write(legacyOutput, chunk, wrapper.getServerVersion());
   }

   /** @deprecated */
   @Deprecated
   public static void write(NetStreamOutput out, Chunk_v1_9 chunk) {
      write(out, chunk, PacketEvents.getAPI().getServerManager().getVersion());
   }

   /** @deprecated */
   @Deprecated
   private static void write(NetStreamOutput out, Chunk_v1_9 chunk, ServerVersion version) {
      if (version.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         out.writeShort(chunk.blockCount);
      }

      DataPalette.write(out, chunk.dataPalette);
      if (chunk.blockLight != null) {
         out.writeBytes(chunk.blockLight.getData());
      }

      if (chunk.skyLight != null) {
         out.writeBytes(chunk.skyLight.getData());
      }

   }

   public int getBlockId(int x, int y, int z) {
      return this.dataPalette.get(x, y, z);
   }

   public void set(int x, int y, int z, int state) {
      int curr = this.dataPalette.set(x, y, z, state);
      if (this.blockCount != Integer.MAX_VALUE) {
         if (state != 0 && curr == 0) {
            ++this.blockCount;
         } else if (state == 0 && curr != 0) {
            --this.blockCount;
         }

      }
   }

   public boolean isEmpty() {
      if (this.blockCount != Integer.MAX_VALUE) {
         return this.blockCount == 0;
      } else {
         for(int x = 0; x < 16; ++x) {
            for(int y = 0; y < 16; ++y) {
               for(int z = 0; z < 16; ++z) {
                  if (this.dataPalette.get(x, y, z) != 0) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   @Nullable
   public NibbleArray3d getSkyLight() {
      return this.skyLight;
   }

   public void setSkyLight(@Nullable NibbleArray3d skyLight) {
      this.skyLight = skyLight;
   }

   @Nullable
   public NibbleArray3d getBlockLight() {
      return this.blockLight;
   }

   public void setBlockLight(@Nullable NibbleArray3d blockLight) {
      this.blockLight = blockLight;
   }
}
