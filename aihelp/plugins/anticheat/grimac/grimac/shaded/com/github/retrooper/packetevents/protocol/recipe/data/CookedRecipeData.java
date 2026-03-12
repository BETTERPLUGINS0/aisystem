package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.CookingCategory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class CookedRecipeData implements RecipeData {
   private final String group;
   private final CookingCategory category;
   private final Ingredient ingredient;
   private final ItemStack result;
   private final float experience;
   private final int cookingTime;

   /** @deprecated */
   @Deprecated
   public CookedRecipeData(String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
      this(group, CookingCategory.MISC, ingredient, result, experience, cookingTime);
   }

   public CookedRecipeData(String group, CookingCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
      this.group = group;
      this.category = category;
      this.ingredient = ingredient;
      this.result = result;
      this.experience = experience;
      this.cookingTime = cookingTime;
   }

   public static CookedRecipeData read(PacketWrapper<?> wrapper) {
      String group = wrapper.readString();
      CookingCategory category = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3) ? (CookingCategory)wrapper.readEnum((Enum[])CookingCategory.values()) : CookingCategory.MISC;
      Ingredient ingredient = Ingredient.read(wrapper);
      ItemStack result = wrapper.readItemStack();
      float experience = wrapper.readFloat();
      int cookingTime = wrapper.readVarInt();
      return new CookedRecipeData(group, category, ingredient, result, experience, cookingTime);
   }

   public static void write(PacketWrapper<?> wrapper, CookedRecipeData data) {
      wrapper.writeString(data.group);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         wrapper.writeEnum(data.category);
      }

      Ingredient.write(wrapper, data.ingredient);
      wrapper.writeItemStack(data.result);
      wrapper.writeFloat(data.experience);
      wrapper.writeVarInt(data.cookingTime);
   }

   public String getGroup() {
      return this.group;
   }

   public CookingCategory getCategory() {
      return this.category;
   }

   public Ingredient getIngredient() {
      return this.ingredient;
   }

   public ItemStack getResult() {
      return this.result;
   }

   public float getExperience() {
      return this.experience;
   }

   public int getCookingTime() {
      return this.cookingTime;
   }
}
