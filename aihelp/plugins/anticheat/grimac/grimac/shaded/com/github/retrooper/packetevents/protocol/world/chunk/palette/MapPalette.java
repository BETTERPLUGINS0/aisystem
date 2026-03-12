package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.HashMap;

public class MapPalette implements Palette {
   private final int bits;
   private final int[] idToState;
   private final HashMap<Object, Integer> stateToId;
   private int nextId;

   public MapPalette(int bitsPerEntry) {
      this.stateToId = new HashMap();
      this.nextId = 0;
      this.bits = bitsPerEntry;
      this.idToState = new int[1 << bitsPerEntry];
   }

   /** @deprecated */
   @Deprecated
   public MapPalette(int bitsPerEntry, NetStreamInput in) {
      this(bitsPerEntry);
      int paletteLength = in.readVarInt();

      for(int i = 0; i < paletteLength; ++i) {
         int state = in.readVarInt();
         this.idToState[i] = state;
         this.stateToId.putIfAbsent(state, i);
      }

      this.nextId = paletteLength;
   }

   public MapPalette(int bitsPerEntry, PacketWrapper<?> wrapper) {
      this(bitsPerEntry);
      int paletteLength = wrapper.readVarInt();

      for(int i = 0; i < paletteLength; ++i) {
         int state = wrapper.readVarInt();
         this.idToState[i] = state;
         this.stateToId.putIfAbsent(state, i);
      }

      this.nextId = paletteLength;
   }

   public int size() {
      return this.nextId;
   }

   public int stateToId(int state) {
      Integer id = (Integer)this.stateToId.get(state);
      if (id == null && this.size() < this.idToState.length) {
         id = this.nextId++;
         this.idToState[id] = state;
         this.stateToId.put(state, id);
      }

      return id != null ? id : -1;
   }

   public int idToState(int id) {
      return id >= 0 && id < this.size() ? this.idToState[id] : 0;
   }

   public int getBits() {
      return this.bits;
   }
}
