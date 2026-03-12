package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class StaticRecipeSerializer<T extends RecipeData> extends AbstractMappedEntity implements RecipeSerializer<T> {
   private final PacketWrapper.Reader<T> reader;
   private final PacketWrapper.Writer<T> writer;
   @Nullable
   private final RecipeType legacyType;

   @ApiStatus.Internal
   public StaticRecipeSerializer(@Nullable TypesBuilderData data, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer, @Nullable RecipeType legacyType) {
      super(data);
      this.reader = reader;
      this.writer = writer;
      this.legacyType = legacyType;
   }

   public RecipeType getLegacyType() {
      return this.legacyType;
   }

   public T read(PacketWrapper<?> wrapper) {
      return (RecipeData)this.reader.apply(wrapper);
   }

   public void write(PacketWrapper<?> wrapper, T data) {
      this.writer.accept(wrapper, data);
   }
}
