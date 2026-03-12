package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.EnchantEffectComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.IComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.EnchantmentDefinition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EnchantmentType extends MappedEntity, CopyableEntity<EnchantmentType>, DeepComparableEntity {
   Component getDescription();

   EnchantmentDefinition getDefinition();

   MappedEntitySet<EnchantmentType> getExclusiveSet();

   MappedEntityRefSet<EnchantmentType> getExclusiveRefSet();

   StaticComponentMap getEffects();

   /** @deprecated */
   @Deprecated
   static EnchantmentType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version), data);
   }

   static EnchantmentType decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      Component description = (Component)compound.getOrThrow("description", wrapper.getSerializers(), wrapper);
      EnchantmentDefinition definition = EnchantmentDefinition.decode(compound, (PacketWrapper)wrapper);
      MappedEntityRefSet<EnchantmentType> exclusiveSet = (MappedEntityRefSet)Optional.ofNullable(compound.getTagOrNull("exclusive_set")).map((tag) -> {
         return MappedEntitySet.decodeRefSet(tag, wrapper);
      }).orElseGet(MappedEntitySet::createEmpty);
      StaticComponentMap effects = (StaticComponentMap)Optional.ofNullable(compound.getTagOrNull("effects")).map((tag) -> {
         return IComponentMap.decode(tag, (PacketWrapper)wrapper, EnchantEffectComponentTypes.getRegistry());
      }).orElse(StaticComponentMap.EMPTY);
      return new StaticEnchantmentType(data, description, definition, exclusiveSet, effects);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(EnchantmentType type, ClientVersion version) {
      return encode(type, PacketWrapper.createDummyWrapper(version));
   }

   static NBT encode(EnchantmentType type, PacketWrapper<?> wrapper) {
      NBTCompound compound = new NBTCompound();
      EnchantmentDefinition.encode(compound, wrapper, type.getDefinition());
      compound.set("description", type.getDescription(), wrapper.getSerializers(), wrapper);
      if (!type.getExclusiveRefSet().isEmpty()) {
         compound.set("exclusive_set", type.getExclusiveRefSet(), MappedEntitySet::encodeRefSet, wrapper);
      }

      if (!type.getEffects().isEmpty()) {
         compound.set("effects", type.getEffects(), IComponentMap::encode, wrapper);
      }

      return compound;
   }
}
