package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette;

public class GlobalPalette implements Palette {
   public static final int BITS_PER_ENTRY = 15;
   public static final GlobalPalette INSTANCE = new GlobalPalette();

   public int size() {
      return Integer.MAX_VALUE;
   }

   public int stateToId(int state) {
      return state;
   }

   public int idToState(int id) {
      return id;
   }

   public int getBits() {
      return 15;
   }
}
