package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticArmorMaterial extends AbstractMappedEntity implements ArmorMaterial {
   @ApiStatus.Internal
   public StaticArmorMaterial(@Nullable TypesBuilderData data) {
      super(data);
   }
}
