package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.CraftingCategory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class ShapedRecipeData implements RecipeData {
   private final String group;
   private final CraftingCategory category;
   private final ItemStack result;
   private final boolean showNotification;
   private final int width;
   private final int height;
   private final Ingredient[] ingredients;

   /** @deprecated */
   @Deprecated
   public ShapedRecipeData(int width, int height, String group, Ingredient[] ingredients, ItemStack result) {
      this(group, CraftingCategory.MISC, result, true, width, height, ingredients);
   }

   public ShapedRecipeData(String group, CraftingCategory category, ItemStack result, boolean showNotification, int width, int height, Ingredient[] ingredients) {
      if (width * height != ingredients.length) {
         throw new IllegalArgumentException("Illegal ingredients length, found " + ingredients.length + " but expected " + width + " * " + height);
      } else {
         this.group = group;
         this.category = category;
         this.result = result;
         this.showNotification = showNotification;
         this.width = width;
         this.height = height;
         this.ingredients = ingredients;
      }
   }

   public static ShapedRecipeData read(PacketWrapper<?> wrapper) {
      int width = 0;
      int height = 0;
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_3)) {
         width = wrapper.readVarInt();
         height = wrapper.readVarInt();
      }

      String group = wrapper.readString();
      CraftingCategory category = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3) ? (CraftingCategory)wrapper.readEnum((Enum[])CraftingCategory.values()) : CraftingCategory.MISC;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         width = wrapper.readVarInt();
         height = wrapper.readVarInt();
      }

      Ingredient[] ingredients = new Ingredient[width * height];

      for(int i = 0; i < ingredients.length; ++i) {
         ingredients[i] = Ingredient.read(wrapper);
      }

      ItemStack result = wrapper.readItemStack();
      boolean showNotification = true;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4)) {
         showNotification = wrapper.readBoolean();
      }

      return new ShapedRecipeData(group, category, result, showNotification, width, height, ingredients);
   }

   public static void write(PacketWrapper<?> wrapper, ShapedRecipeData data) {
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_3)) {
         wrapper.writeVarInt(data.width);
         wrapper.writeVarInt(data.height);
      }

      wrapper.writeString(data.group);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         wrapper.writeEnum(data.category);
      }

      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         wrapper.writeVarInt(data.width);
         wrapper.writeVarInt(data.height);
      }

      Ingredient[] var2 = data.ingredients;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Ingredient ingredient = var2[var4];
         Ingredient.write(wrapper, ingredient);
      }

      wrapper.writeItemStack(data.result);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4)) {
         wrapper.writeBoolean(data.showNotification);
      }

   }

   public String getGroup() {
      return this.group;
   }

   public CraftingCategory getCategory() {
      return this.category;
   }

   public ItemStack getResult() {
      return this.result;
   }

   public boolean isShowNotification() {
      return this.showNotification;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public Ingredient[] getIngredients() {
      return this.ingredients;
   }
}
