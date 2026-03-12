package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.fox;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticFoxVariant extends AbstractMappedEntity implements FoxVariant {
   @ApiStatus.Internal
   public StaticFoxVariant(@Nullable TypesBuilderData data) {
      super(data);
   }
}
