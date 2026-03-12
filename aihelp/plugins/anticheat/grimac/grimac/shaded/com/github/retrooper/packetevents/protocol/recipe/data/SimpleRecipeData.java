package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.CraftingCategory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class SimpleRecipeData implements RecipeData {
   private final CraftingCategory category;

   public SimpleRecipeData(CraftingCategory category) {
      this.category = category;
   }

   public static SimpleRecipeData read(PacketWrapper<?> wrapper) {
      CraftingCategory category = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3) ? (CraftingCategory)wrapper.readEnum((Enum[])CraftingCategory.values()) : CraftingCategory.MISC;
      return new SimpleRecipeData(category);
   }

   public static void write(PacketWrapper<?> wrapper, SimpleRecipeData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         wrapper.writeEnum(data.getCategory());
      }

   }

   public CraftingCategory getCategory() {
      return this.category;
   }
}
