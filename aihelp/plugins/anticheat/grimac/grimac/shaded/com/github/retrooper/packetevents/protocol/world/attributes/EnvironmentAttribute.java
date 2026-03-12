package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EnvironmentAttribute<T> extends MappedEntity {
   boolean isSynced();

   AttributeType<T> getType();

   T getDefaultValue();
}
