package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class SingletonPalette implements Palette {
   private final int state;

   /** @deprecated */
   @Deprecated
   public SingletonPalette(NetStreamInput in) {
      this(in.readVarInt());
   }

   public SingletonPalette(PacketWrapper<?> wrapper) {
      this(wrapper.readVarInt());
   }

   public SingletonPalette(int state) {
      this.state = state;
   }

   public int size() {
      return 1;
   }

   public int stateToId(int state) {
      return this.state == state ? 0 : -1;
   }

   public int idToState(int id) {
      return id == 0 ? this.state : 0;
   }

   public int getBits() {
      return 0;
   }
}
