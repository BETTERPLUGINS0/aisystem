package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticAttributeType<T> extends AbstractMappedEntity implements AttributeType<T> {
   @Nullable
   private final NbtCodec<T> valueCodec;
   private final NbtCodec<AttributeModifier<T, ?>> modifierCodec;

   @ApiStatus.Internal
   public StaticAttributeType(@Nullable TypesBuilderData data, @Nullable NbtCodec<T> valueCodec, NbtCodec<AttributeModifier<T, ?>> modifierCodec) {
      super(data);
      this.valueCodec = valueCodec;
      this.modifierCodec = modifierCodec;
   }

   public boolean isSynced() {
      return this.valueCodec != null;
   }

   public NbtCodec<T> getValueCodec() {
      if (this.valueCodec == null) {
         throw new UnsupportedOperationException();
      } else {
         return this.valueCodec;
      }
   }

   public NbtCodec<AttributeModifier<T, ?>> getModifierCodec() {
      return this.modifierCodec;
   }
}
