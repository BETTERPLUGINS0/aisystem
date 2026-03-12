package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.rabbit;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticRabbitVariant extends AbstractMappedEntity implements RabbitVariant {
   @ApiStatus.Internal
   public StaticRabbitVariant(@Nullable TypesBuilderData data) {
      super(data);
   }
}
