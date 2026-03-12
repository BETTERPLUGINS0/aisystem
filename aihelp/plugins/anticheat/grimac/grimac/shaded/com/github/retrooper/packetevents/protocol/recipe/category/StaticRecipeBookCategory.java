package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.category;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticRecipeBookCategory extends AbstractMappedEntity implements RecipeBookCategory {
   @ApiStatus.Internal
   public StaticRecipeBookCategory(@Nullable TypesBuilderData data) {
      super(data);
   }
}
