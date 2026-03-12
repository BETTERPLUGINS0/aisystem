package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Instrument extends MappedEntity, CopyableEntity<Instrument>, DeepComparableEntity {
   Sound getSound();

   float getUseSeconds();

   default int getUseDuration() {
      return MathUtil.floor(this.getUseSeconds() * 20.0F);
   }

   float getRange();

   Component getDescription();

   static Instrument read(PacketWrapper<?> wrapper) {
      return (Instrument)wrapper.readMappedEntityOrDirect((IRegistry)Instruments.getRegistry(), Instrument::readDirect);
   }

   static Instrument readDirect(PacketWrapper<?> wrapper) {
      Sound sound = Sound.read(wrapper);
      float useSeconds = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2) ? wrapper.readFloat() : (float)wrapper.readVarInt() * 20.0F;
      float range = wrapper.readFloat();
      Component description = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2) ? wrapper.readComponent() : Component.empty();
      return new StaticInstrument(sound, useSeconds, range, (Component)description);
   }

   static void write(PacketWrapper<?> wrapper, Instrument instrument) {
      wrapper.writeMappedEntityOrDirect(instrument, Instrument::writeDirect);
   }

   static void writeDirect(PacketWrapper<?> wrapper, Instrument instrument) {
      Sound.write(wrapper, instrument.getSound());
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         wrapper.writeFloat(instrument.getUseSeconds());
      } else {
         wrapper.writeVarInt(instrument.getUseDuration());
      }

      wrapper.writeFloat(instrument.getRange());
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         wrapper.writeComponent(instrument.getDescription());
      }

   }

   /** @deprecated */
   @Deprecated
   static Instrument decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version), data);
   }

   static Instrument decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      Sound sound = (Sound)compound.getOrThrow("sound_event", Sound.CODEC, wrapper);
      float useSeconds = compound.getNumberTagOrThrow("use_duration").getAsFloat();
      float range = compound.getNumberTagOrThrow("range").getAsFloat();
      Component description = (Component)compound.getOrThrow("description", wrapper.getSerializers(), wrapper);
      return new StaticInstrument(data, sound, useSeconds, range, description);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(Instrument instrument, ClientVersion version) {
      return encode(PacketWrapper.createDummyWrapper(version), instrument);
   }

   static NBT encode(PacketWrapper<?> wrapper, Instrument instrument) {
      NBTCompound compound = new NBTCompound();
      compound.set("sound_event", instrument.getSound(), Sound.CODEC, wrapper);
      compound.setTag("use_duration", new NBTFloat(instrument.getUseSeconds()));
      compound.setTag("range", new NBTFloat(instrument.getRange()));
      compound.set("description", instrument.getDescription(), wrapper.getSerializers(), wrapper);
      return compound;
   }
}
