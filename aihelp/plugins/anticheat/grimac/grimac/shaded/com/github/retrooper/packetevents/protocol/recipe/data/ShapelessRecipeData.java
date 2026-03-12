package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.CraftingCategory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class ShapelessRecipeData implements RecipeData {
   private final String group;
   private final CraftingCategory category;
   private final Ingredient[] ingredients;
   private final ItemStack result;

   /** @deprecated */
   @Deprecated
   public ShapelessRecipeData(String group, Ingredient[] ingredients, ItemStack result) {
      this(group, CraftingCategory.MISC, ingredients, result);
   }

   public ShapelessRecipeData(String group, CraftingCategory category, Ingredient[] ingredients, ItemStack result) {
      this.group = group;
      this.category = category;
      this.ingredients = ingredients;
      this.result = result;
   }

   public static ShapelessRecipeData read(PacketWrapper<?> wrapper) {
      String group = wrapper.readString();
      CraftingCategory category = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3) ? (CraftingCategory)wrapper.readEnum((Enum[])CraftingCategory.values()) : CraftingCategory.MISC;
      Ingredient[] ingredients = (Ingredient[])wrapper.readArray(Ingredient::read, Ingredient.class);
      ItemStack result = wrapper.readItemStack();
      return new ShapelessRecipeData(group, category, ingredients, result);
   }

   public static void write(PacketWrapper<?> wrapper, ShapelessRecipeData data) {
      wrapper.writeString(data.group);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         wrapper.writeEnum(data.category);
      }

      wrapper.writeArray(data.ingredients, Ingredient::write);
      wrapper.writeItemStack(data.result);
   }

   public String getGroup() {
      return this.group;
   }

   public CraftingCategory getCategory() {
      return this.category;
   }

   public Ingredient[] getIngredients() {
      return this.ingredients;
   }

   public ItemStack getResult() {
      return this.result;
   }
}
