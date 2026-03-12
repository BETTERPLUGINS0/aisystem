package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.horse;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticHorseVariant extends AbstractMappedEntity implements HorseVariant {
   @ApiStatus.Internal
   public StaticHorseVariant(@Nullable TypesBuilderData data) {
      super(data);
   }
}
