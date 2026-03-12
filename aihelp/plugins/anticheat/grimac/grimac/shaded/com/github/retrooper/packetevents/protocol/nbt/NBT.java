package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class NBT {
   public abstract NBTType<?> getType();

   public abstract boolean equals(Object other);

   public abstract int hashCode();

   public String toString() {
      return "nbt";
   }

   public abstract NBT copy();

   public <T> T castOrThrow(Class<T> clazz) throws NbtCodecException {
      if (clazz.isInstance(this)) {
         return this;
      } else {
         throw new NbtCodecException("expected: " + clazz.getName() + ", actual: " + this);
      }
   }
}
