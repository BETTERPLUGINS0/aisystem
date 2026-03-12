package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Sound extends MappedEntity, DeepComparableEntity {
   NbtCodec<Sound> CODEC = new NbtCodec<Sound>() {
      public Sound decode(NBT nbt, PacketWrapper<?> wrapper) {
         if (nbt instanceof NBTString) {
            return Sounds.getByNameOrCreate(((NBTString)nbt).getValue());
         } else {
            NBTCompound compound = (NBTCompound)nbt.castOrThrow(NBTCompound.class);
            ResourceLocation soundId = (ResourceLocation)compound.getOrThrow("sound_id", ResourceLocation.CODEC, wrapper);
            Float range = (Float)compound.getOrNull("range", NbtCodecs.FLOAT, wrapper);
            return new StaticSound(soundId, range);
         }
      }

      public NBT encode(PacketWrapper<?> wrapper, Sound value) {
         if (value.isRegistered()) {
            return new NBTString(value.getName().toString());
         } else {
            NBTCompound compound = new NBTCompound();
            compound.set("sound_id", value.getSoundId(), ResourceLocation.CODEC, wrapper);
            if (value.getRange() != null) {
               compound.set("range", value.getRange(), NbtCodecs.FLOAT, wrapper);
            }

            return compound;
         }
      }
   };

   ResourceLocation getSoundId();

   @Nullable
   Float getRange();

   static Sound read(PacketWrapper<?> wrapper) {
      return (Sound)wrapper.readMappedEntityOrDirect(Sounds::getById, Sound::readDirect);
   }

   static Sound readDirect(PacketWrapper<?> wrapper) {
      ResourceLocation soundId = wrapper.readIdentifier();
      Float range = (Float)wrapper.readOptional(PacketWrapper::readFloat);
      return new StaticSound(soundId, range);
   }

   static void write(PacketWrapper<?> wrapper, Sound sound) {
      wrapper.writeMappedEntityOrDirect(sound, Sound::writeDirect);
   }

   static void writeDirect(PacketWrapper<?> wrapper, Sound sound) {
      wrapper.writeIdentifier(sound.getSoundId());
      wrapper.writeOptional(sound.getRange(), PacketWrapper::writeFloat);
   }

   /** @deprecated */
   @Deprecated
   static Sound decode(NBT nbt, ClientVersion version) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version));
   }

   /** @deprecated */
   @Deprecated
   static Sound decode(NBT nbt, PacketWrapper<?> wrapper) {
      return (Sound)CODEC.decode(nbt, wrapper);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(Sound sound, ClientVersion version) {
      return encode(PacketWrapper.createDummyWrapper(version), sound);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(PacketWrapper<?> wrapper, Sound sound) {
      return CODEC.encode(wrapper, sound);
   }
}
