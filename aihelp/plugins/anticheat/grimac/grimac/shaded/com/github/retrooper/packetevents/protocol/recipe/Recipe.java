package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class Recipe<T extends RecipeData> {
   private final ResourceLocation key;
   private final RecipeSerializer<T> serializer;
   private final T data;

   /** @deprecated */
   @Deprecated
   public Recipe(RecipeType serializer, String key, RecipeData data) {
      this(new ResourceLocation(key), serializer.getSerializer(), data);
   }

   public Recipe(ResourceLocation key, RecipeSerializer<T> serializer, T data) {
      this.key = key;
      this.serializer = serializer;
      this.data = data;
   }

   public static Recipe<?> read(PacketWrapper<?> wrapper) {
      ResourceLocation key;
      RecipeSerializer serializer;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         key = wrapper.readIdentifier();
         serializer = (RecipeSerializer)wrapper.readMappedEntity(RecipeSerializers::getById);
      } else {
         serializer = RecipeSerializers.getByName(wrapper.readIdentifier().toString());
         key = wrapper.readIdentifier();
      }

      return read(wrapper, key, serializer);
   }

   private static <T extends RecipeData> Recipe<T> read(PacketWrapper<?> wrapper, ResourceLocation key, RecipeSerializer<T> serializer) {
      T data = serializer.read(wrapper);
      return new Recipe(key, serializer, data);
   }

   public static <T extends RecipeData> void write(PacketWrapper<?> wrapper, Recipe<T> recipe) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         wrapper.writeIdentifier(recipe.key);
         wrapper.writeMappedEntity(recipe.serializer);
      } else {
         wrapper.writeIdentifier(recipe.serializer.getName());
         wrapper.writeIdentifier(recipe.key);
      }

      recipe.serializer.write(wrapper, recipe.data);
   }

   /** @deprecated */
   @Deprecated
   public RecipeType getType() {
      return this.serializer.getLegacyType();
   }

   public String getIdentifier() {
      return this.key.toString();
   }

   public ResourceLocation getKey() {
      return this.key;
   }

   public RecipeSerializer<T> getSerializer() {
      return this.serializer;
   }

   public T getData() {
      return this.data;
   }

   public String toString() {
      return "Recipe{key=" + this.key + ", serializer=" + this.serializer + ", data=" + this.data + '}';
   }
}
