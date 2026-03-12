package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface DamageType extends MappedEntity, CopyableEntity<DamageType>, DeepComparableEntity {
   String getMessageId();

   DamageScaling getScaling();

   float getExhaustion();

   DamageEffects getEffects();

   DeathMessageType getDeathMessageType();

   static DamageType read(PacketWrapper<?> wrapper) {
      return (DamageType)wrapper.readMappedEntity((IRegistry)DamageTypes.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, DamageType damageType) {
      wrapper.writeMappedEntity(damageType);
   }

   static DamageType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      String messageId = ((NBTCompound)nbt).getStringTagValueOrThrow("message_id");
      DamageScaling scaling = (DamageScaling)AdventureIndexUtil.indexValueOrThrow(DamageScaling.ID_INDEX, ((NBTCompound)nbt).getStringTagValueOrThrow("scaling"));
      float exhaustion = ((NBTCompound)nbt).getNumberTagOrThrow("exhaustion").getAsFloat();
      DamageEffects effects = (DamageEffects)Optional.ofNullable(compound.getStringTagValueOrNull("effects")).map((id) -> {
         return (DamageEffects)AdventureIndexUtil.indexValueOrThrow(DamageEffects.ID_INDEX, id);
      }).orElse(DamageEffects.HURT);
      DeathMessageType deathMessageType = (DeathMessageType)Optional.ofNullable(compound.getStringTagValueOrNull("death_message_type")).map((id) -> {
         return (DeathMessageType)AdventureIndexUtil.indexValueOrThrow(DeathMessageType.ID_INDEX, id);
      }).orElse(DeathMessageType.DEFAULT);
      return new StaticDamageType(data, messageId, scaling, exhaustion, effects, deathMessageType);
   }

   static NBT encode(DamageType damageType, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("message_id", new NBTString(damageType.getMessageId()));
      compound.setTag("scaling", new NBTString(damageType.getScaling().getId()));
      compound.setTag("exhaustion", new NBTFloat(damageType.getExhaustion()));
      if (damageType.getEffects() != DamageEffects.HURT) {
         compound.setTag("effects", new NBTString(damageType.getEffects().getId()));
      }

      if (damageType.getDeathMessageType() != DeathMessageType.DEFAULT) {
         compound.setTag("death_message_type", new NBTString(damageType.getDeathMessageType().getId()));
      }

      return compound;
   }
}
