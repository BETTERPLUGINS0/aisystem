package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class RecipeDisplayTypes {
   private static final VersionedRegistry<RecipeDisplayType<?>> REGISTRY = new VersionedRegistry("recipe_display");
   public static final RecipeDisplayType<ShapelessCraftingRecipeDisplay> CRAFTING_SHAPELESS = register("crafting_shapeless", ShapelessCraftingRecipeDisplay::read, ShapelessCraftingRecipeDisplay::write);
   public static final RecipeDisplayType<ShapedCraftingRecipeDisplay> CRAFTING_SHAPED = register("crafting_shaped", ShapedCraftingRecipeDisplay::read, ShapedCraftingRecipeDisplay::write);
   public static final RecipeDisplayType<FurnaceRecipeDisplay> FURNACE = register("furnace", FurnaceRecipeDisplay::read, FurnaceRecipeDisplay::write);
   public static final RecipeDisplayType<StonecutterRecipeDisplay> STONECUTTER = register("stonecutter", StonecutterRecipeDisplay::read, StonecutterRecipeDisplay::write);
   public static final RecipeDisplayType<SmithingRecipeDisplay> SMITHING = register("smithing", SmithingRecipeDisplay::read, SmithingRecipeDisplay::write);

   private RecipeDisplayTypes() {
   }

   private static <T extends RecipeDisplay<?>> RecipeDisplayType<T> register(String id, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
      return (RecipeDisplayType)REGISTRY.define(id, (data) -> {
         return new StaticRecipeDisplayType(data, reader, writer);
      });
   }

   public static VersionedRegistry<RecipeDisplayType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
