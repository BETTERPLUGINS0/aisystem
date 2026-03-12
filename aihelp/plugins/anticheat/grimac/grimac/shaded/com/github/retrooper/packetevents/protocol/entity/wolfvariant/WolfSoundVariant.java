package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface WolfSoundVariant extends MappedEntity, CopyableEntity<WolfSoundVariant>, DeepComparableEntity {
   Sound getAmbientSound();

   Sound getDeathSound();

   Sound getGrowlSound();

   Sound getHurtSound();

   Sound getPantSound();

   Sound getWhineSound();

   static WolfSoundVariant read(PacketWrapper<?> wrapper) {
      return (WolfSoundVariant)wrapper.readMappedEntity((IRegistry)WolfSoundVariants.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, WolfSoundVariant variant) {
      wrapper.writeMappedEntity(variant);
   }

   static WolfSoundVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      Sound ambientSound = Sound.decode(compound.getTagOrThrow("ambient_sound"), version);
      Sound deathSound = Sound.decode(compound.getTagOrThrow("death_sound"), version);
      Sound growlSound = Sound.decode(compound.getTagOrThrow("growl_sound"), version);
      Sound hurtSound = Sound.decode(compound.getTagOrThrow("hurt_sound"), version);
      Sound pantSound = Sound.decode(compound.getTagOrThrow("pant_sound"), version);
      Sound whineSound = Sound.decode(compound.getTagOrThrow("whine_sound"), version);
      return new StaticWolfSoundVariant(data, ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound);
   }

   static NBT encode(WolfSoundVariant variant, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("ambient_sound", Sound.encode(variant.getAmbientSound(), version));
      compound.setTag("death_sound", Sound.encode(variant.getDeathSound(), version));
      compound.setTag("growl_sound", Sound.encode(variant.getGrowlSound(), version));
      compound.setTag("hurt_sound", Sound.encode(variant.getHurtSound(), version));
      compound.setTag("pant_sound", Sound.encode(variant.getPantSound(), version));
      compound.setTag("whine_sound", Sound.encode(variant.getWhineSound(), version));
      return compound;
   }
}
