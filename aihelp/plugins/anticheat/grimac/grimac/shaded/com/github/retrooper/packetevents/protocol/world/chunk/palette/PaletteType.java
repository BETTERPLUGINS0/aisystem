package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInputWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamOutputWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public enum PaletteType {
   BIOME(3, 3, false, 2),
   CHUNK(4, 8, true, 4);

   private final int maxBitsPerEntryForList;
   private final int maxBitsPerEntryForMap;
   private final boolean forceMaxListPaletteSize;
   private final int bitShift;
   private final int storageSize;

   private PaletteType(int maxBitsPerEntryForList, int maxBitsPerEntryForMap, boolean forceMaxListPaletteSize, int bitShift) {
      this.maxBitsPerEntryForList = maxBitsPerEntryForList;
      this.maxBitsPerEntryForMap = maxBitsPerEntryForMap;
      this.forceMaxListPaletteSize = forceMaxListPaletteSize;
      this.bitShift = bitShift;
      this.storageSize = 1 << bitShift * 3;
   }

   public static void write(PacketWrapper<?> wrapper, DataPalette palette) {
      boolean lengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
      DataPalette.write(new NetStreamOutputWrapper(wrapper), palette, lengthPrefix);
   }

   public DataPalette read(PacketWrapper<?> wrapper) {
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_16)) {
         return DataPalette.readLegacy(new NetStreamInputWrapper(wrapper));
      } else {
         boolean allowSingletonPalette = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_18);
         boolean lengthPrefix = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5);
         return DataPalette.read(new NetStreamInputWrapper(wrapper), this, allowSingletonPalette, lengthPrefix);
      }
   }

   public DataPalette create() {
      int bitsPerEntry = this.getMaxBitsPerEntryForList();
      Palette palette = new ListPalette(bitsPerEntry);
      BitStorage storage = new BitStorage(bitsPerEntry, this.getStorageSize());
      return new DataPalette(palette, storage, this);
   }

   public int getMaxBitsPerEntryForList() {
      return this.maxBitsPerEntryForList;
   }

   public int getMaxBitsPerEntryForMap() {
      return this.maxBitsPerEntryForMap;
   }

   public boolean isForceMaxListPaletteSize() {
      return this.forceMaxListPaletteSize;
   }

   public int getBitShift() {
      return this.bitShift;
   }

   public int getStorageSize() {
      return this.storageSize;
   }

   /** @deprecated */
   @Deprecated
   public int getMaxBitsPerEntry() {
      return this.maxBitsPerEntryForMap;
   }

   /** @deprecated */
   @Deprecated
   public int getMinBitsPerEntry() {
      return this.maxBitsPerEntryForList;
   }

   // $FF: synthetic method
   private static PaletteType[] $values() {
      return new PaletteType[]{BIOME, CHUNK};
   }
}
