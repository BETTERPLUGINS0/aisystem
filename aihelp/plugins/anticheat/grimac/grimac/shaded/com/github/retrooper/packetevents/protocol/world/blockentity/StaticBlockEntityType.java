package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.blockentity;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticBlockEntityType extends AbstractMappedEntity implements BlockEntityType {
   @ApiStatus.Internal
   public StaticBlockEntityType(@Nullable TypesBuilderData data) {
      super(data);
   }
}
