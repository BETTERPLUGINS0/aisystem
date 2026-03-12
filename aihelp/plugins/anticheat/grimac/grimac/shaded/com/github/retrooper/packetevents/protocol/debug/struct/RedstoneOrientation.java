package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RedstoneOrientation {
   private final int index;

   public RedstoneOrientation(int index) {
      this.index = index;
   }

   public static RedstoneOrientation read(PacketWrapper<?> wrapper) {
      int index = wrapper.readVarInt();
      return new RedstoneOrientation(index);
   }

   public static void write(PacketWrapper<?> wrapper, RedstoneOrientation orientation) {
      wrapper.writeVarInt(orientation.index);
   }

   public int getIndex() {
      return this.index;
   }
}
