package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Keyframe<T> {
   private final int ticks;
   private final T value;

   public Keyframe(int ticks, T value) {
      this.ticks = ticks;
      this.value = value;
   }

   public static <T> NbtCodec<Keyframe<T>> codec(NbtCodec<T> codec) {
      return (new NbtMapCodec<Keyframe<T>>() {
         public Keyframe<T> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            int ticks = compound.getNumberTagOrThrow("ticks").getAsInt();
            T value = compound.getOrThrow("value", codec, wrapper);
            return new Keyframe(ticks, value);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Keyframe<T> value) throws NbtCodecException {
            compound.setTag("ticks", new NBTInt(value.ticks));
            compound.set("value", value.value, codec, wrapper);
         }
      }).codec();
   }

   public int getTicks() {
      return this.ticks;
   }

   public T getValue() {
      return this.value;
   }
}
