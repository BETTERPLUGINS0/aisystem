package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.llama;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticLlamaVariant extends AbstractMappedEntity implements LlamaVariant {
   @ApiStatus.Internal
   public StaticLlamaVariant(@Nullable TypesBuilderData data) {
      super(data);
   }
}
