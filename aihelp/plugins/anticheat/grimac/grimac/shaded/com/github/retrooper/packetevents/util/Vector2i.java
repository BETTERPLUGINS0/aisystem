package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Vector2i {
   private final int x;
   private final int z;

   public Vector2i(int x, int z) {
      this.x = x;
      this.z = z;
   }

   public static Vector2i read(PacketWrapper<?> wrapper) {
      return fromLong(wrapper.readLong());
   }

   public static void write(PacketWrapper<?> wrapper, Vector2i vec) {
      wrapper.writeLong(vec.asLong());
   }

   public static Vector2i fromLong(long l) {
      return new Vector2i((int)l, (int)(l >> 32));
   }

   public long asLong() {
      return (long)this.x & 4294967295L | ((long)this.z & 4294967295L) << 32;
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }
}
