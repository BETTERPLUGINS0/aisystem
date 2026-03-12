package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.poi;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticPoiType extends AbstractMappedEntity implements PoiType {
   @ApiStatus.Internal
   public StaticPoiType(@Nullable TypesBuilderData data) {
      super(data);
   }
}
