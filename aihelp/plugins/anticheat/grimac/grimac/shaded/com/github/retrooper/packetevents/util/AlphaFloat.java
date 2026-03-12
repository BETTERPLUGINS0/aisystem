package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AlphaFloat {
   public static final NbtCodec<AlphaFloat> CODEC = new NbtCodec<AlphaFloat>() {
      public AlphaFloat decode(NBT nbt, PacketWrapper<?> wrapper) throws NbtCodecException {
         if (nbt instanceof NBTNumber) {
            return new AlphaFloat(((NBTNumber)nbt).getAsFloat(), 1.0F);
         } else {
            NBTCompound compound = (NBTCompound)nbt.castOrThrow(NBTCompound.class);
            float value = compound.getNumberTagValueOrThrow("value").floatValue();
            float alpha = compound.getNumberTagValueOrDefault("alpha", 1.0F).floatValue();
            return new AlphaFloat(value, alpha);
         }
      }

      public NBT encode(PacketWrapper<?> wrapper, AlphaFloat value) throws NbtCodecException {
         if (value.alpha == 1.0F) {
            return new NBTFloat(value.value);
         } else {
            NBTCompound compound = new NBTCompound();
            compound.setTag("value", new NBTFloat(value.value));
            compound.setTag("alpha", new NBTFloat(value.alpha));
            return compound;
         }
      }
   };
   private final float value;
   private final float alpha;

   public AlphaFloat(float value, float alpha) {
      this.value = value;
      this.alpha = alpha;
   }

   public float getValue() {
      return this.value;
   }

   public float getAlpha() {
      return this.alpha;
   }
}
