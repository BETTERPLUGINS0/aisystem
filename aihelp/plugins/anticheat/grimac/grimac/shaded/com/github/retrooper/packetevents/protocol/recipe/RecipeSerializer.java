package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public interface RecipeSerializer<T extends RecipeData> extends MappedEntity {
   /** @deprecated */
   @Deprecated
   RecipeType getLegacyType();

   T read(PacketWrapper<?> wrapper);

   void write(PacketWrapper<?> wrapper, T data);
}
