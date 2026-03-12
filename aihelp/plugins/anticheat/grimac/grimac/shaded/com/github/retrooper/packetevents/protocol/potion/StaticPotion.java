package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticPotion extends AbstractMappedEntity implements Potion {
   @ApiStatus.Internal
   public StaticPotion(@Nullable TypesBuilderData data) {
      super(data);
   }
}
