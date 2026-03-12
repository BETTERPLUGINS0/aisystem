package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AttributeType<T> extends MappedEntity {
   boolean isSynced();

   NbtCodec<T> getValueCodec();

   NbtCodec<AttributeModifier<T, ?>> getModifierCodec();
}
