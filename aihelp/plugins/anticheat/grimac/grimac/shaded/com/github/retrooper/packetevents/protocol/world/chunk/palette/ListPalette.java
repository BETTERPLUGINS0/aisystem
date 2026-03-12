package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Arrays;

public class ListPalette implements Palette {
   private final int bits;
   private final int[] data;
   private int nextId;

   public ListPalette(int bitsPerEntry) {
      this.bits = bitsPerEntry;
      this.data = new int[1 << bitsPerEntry];
      this.nextId = 0;
   }

   /** @deprecated */
   @Deprecated
   public ListPalette(int bitsPerEntry, NetStreamInput in) {
      this(bitsPerEntry);
      int paletteLength = in.readVarInt();

      for(int i = 0; i < paletteLength; ++i) {
         this.data[i] = in.readVarInt();
      }

      this.nextId = paletteLength;
   }

   public ListPalette(int bitsPerEntry, PacketWrapper<?> wrapper) {
      this(bitsPerEntry);
      int paletteLength = wrapper.readVarInt();

      for(int i = 0; i < paletteLength; ++i) {
         this.data[i] = wrapper.readVarInt();
      }

      this.nextId = paletteLength;
   }

   public ListPalette(int bitsPerEntry, int[] data) {
      this.bits = bitsPerEntry;
      int expectedSize = 1 << this.bits;
      if (data.length > expectedSize) {
         throw new IllegalArgumentException("Data length exceeds the max size the bits can hold");
      } else {
         this.data = Arrays.copyOf(data, expectedSize);
         this.nextId = data.length;
      }
   }

   public int size() {
      return this.nextId;
   }

   public int stateToId(int state) {
      int id = -1;

      for(int i = 0; i < this.nextId; ++i) {
         if (this.data[i] == state) {
            id = i;
            break;
         }
      }

      if (id == -1 && this.size() < this.data.length) {
         id = this.nextId++;
         this.data[id] = state;
      }

      return id;
   }

   public int idToState(int id) {
      return id >= 0 && id < this.size() ? this.data[id] : 0;
   }

   public int getBits() {
      return this.bits;
   }
}
