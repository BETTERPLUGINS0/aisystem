package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInputWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamOutputWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class Chunk_v1_18 implements BaseChunk {
   private static final int AIR = 0;
   private int blockCount;
   private final DataPalette chunkData;
   private final DataPalette biomeData;

   public Chunk_v1_18() {
      this.chunkData = PaletteType.CHUNK.create();
      this.biomeData = PaletteType.BIOME.create();
   }

   public Chunk_v1_18(int blockCount, DataPalette chunkData, DataPalette biomeData) {
      this.blockCount = blockCount;
      this.chunkData = chunkData;
      this.biomeData = biomeData;
   }

   public static Chunk_v1_18 read(PacketWrapper<?> wrapper) {
      boolean paletteLengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
      return read(new NetStreamInputWrapper(wrapper), paletteLengthPrefix);
   }

   /** @deprecated */
   @Deprecated
   public static Chunk_v1_18 read(NetStreamInput in) {
      return read(in, true);
   }

   /** @deprecated */
   @Deprecated
   public static Chunk_v1_18 read(NetStreamInput in, boolean paletteLengthPrefix) {
      int blockCount = in.readShort();
      DataPalette chunkPalette = DataPalette.read(in, PaletteType.CHUNK, true, paletteLengthPrefix);
      DataPalette biomePalette = DataPalette.read(in, PaletteType.BIOME, true, paletteLengthPrefix);
      return new Chunk_v1_18(blockCount, chunkPalette, biomePalette);
   }

   public static void write(PacketWrapper<?> wrapper, Chunk_v1_18 section) {
      boolean paletteLengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
      write(new NetStreamOutputWrapper(wrapper), section, paletteLengthPrefix);
   }

   /** @deprecated */
   @Deprecated
   public static void write(NetStreamOutput out, Chunk_v1_18 section) {
      write(out, section, true);
   }

   /** @deprecated */
   @Deprecated
   public static void write(NetStreamOutput out, Chunk_v1_18 section, boolean paletteLengthPrefix) {
      out.writeShort(section.blockCount);
      DataPalette.write(out, section.chunkData, paletteLengthPrefix);
      DataPalette.write(out, section.biomeData, paletteLengthPrefix);
   }

   public int getBlockId(int x, int y, int z) {
      return this.chunkData.get(x, y, z);
   }

   public void set(int x, int y, int z, int state) {
      int curr = this.chunkData.set(x, y, z, state);
      if (state != 0 && curr == 0) {
         ++this.blockCount;
      } else if (state == 0 && curr != 0) {
         --this.blockCount;
      }

   }

   public boolean isEmpty() {
      return this.blockCount == 0;
   }

   public int getBlockCount() {
      return this.blockCount;
   }

   public void setBlockCount(int blockCount) {
      this.blockCount = blockCount;
   }

   public DataPalette getChunkData() {
      return this.chunkData;
   }

   public DataPalette getBiomeData() {
      return this.biomeData;
   }
}
