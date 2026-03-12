package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.EnchantmentDefinition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.ResolvableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticEnchantmentType extends AbstractMappedEntity implements EnchantmentType, ResolvableEntity {
   private final Component description;
   private final EnchantmentDefinition definition;
   private final MappedEntityRefSet<EnchantmentType> exclusiveSetRef;
   @Nullable
   private MappedEntitySet<EnchantmentType> exclusiveSet;
   private final StaticComponentMap effects;

   public StaticEnchantmentType(Component description, EnchantmentDefinition definition, MappedEntitySet<EnchantmentType> exclusiveSet, StaticComponentMap effects) {
      this((TypesBuilderData)null, description, definition, exclusiveSet, effects);
   }

   @ApiStatus.Internal
   public StaticEnchantmentType(@Nullable TypesBuilderData data, Component description, EnchantmentDefinition definition, MappedEntityRefSet<EnchantmentType> exclusiveSet, StaticComponentMap effects) {
      super(data);
      this.description = description;
      this.definition = definition;
      this.exclusiveSetRef = exclusiveSet;
      this.effects = effects;
   }

   public void doResolve(PacketWrapper<?> wrapper) {
      this.exclusiveSet = this.exclusiveSetRef.resolve((PacketWrapper)wrapper, EnchantmentTypes.getRegistry());
   }

   public EnchantmentType copy(@Nullable TypesBuilderData newData) {
      StaticEnchantmentType type = new StaticEnchantmentType(newData, this.description, this.definition, this.exclusiveSetRef, this.effects);
      type.exclusiveSet = this.exclusiveSet;
      return type;
   }

   public Component getDescription() {
      return this.description;
   }

   public EnchantmentDefinition getDefinition() {
      return this.definition;
   }

   public MappedEntitySet<EnchantmentType> getExclusiveSet() {
      if (this.exclusiveSet == null) {
         this.exclusiveSet = this.exclusiveSetRef.resolve(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), GlobalRegistryHolder.INSTANCE, EnchantmentTypes.getRegistry());
      }

      return this.exclusiveSet;
   }

   public MappedEntityRefSet<EnchantmentType> getExclusiveRefSet() {
      return this.exclusiveSetRef;
   }

   public StaticComponentMap getEffects() {
      return this.effects;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticEnchantmentType)) {
         return false;
      } else {
         StaticEnchantmentType that = (StaticEnchantmentType)obj;
         if (!this.description.equals(that.description)) {
            return false;
         } else if (!this.definition.equals(that.definition)) {
            return false;
         } else {
            return !this.exclusiveSetRef.equals(that.exclusiveSetRef) ? false : this.effects.equals(that.effects);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.description, this.definition, this.exclusiveSetRef, this.effects});
   }

   public String toString() {
      return "StaticEnchantmentType{description=" + this.description + ", definition=" + this.definition + ", exclusiveSetRef=" + this.exclusiveSetRef + ", effects=" + this.effects + "}";
   }
}
