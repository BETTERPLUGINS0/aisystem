package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.salmon;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticSalmonVariant extends AbstractMappedEntity implements SalmonVariant {
   @ApiStatus.Internal
   public StaticSalmonVariant(@Nullable TypesBuilderData data) {
      super(data);
   }
}
