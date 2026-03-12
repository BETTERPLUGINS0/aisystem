package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.tropicalfish;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticTropicalFishPattern extends AbstractMappedEntity implements TropicalFishPattern {
   private final TropicalFishPattern.Base base;

   @ApiStatus.Internal
   public StaticTropicalFishPattern(@Nullable TypesBuilderData data, TropicalFishPattern.Base base) {
      super(data);
      this.base = base;
   }

   public TropicalFishPattern.Base getBase() {
      return this.base;
   }
}
