package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.BaseStorage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.BitStorage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.LegacyFlexibleStorage;

public class DataPalette {
   public final PaletteType paletteType;
   public Palette palette;
   public BaseStorage storage;

   public DataPalette(Palette palette, BaseStorage storage, PaletteType paletteType) {
      this.palette = palette;
      this.storage = storage;
      this.paletteType = paletteType;
   }

   /** @deprecated */
   @Deprecated
   public static DataPalette createForChunk() {
      return PaletteType.CHUNK.create();
   }

   /** @deprecated */
   @Deprecated
   public static DataPalette createForBiome() {
      return PaletteType.BIOME.create();
   }

   /** @deprecated */
   @Deprecated
   public static DataPalette createEmpty(PaletteType paletteType) {
      return paletteType.create();
   }

   /** @deprecated */
   @Deprecated
   public static DataPalette read(NetStreamInput in, PaletteType paletteType) {
      return read(in, paletteType, true);
   }

   /** @deprecated */
   @Deprecated
   public static DataPalette read(NetStreamInput in, PaletteType paletteType, boolean allowSingletonPalette) {
      return read(in, paletteType, allowSingletonPalette, true);
   }

   /** @deprecated */
   @Deprecated
   public static DataPalette read(NetStreamInput in, PaletteType paletteType, boolean allowSingletonPalette, boolean lengthPrefix) {
      int bitsPerEntry = in.readByte();
      Palette palette = readPalette(paletteType, bitsPerEntry, in, allowSingletonPalette);
      BitStorage storage;
      if (!(palette instanceof SingletonPalette)) {
         long[] data = lengthPrefix ? in.readLongs(in.readVarInt()) : null;
         storage = new BitStorage(bitsPerEntry, paletteType.getStorageSize(), data);
         if (!lengthPrefix) {
            in.readLongs(storage.getData());
         }
      } else {
         if (lengthPrefix) {
            in.readLongs(in.readVarInt());
         }

         storage = null;
      }

      return new DataPalette(palette, storage, paletteType);
   }

   /** @deprecated */
   @Deprecated
   public static void write(NetStreamOutput out, DataPalette palette) {
      write(out, palette, true);
   }

   /** @deprecated */
   @Deprecated
   public static void write(NetStreamOutput out, DataPalette palette, boolean lengthPrefix) {
      if (palette.palette instanceof SingletonPalette) {
         out.writeByte(0);
         out.writeVarInt(palette.palette.idToState(0));
         if (lengthPrefix) {
            out.writeVarInt(0);
         }

      } else {
         out.writeByte(palette.storage.getBitsPerEntry());
         if (!(palette.palette instanceof GlobalPalette)) {
            int paletteLength = palette.palette.size();
            out.writeVarInt(paletteLength);

            for(int i = 0; i < paletteLength; ++i) {
               out.writeVarInt(palette.palette.idToState(i));
            }
         }

         long[] data = palette.storage.getData();
         if (lengthPrefix) {
            out.writeVarInt(data.length);
         }

         out.writeLongs(data);
      }
   }

   /** @deprecated */
   @Deprecated
   public static DataPalette readLegacy(NetStreamInput in) {
      int bitsPerEntry = Math.max(4, in.readByte() & 255);
      Palette palette = readPalette(PaletteType.CHUNK, bitsPerEntry, in, false);
      BaseStorage storage = new LegacyFlexibleStorage(bitsPerEntry, in.readLongs(in.readVarInt()));
      return new DataPalette(palette, storage, PaletteType.CHUNK);
   }

   public int get(int x, int y, int z) {
      if (this.storage != null) {
         int id = this.storage.get(index(this.paletteType, x, y, z));
         return this.palette.idToState(id);
      } else {
         return this.palette.idToState(0);
      }
   }

   public int set(int x, int y, int z, int state) {
      int id = this.palette.stateToId(state);
      if (id == -1) {
         this.resizeOneUp();
         id = this.palette.stateToId(state);
      }

      if (this.storage != null) {
         int index = index(this.paletteType, x, y, z);
         int curr = this.storage.get(index);
         this.storage.set(index, id);
         return curr;
      } else {
         return state;
      }
   }

   /** @deprecated */
   @Deprecated
   private static Palette readPalette(PaletteType paletteType, int bitsPerEntry, NetStreamInput in, boolean allowSingletonPalette) {
      if (bitsPerEntry == 0 && allowSingletonPalette) {
         return new SingletonPalette(in);
      } else if (bitsPerEntry <= paletteType.getMaxBitsPerEntryForList()) {
         int bits = paletteType.isForceMaxListPaletteSize() ? paletteType.getMaxBitsPerEntryForList() : bitsPerEntry;
         return new ListPalette(bits, in);
      } else {
         return (Palette)(bitsPerEntry <= paletteType.getMaxBitsPerEntryForMap() ? new MapPalette(bitsPerEntry, in) : GlobalPalette.INSTANCE);
      }
   }

   private void resizeOneUp() {
      Palette oldPalette = this.palette;
      BaseStorage oldData = this.storage;
      int prevBitsPerEntry = oldData != null ? oldData.getBitsPerEntry() : 0;
      this.palette = createPalette(prevBitsPerEntry + 1, this.paletteType);
      this.storage = new BitStorage(this.palette.getBits(), this.paletteType.getStorageSize());
      if (oldData != null) {
         int i = 0;

         for(int len = this.paletteType.getStorageSize(); i < len; ++i) {
            this.storage.set(i, this.palette.stateToId(oldPalette.idToState(oldData.get(i))));
         }
      } else {
         this.palette.stateToId(oldPalette.idToState(0));
      }

   }

   private static Palette createPalette(int bitsPerEntry, PaletteType paletteType) {
      if (bitsPerEntry <= paletteType.getMaxBitsPerEntryForList()) {
         int bits = paletteType.isForceMaxListPaletteSize() ? paletteType.getMaxBitsPerEntryForList() : bitsPerEntry;
         return new ListPalette(bits);
      } else {
         return (Palette)(bitsPerEntry <= paletteType.getMaxBitsPerEntryForMap() ? new MapPalette(bitsPerEntry) : GlobalPalette.INSTANCE);
      }
   }

   private static int index(PaletteType paletteType, int x, int y, int z) {
      return (y << paletteType.getBitShift() | z) << paletteType.getBitShift() | x;
   }
}
