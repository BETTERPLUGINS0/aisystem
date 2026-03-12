package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.mooshroom;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticMooshroomVariant extends AbstractMappedEntity implements MooshroomVariant {
   @ApiStatus.Internal
   public StaticMooshroomVariant(@Nullable TypesBuilderData data) {
      super(data);
   }
}
