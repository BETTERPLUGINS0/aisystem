package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticPotionType extends AbstractMappedEntity implements PotionType {
   @ApiStatus.Internal
   public StaticPotionType(@Nullable TypesBuilderData data) {
      super(data);
   }
}
